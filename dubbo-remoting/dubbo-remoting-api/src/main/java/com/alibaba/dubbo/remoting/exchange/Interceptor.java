package com.alibaba.dubbo.remoting.exchange;

import com.alibaba.dubbo.remoting.RemotingException;

/**
 * @author Xs
 */
public interface Interceptor {

    Response intercept(Chain chain);

    interface Chain {

        Request request();

        Response proceed(Request request, int timeout) throws RemotingException;

    }
}
