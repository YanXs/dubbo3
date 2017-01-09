package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.remoting.exchange.Response;
import com.alibaba.dubbo.tracker.ClientResponseInterceptor;

/**
 * @author Xs
 */
public class BraveClientResponseInterceptor implements ClientResponseInterceptor {

    private final com.github.kristofa.brave.ClientRequestInterceptor clientRequestInterceptor;


    public BraveClientResponseInterceptor(com.github.kristofa.brave.ClientRequestInterceptor clientRequestInterceptor) {
        this.clientRequestInterceptor = clientRequestInterceptor;
    }

    @Override
    public void handle(Object response) {
        if (!(response instanceof Response)) {
            return;
        }
        Response res = (Response) response;
    }
}
