package com.alibaba.dubbo.remoting.exchange;

import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.RemotingException;

public interface ExchangeChannelV2 extends Channel {
    /**
     * send request.
     *
     * @param request
     * @return response
     * @throws RemotingException
     */
    Response request(Request request) throws RemotingException;

    /**
     * send request.
     *
     * @param request
     * @param timeout
     * @return response future
     * @throws RemotingException
     */
    Response request(Request request, int timeout) throws RemotingException;

    /**
     * get message handler.
     *
     * @return message handler
     */
    ExchangeHandler getExchangeHandler();

    /**
     * graceful close.
     *
     * @param timeout
     */
    void close(int timeout);

}
