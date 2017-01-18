package com.alibaba.dubbo.tracker;

public interface RpcTrackerEngine {

    Object clientRequestInterceptor();

    Object clientResponseInterceptor();

    Object serverRequestInterceptor();

    Object serverResponseInterceptor();
}
