package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.ClientRequestInterceptor;

public interface DubboClientRequestInterceptor extends ClientRequestInterceptor {

    void handle(DubboClientRequestAdapter clientRequestAdapter);
}
