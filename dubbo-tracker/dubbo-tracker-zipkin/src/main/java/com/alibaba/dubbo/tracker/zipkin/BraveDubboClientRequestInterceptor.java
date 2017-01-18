package com.alibaba.dubbo.tracker.zipkin;

import com.github.kristofa.brave.ClientRequestInterceptor;

public class BraveDubboClientRequestInterceptor implements DubboClientRequestInterceptor {

    private final ClientRequestInterceptor clientRequestInterceptor;

    public BraveDubboClientRequestInterceptor(ClientRequestInterceptor clientRequestInterceptor) {
        this.clientRequestInterceptor = clientRequestInterceptor;
    }

    @Override
    public void handle(DubboClientRequestAdapter clientRequestAdapter) {
        if (clientRequestAdapter.isTraceable()) {
            clientRequestInterceptor.handle(clientRequestAdapter);
        }
    }
}
