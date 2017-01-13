package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.rpc.RpcInvocation;
import com.alibaba.dubbo.tracker.InvocationSpanNameProvider;
import com.alibaba.dubbo.tracker.RpcRequest;
import com.alibaba.dubbo.tracker.ServerRequestInterceptor;

public class BraveServerRequestInterceptor implements ServerRequestInterceptor {

    private final com.github.kristofa.brave.ServerRequestInterceptor serverRequestInterceptor;

    private final InvocationSpanNameProvider spanNameProvider;

    public BraveServerRequestInterceptor(com.github.kristofa.brave.ServerRequestInterceptor serverRequestInterceptor,
                                         InvocationSpanNameProvider spanNameProvider) {
        this.serverRequestInterceptor = serverRequestInterceptor;
        this.spanNameProvider = spanNameProvider;
    }

    @Override
    public void handle(RpcRequest request) {
        serverRequestInterceptor.handle(new BraveServerRequestAdapter(request,spanNameProvider));
    }
}
