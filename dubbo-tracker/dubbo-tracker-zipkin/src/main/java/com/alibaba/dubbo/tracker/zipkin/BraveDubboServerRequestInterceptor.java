package com.alibaba.dubbo.tracker.zipkin;


import com.alibaba.dubbo.tracker.ServerRequestAdapter;
import com.alibaba.dubbo.tracker.ServerRequestInterceptor;

/**
 * @author Xs
 */
public class BraveDubboServerRequestInterceptor implements ServerRequestInterceptor {

    private final com.github.kristofa.brave.ServerRequestInterceptor serverRequestInterceptor;

    public BraveDubboServerRequestInterceptor(com.github.kristofa.brave.ServerRequestInterceptor serverRequestInterceptor) {
        this.serverRequestInterceptor = serverRequestInterceptor;
    }

    @Override
    public void handle(ServerRequestAdapter serverRequestAdapter) {
        if (serverRequestAdapter.isTraceable()) {
            serverRequestInterceptor.handle((com.github.kristofa.brave.ServerRequestAdapter) serverRequestAdapter);
        }
    }

}
