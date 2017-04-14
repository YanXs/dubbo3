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
package com.alibaba.dubbo.remoting.transport.netty;

import com.alibaba.dubbo.remoting.exception.RemotingException;
import com.alibaba.dubbo.remoting.exchange.ExchangeChannel;
import com.alibaba.dubbo.remoting.exchange.ExchangeServer;
import com.alibaba.dubbo.remoting.exchange.support.Replier;
import junit.framework.TestCase;

/**
 * ClientToServer
 *
 * @author william.liangf
 */
public abstract class ClientToServerTest extends TestCase {

    protected static final String LOCALHOST = "127.0.0.1";

    protected ExchangeServer server;

    protected ExchangeChannel client;

    protected WorldHandler handler = new WorldHandler();

    protected abstract ExchangeServer newServer(int port, Replier<?> receiver) throws RemotingException;

    protected abstract ExchangeChannel newClient(int port) throws RemotingException;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        int port = (int) (1000 * Math.random() + 10000);
        server = newServer(port, handler);
        client = newClient(port);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        try {
            if (server != null)
                server.close();
        } finally {
            if (client != null)
                client.close();
        }
    }
}