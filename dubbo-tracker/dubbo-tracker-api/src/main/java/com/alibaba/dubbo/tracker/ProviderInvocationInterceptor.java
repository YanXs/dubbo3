package com.alibaba.dubbo.tracker;

public interface ProviderInvocationInterceptor {

    void handle(RpcRequest request);
}
