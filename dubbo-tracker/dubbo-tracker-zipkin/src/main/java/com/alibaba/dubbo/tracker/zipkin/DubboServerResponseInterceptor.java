package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.ServerResponseInterceptor;

/**
 * @author Xs
 */
public interface DubboServerResponseInterceptor extends ServerResponseInterceptor {

    void handle(DubboServerResponseAdapter serverResponseAdapter);
}
