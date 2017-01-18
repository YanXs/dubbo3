package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.ClientResponseInterceptor;

public interface DubboClientResponseInterceptor extends ClientResponseInterceptor {

    void handle(DubboClientResponseAdapter clientResponseAdapter);
}
