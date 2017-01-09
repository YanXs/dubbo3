package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.remoting.exchange.Request;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.alibaba.dubbo.tracker.ClientRequestInterceptor;
import com.alibaba.dubbo.tracker.DecodeableRequest;
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
        if (!(request instanceof Request)) {
            return;
        }
        Request req = (Request) request;
        if (req.isEvent()) {
            return;
        }
        if (!(req.getData() instanceof RpcInvocation)) {
            return;
        }
        clientRequestInterceptor.handle(new BraveClientRequestAdapter(new DecodeableRequest(req), spanNameProvider));
    }
}
