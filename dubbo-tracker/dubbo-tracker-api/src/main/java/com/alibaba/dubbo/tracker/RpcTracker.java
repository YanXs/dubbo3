package com.alibaba.dubbo.tracker;

/**
 * @author Xs
 */
public interface RpcTracker {

    ClientRequestInterceptor requestInterceptor();

    ClientResponseInterceptor responseInterceptor();
}
