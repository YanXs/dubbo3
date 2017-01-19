package com.alibaba.dubbo.tracker;

/**
 * RpcTracker引擎
 *
 * @author Xs
 */
public interface RpcTrackerEngine {

    Object clientRequestInterceptor();

    Object clientResponseInterceptor();

    Object serverRequestInterceptor();

    Object serverResponseInterceptor();
}
