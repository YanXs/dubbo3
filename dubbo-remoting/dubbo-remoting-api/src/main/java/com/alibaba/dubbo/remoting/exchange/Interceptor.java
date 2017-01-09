package com.alibaba.dubbo.remoting.exchange;

import com.alibaba.dubbo.remoting.RemotingException;

/**
 * @author Xs
 */
public interface Interceptor {

    ResponseFuture intercept(Chain chain);

    interface Chain {

        Object request();

        ResponseFuture proceed(Object request, int timeout) throws RemotingException;

    }
}
