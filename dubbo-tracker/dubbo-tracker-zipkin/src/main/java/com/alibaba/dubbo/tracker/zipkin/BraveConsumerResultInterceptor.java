package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.ConsumerResultInterceptor;
import com.alibaba.dubbo.tracker.RpcResponse;
import com.github.kristofa.brave.ClientResponseInterceptor;

/**
 * @author Xs
 */
public class BraveConsumerResultInterceptor implements ConsumerResultInterceptor {

    private final ClientResponseInterceptor clientResponseInterceptor;


    public BraveConsumerResultInterceptor(com.github.kristofa.brave.ClientResponseInterceptor clientResponseInterceptor) {
        this.clientResponseInterceptor = clientResponseInterceptor;
    }

    @Override
    public void handle(RpcResponse response) {
        if (!response.isTraceableResponse()) {
            return;
        }
        clientResponseInterceptor.handle(new BraveClientResponseAdapter(response));
    }
}
