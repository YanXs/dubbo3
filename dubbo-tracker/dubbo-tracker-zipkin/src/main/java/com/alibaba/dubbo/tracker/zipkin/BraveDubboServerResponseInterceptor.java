package com.alibaba.dubbo.tracker.zipkin;

import com.github.kristofa.brave.ServerResponseInterceptor;

/**
 * @author Xs
 */
public class BraveDubboServerResponseInterceptor implements DubboServerResponseInterceptor {

    private final ServerResponseInterceptor serverResponseInterceptor;

    public BraveDubboServerResponseInterceptor(ServerResponseInterceptor serverResponseInterceptor) {
        this.serverResponseInterceptor = serverResponseInterceptor;
    }

    @Override
    public void handle(DubboServerResponseAdapter serverResponseAdapter) {
        if (serverResponseAdapter.isTraceable()) {
            serverResponseInterceptor.handle(serverResponseAdapter);
        }
    }

}
