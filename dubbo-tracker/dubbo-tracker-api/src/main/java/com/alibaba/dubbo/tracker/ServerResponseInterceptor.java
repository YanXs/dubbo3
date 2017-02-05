package com.alibaba.dubbo.tracker;

public interface ServerResponseInterceptor {

    void handle(ServerResponseAdapter serverResponseAdapter);

}
