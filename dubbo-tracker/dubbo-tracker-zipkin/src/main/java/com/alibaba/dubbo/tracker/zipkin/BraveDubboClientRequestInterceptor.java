package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.DubboRequestSpanNameProvider;
import com.github.kristofa.brave.ClientRequestInterceptor;

public class BraveDubboClientRequestInterceptor implements DubboClientRequestInterceptor {

    private final ClientRequestInterceptor clientRequestInterceptor;

    private final DubboRequestSpanNameProvider spanNameProvider;

    public BraveDubboClientRequestInterceptor(ClientRequestInterceptor clientRequestInterceptor,
                                              DubboRequestSpanNameProvider spanNameProvider) {
        this.clientRequestInterceptor = clientRequestInterceptor;
        this.spanNameProvider = spanNameProvider;
    }

    @Override
    public void handle(DubboClientRequestAdapter clientRequestAdapter) {
        if (clientRequestAdapter.isTraceable()) {
            clientRequestInterceptor.handle(clientRequestAdapter);
        }
    }
}
