package com.alibaba.dubbo.tracker.zipkin;


import com.alibaba.dubbo.tracker.RpcResponse;
import com.alibaba.dubbo.tracker.ProviderResultInterceptor;

public class BraveProviderResultInterceptor implements ProviderResultInterceptor {

    private final com.github.kristofa.brave.ServerResponseInterceptor serverResponseInterceptor;

    public BraveProviderResultInterceptor(com.github.kristofa.brave.ServerResponseInterceptor serverResponseInterceptor) {
        this.serverResponseInterceptor = serverResponseInterceptor;
    }

    @Override
    public void handle(RpcResponse response) {
        if (!response.isTraceableResponse()) {
            return;
        }
        serverResponseInterceptor.handle(new BraveServerResponseAdapter(response));
    }
}
