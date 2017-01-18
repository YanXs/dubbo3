package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.exchange.Interceptor;
import com.alibaba.dubbo.remoting.exchange.Request;
import com.alibaba.dubbo.remoting.exchange.Response;
import com.alibaba.dubbo.tracker.DubboRequest;
import com.alibaba.dubbo.tracker.DubboRequestSpanNameProvider;
import com.alibaba.dubbo.tracker.DubboResponse;

public class DubboRequestResponseInterceptor implements Interceptor {

    private final DubboClientRequestInterceptor clientRequestInterceptor;
    private final DubboClientResponseInterceptor clientResponseInterceptor;
    private final DubboRequestSpanNameProvider spanNameProvider;

    public DubboRequestResponseInterceptor(DubboClientRequestInterceptor clientRequestInterceptor,
                                           DubboClientResponseInterceptor clientResponseInterceptor,
                                           DubboRequestSpanNameProvider spanNameProvider) {
        this.clientRequestInterceptor = clientRequestInterceptor;
        this.clientResponseInterceptor = clientResponseInterceptor;
        this.spanNameProvider = spanNameProvider;
    }

    @Override
    public Response intercept(Chain chain) throws RemotingException {
        Request request = chain.request();
        clientRequestInterceptor.handle(new DubboClientRequestAdapter(new DubboRequest(request), spanNameProvider));
        Response response = chain.proceed(request, chain.timeout());
        clientResponseInterceptor.handle(new DubboClientResponseAdapter(new DubboResponse(response)));
        return response;
    }
}
