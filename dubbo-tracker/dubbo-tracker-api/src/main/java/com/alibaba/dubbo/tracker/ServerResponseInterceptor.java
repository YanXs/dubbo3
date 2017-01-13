package com.alibaba.dubbo.tracker;

public interface ServerResponseInterceptor {

    void handle(RpcResponse response);
}
