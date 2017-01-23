package com.alibaba.dubbo.remoting.message;


import com.alibaba.dubbo.remoting.exception.RemotingException;

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
