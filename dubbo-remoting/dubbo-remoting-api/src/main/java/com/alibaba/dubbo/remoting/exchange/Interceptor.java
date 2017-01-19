package com.alibaba.dubbo.remoting.exchange;

import com.alibaba.dubbo.remoting.RemotingException;

/**
 * @author Xs
 */
public interface Interceptor {

    Response intercept(Chain chain) throws RemotingException;

    interface Chain {

        Request request();

        int timeout();

        Response proceed(Request request, int timeout) throws RemotingException;

        Response proceed(Request request) throws RemotingException;

    }
}
