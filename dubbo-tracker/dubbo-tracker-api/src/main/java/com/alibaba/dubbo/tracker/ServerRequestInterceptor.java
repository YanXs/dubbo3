package com.alibaba.dubbo.tracker;

public interface ServerRequestInterceptor {

    void handle(RpcRequest request);
}
