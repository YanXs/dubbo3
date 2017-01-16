package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.ConsumerInvocationInterceptor;
import com.alibaba.dubbo.tracker.RpcRequest;
import com.alibaba.dubbo.tracker.RpcRequestSpanNameProvider;
import com.github.kristofa.brave.ClientRequestInterceptor;

/**
 * @author Xs
 */
public class BraveConsumerInvocationInterceptor implements ConsumerInvocationInterceptor {

    private final ClientRequestInterceptor clientRequestInterceptor;

    private final RpcRequestSpanNameProvider spanNameProvider;

    public BraveConsumerInvocationInterceptor(com.github.kristofa.brave.ClientRequestInterceptor clientRequestInterceptor,
                                              RpcRequestSpanNameProvider spanNameProvider) {
        this.clientRequestInterceptor = clientRequestInterceptor;
        this.spanNameProvider = spanNameProvider;
    }

    @Override
    public void handle(RpcRequest request) {
        if (!request.isTraceableRequest()) {
            return;
        }
        clientRequestInterceptor.handle(new BraveClientRequestAdapter(request, spanNameProvider));
    }
}
