package com.alibaba.dubbo.tracker;

public interface ProviderResultInterceptor {

    void handle(RpcResponse response);
}
