package com.alibaba.dubbo.tracker.zipkin;


import com.alibaba.dubbo.tracker.ClientResponseAdapter;
import com.alibaba.dubbo.tracker.ClientResponseInterceptor;

/**
 * @author Xs.
 */
public class BraveDubboClientResponseInterceptor implements ClientResponseInterceptor {

    private final com.github.kristofa.brave.ClientResponseInterceptor clientResponseInterceptor;

    public BraveDubboClientResponseInterceptor(com.github.kristofa.brave.ClientResponseInterceptor clientResponseInterceptor) {
        this.clientResponseInterceptor = clientResponseInterceptor;
    }


    @Override
    public void handle(ClientResponseAdapter clientResponseAdapter) {
        if (clientResponseAdapter.isTraceable()) {
            clientResponseInterceptor.handle((com.github.kristofa.brave.ClientResponseAdapter) clientResponseAdapter);
        }
    }
}
