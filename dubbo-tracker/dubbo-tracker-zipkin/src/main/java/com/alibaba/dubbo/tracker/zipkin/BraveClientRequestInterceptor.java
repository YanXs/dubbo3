package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.rpc.RpcInvocation;
import com.alibaba.dubbo.tracker.ClientRequestInterceptor;
import com.alibaba.dubbo.tracker.DubboSpanNameProvider;

/**
 * @author Xs
 */
public class BraveClientRequestInterceptor implements ClientRequestInterceptor {

    private final com.github.kristofa.brave.ClientRequestInterceptor clientRequestInterceptor;

    private final DubboSpanNameProvider spanNameProvider;

    public BraveClientRequestInterceptor(com.github.kristofa.brave.ClientRequestInterceptor clientRequestInterceptor,
                                         DubboSpanNameProvider spanNameProvider) {
        this.clientRequestInterceptor = clientRequestInterceptor;
        this.spanNameProvider = spanNameProvider;
    }

    @Override
    public void handle(Object request) {
        if (!(request instanceof RpcInvocation)) {
            return;
        }
        RpcInvocation invocation = (RpcInvocation) request;
        clientRequestInterceptor.handle(new BraveClientRequestAdapter(new BraveRpcInvocation(invocation), spanNameProvider));
    }
}
