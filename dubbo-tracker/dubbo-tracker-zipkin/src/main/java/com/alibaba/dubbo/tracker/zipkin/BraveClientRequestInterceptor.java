package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.rpc.RpcInvocation;
import com.alibaba.dubbo.tracker.ClientRequestInterceptor;
import com.alibaba.dubbo.tracker.InvocationSpanNameProvider;
import com.alibaba.dubbo.tracker.RpcRequest;

/**
 * @author Xs
 */
public class BraveClientRequestInterceptor implements ClientRequestInterceptor {

    private final com.github.kristofa.brave.ClientRequestInterceptor clientRequestInterceptor;

    private final InvocationSpanNameProvider spanNameProvider;

    public BraveClientRequestInterceptor(com.github.kristofa.brave.ClientRequestInterceptor clientRequestInterceptor,
                                         InvocationSpanNameProvider spanNameProvider) {
        this.clientRequestInterceptor = clientRequestInterceptor;
        this.spanNameProvider = spanNameProvider;
    }

    @Override
    public void handle(RpcRequest request) {
        clientRequestInterceptor.handle(new BraveClientRequestAdapter(request, spanNameProvider));
    }
}
