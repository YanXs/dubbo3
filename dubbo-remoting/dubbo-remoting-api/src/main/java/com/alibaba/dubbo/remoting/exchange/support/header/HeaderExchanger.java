/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.remoting.exchange.support.header;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.exception.RemotingException;
import com.alibaba.dubbo.remoting.Transporters;
import com.alibaba.dubbo.remoting.exchange.ExchangeClient;
import com.alibaba.dubbo.remoting.exchange.ExchangeHandler;
import com.alibaba.dubbo.remoting.exchange.ExchangeServer;
import com.alibaba.dubbo.remoting.exchange.Exchanger;
import com.alibaba.dubbo.remoting.transport.DecodeHandler;
import com.alibaba.dubbo.tracker.DubboRequestSpanNameProvider;
import com.alibaba.dubbo.tracker.RpcTrackerEngine;
import com.alibaba.dubbo.tracker.RpcTrackerManager;
import com.alibaba.dubbo.tracker.zipkin.DubboClientRequestResponseInterceptor;
import com.alibaba.dubbo.tracker.zipkin.DubboRpcTracker;
import com.alibaba.dubbo.tracker.zipkin.DubboServerRequestResponseInterceptor;

/**
 * DefaultMessenger
 *
 * @author william.liangf
 */
public class HeaderExchanger implements Exchanger {

    public static final String NAME = "header";

    // for test
    public static RpcTrackerEngine rpcTrackerEngine;
    public static DubboRpcTracker rpcTracker;

    static {
        URL url = URL.valueOf("zipkin://localhost:9411?application=consumer&collector=http&sampler=counting&rate=1");
        rpcTrackerEngine = RpcTrackerManager.createRpcTrackerEngine(url);
        url = URL.valueOf("dubbo://localhost:9411?application=consumer&collector=http&sampler=counting&rate=0.2");
        rpcTracker = (DubboRpcTracker) RpcTrackerManager.createRpcTracker(url);
    }

    public ExchangeClient connect(URL url, ExchangeHandler handler) throws RemotingException {
        HeaderExchangeClient headerExchangeClient = new HeaderExchangeClient(Transporters.connect(url, new DecodeHandler(new HeaderExchangeHandler(handler))));
        DubboClientRequestResponseInterceptor interceptor = new DubboClientRequestResponseInterceptor(rpcTracker.clientRequestInterceptor(),
                rpcTracker.clientResponseInterceptor(), new DubboRequestSpanNameProvider());
        headerExchangeClient.addInterceptor(interceptor);
        return headerExchangeClient;
    }

    public ExchangeServer bind(URL url, ExchangeHandler handler) throws RemotingException {
        HeaderExchangeHandler exchangeHandler = new HeaderExchangeHandler(handler);
        DubboServerRequestResponseInterceptor interceptor = new DubboServerRequestResponseInterceptor(rpcTracker.serverRequestInterceptor(),
                rpcTracker.serverResponseInterceptor(), new DubboRequestSpanNameProvider());
        exchangeHandler.addInterceptor(interceptor);
        return new HeaderExchangeServer(Transporters.bind(url, new DecodeHandler(exchangeHandler)));
    }

}