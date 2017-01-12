package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.rpc.protocol.dubbo.DecodeableRpcResult;
import com.alibaba.dubbo.tracker.ClientResponseInterceptor;

/**
 * @author Xs
 */
public class BraveClientResponseInterceptor implements ClientResponseInterceptor {

    private final com.github.kristofa.brave.ClientResponseInterceptor clientResponseInterceptor;


    public BraveClientResponseInterceptor(com.github.kristofa.brave.ClientResponseInterceptor clientResponseInterceptor) {
        this.clientResponseInterceptor = clientResponseInterceptor;
    }

    @Override
    public void handle(Object response) {
        if (!(response instanceof DecodeableRpcResult)) {
            return;
        }
        DecodeableRpcResult result = (DecodeableRpcResult) response;
        clientResponseInterceptor.handle(new BraveClientResponseAdapter(new BraveRpcResult(result)));
    }
}
