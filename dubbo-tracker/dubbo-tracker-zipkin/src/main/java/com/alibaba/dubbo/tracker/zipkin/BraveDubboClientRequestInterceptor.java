package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.ClientRequestAdapter;
import com.alibaba.dubbo.tracker.ClientRequestInterceptor;

public class BraveDubboClientRequestInterceptor implements ClientRequestInterceptor {

    private final com.github.kristofa.brave.ClientRequestInterceptor clientRequestInterceptor;

    public BraveDubboClientRequestInterceptor(com.github.kristofa.brave.ClientRequestInterceptor clientRequestInterceptor) {
        this.clientRequestInterceptor = clientRequestInterceptor;
    }

    @Override
    public void handle(ClientRequestAdapter clientRequestAdapter) {
        if (clientRequestAdapter.isTraceable()) {
            clientRequestInterceptor.handle((com.github.kristofa.brave.ClientRequestAdapter) clientRequestAdapter);
        }
    }
}
