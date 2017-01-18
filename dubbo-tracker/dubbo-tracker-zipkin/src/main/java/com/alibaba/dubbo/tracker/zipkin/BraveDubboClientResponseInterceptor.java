package com.alibaba.dubbo.tracker.zipkin;

import com.github.kristofa.brave.ClientResponseInterceptor;

public class BraveDubboClientResponseInterceptor implements DubboClientResponseInterceptor {

    private final ClientResponseInterceptor clientResponseInterceptor;

    public BraveDubboClientResponseInterceptor(ClientResponseInterceptor clientResponseInterceptor) {
        this.clientResponseInterceptor = clientResponseInterceptor;
    }

    @Override
    public void handle(DubboClientResponseAdapter clientResponseAdapter) {
        if (clientResponseAdapter.isTraceable()) {
            clientResponseInterceptor.handle(clientResponseAdapter);
        }
    }
}
