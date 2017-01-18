package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.ServerRequestInterceptor;

/**
 * @author Xs
 */
public interface DubboServerRequestInterceptor extends ServerRequestInterceptor {

    void handle(DubboServerRequestAdapter serverRequestAdapter);

}
