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
import com.alibaba.dubbo.remoting.Transporters;
import com.alibaba.dubbo.remoting.exception.RemotingException;
import com.alibaba.dubbo.remoting.exchange.ExchangeClient;
import com.alibaba.dubbo.remoting.exchange.ExchangeHandler;
import com.alibaba.dubbo.remoting.exchange.ExchangeServer;
import com.alibaba.dubbo.remoting.exchange.Exchanger;
import com.alibaba.dubbo.remoting.message.Interceptor;
import com.alibaba.dubbo.remoting.transport.DecodeHandler;
import com.alibaba.dubbo.tracker.DubboRequestInterceptorBuilder;
import com.alibaba.dubbo.tracker.DubboRequestSpanNameProvider;

/**
 * DefaultMessenger
 *
 * @author william.liangf
 */
public class HeaderExchanger implements Exchanger {

    public static final String NAME = "header";

    private DubboRequestInterceptorBuilder dubboRequestInterceptorBuilder;

    public void setDubboRequestInterceptorBuilder(DubboRequestInterceptorBuilder dubboRequestInterceptorBuilder) {
        this.dubboRequestInterceptorBuilder = dubboRequestInterceptorBuilder;
    }

    public ExchangeClient connect(URL url, ExchangeHandler handler) throws RemotingException {
        HeaderExchangeClient headerExchangeClient = new HeaderExchangeClient(Transporters.connect(url, new DecodeHandler(new HeaderExchangeHandler(handler))));
        if (dubboRequestInterceptorBuilder != null) {
            Interceptor interceptor = dubboRequestInterceptorBuilder.build(url, DubboRequestSpanNameProvider.getInstance());
            if (interceptor != null) {
                headerExchangeClient.addInterceptor(interceptor);
            }
        }
        return headerExchangeClient;
    }

    public ExchangeServer bind(URL url, ExchangeHandler handler) throws RemotingException {
        HeaderExchangeHandler exchangeHandler = new HeaderExchangeHandler(handler);
        if (dubboRequestInterceptorBuilder != null) {
            Interceptor interceptor = dubboRequestInterceptorBuilder.build(url, DubboRequestSpanNameProvider.getInstance());
            if (interceptor != null) {
                exchangeHandler.addInterceptor(interceptor);
            }
        }
        return new HeaderExchangeServer(Transporters.bind(url, new DecodeHandler(exchangeHandler)));
    }
}