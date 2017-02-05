package com.alibaba.dubbo.tracker;

public interface ServerRequestInterceptor {

    void handle(ServerRequestAdapter serverRequestAdapter);

}
