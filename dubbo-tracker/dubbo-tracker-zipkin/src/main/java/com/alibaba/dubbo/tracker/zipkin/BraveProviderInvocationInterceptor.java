package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.RpcRequest;
import com.alibaba.dubbo.tracker.RpcRequestSpanNameProvider;
import com.alibaba.dubbo.tracker.ProviderInvocationInterceptor;

public class BraveProviderInvocationInterceptor implements ProviderInvocationInterceptor {

    private final com.github.kristofa.brave.ServerRequestInterceptor serverRequestInterceptor;

    private final RpcRequestSpanNameProvider spanNameProvider;

    public BraveProviderInvocationInterceptor(com.github.kristofa.brave.ServerRequestInterceptor serverRequestInterceptor,
                                              RpcRequestSpanNameProvider spanNameProvider) {
        this.serverRequestInterceptor = serverRequestInterceptor;
        this.spanNameProvider = spanNameProvider;
    }

    @Override
    public void handle(RpcRequest request) {
        if (!request.isTraceableRequest()) {
            return;
        }
        serverRequestInterceptor.handle(new BraveServerRequestAdapter(request, spanNameProvider));
    }
}
