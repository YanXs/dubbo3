package com.alibaba.dubbo.tracker.zipkin;


import com.alibaba.dubbo.tracker.RpcResponse;
import com.alibaba.dubbo.tracker.ServerResponseInterceptor;

public class BraveServerResponseInterceptor implements ServerResponseInterceptor {

    private final com.github.kristofa.brave.ServerResponseInterceptor serverResponseInterceptor;

    public BraveServerResponseInterceptor(com.github.kristofa.brave.ServerResponseInterceptor serverResponseInterceptor) {
        this.serverResponseInterceptor = serverResponseInterceptor;
    }

    @Override
    public void handle(RpcResponse response) {
        serverResponseInterceptor.handle(new BraveServerResponseAdapter(response));
    }
}
