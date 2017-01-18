package com.alibaba.dubbo.tracker.zipkin;

import com.github.kristofa.brave.ServerRequestInterceptor;

/**
 * @author Xs
 */
public class BraveDubboServerRequestInterceptor implements DubboServerRequestInterceptor {

    private final ServerRequestInterceptor serverRequestInterceptor;

    public BraveDubboServerRequestInterceptor(ServerRequestInterceptor serverRequestInterceptor) {
        this.serverRequestInterceptor = serverRequestInterceptor;
    }

    @Override
    public void handle(DubboServerRequestAdapter serverRequestAdapter) {
        if (serverRequestAdapter.isTraceable()) {
            serverRequestInterceptor.handle(serverRequestAdapter);
        }
    }

}
