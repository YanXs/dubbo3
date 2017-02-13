package com.alibaba.dubbo.tracker.zipkin.dubbo;


import com.alibaba.dubbo.tracker.ServerResponseAdapter;
import com.alibaba.dubbo.tracker.ServerResponseInterceptor;

/**
 * @author Xs
 */
public class BraveDubboServerResponseInterceptor implements ServerResponseInterceptor {

    private final com.github.kristofa.brave.ServerResponseInterceptor serverResponseInterceptor;

    public BraveDubboServerResponseInterceptor(com.github.kristofa.brave.ServerResponseInterceptor serverResponseInterceptor) {
        this.serverResponseInterceptor = serverResponseInterceptor;
    }

    @Override
    public void handle(ServerResponseAdapter serverResponseAdapter) {
        if (serverResponseAdapter.isTraceable()) {
            serverResponseInterceptor.handle((com.github.kristofa.brave.ServerResponseAdapter) serverResponseAdapter);
        }
    }

}
