package com.alibaba.dubbo.tracker.zipkin;


import com.alibaba.dubbo.remoting.exception.RemotingException;
import com.alibaba.dubbo.remoting.message.Interceptor;
import com.alibaba.dubbo.remoting.message.Request;
import com.alibaba.dubbo.remoting.message.Response;
import com.alibaba.dubbo.tracker.DubboRequest;
import com.alibaba.dubbo.tracker.DubboRequestSpanNameProvider;
import com.alibaba.dubbo.tracker.DubboResponse;

public class DubboClientRequestResponseInterceptor implements Interceptor {

    private final DubboClientRequestInterceptor clientRequestInterceptor;
    private final DubboClientResponseInterceptor clientResponseInterceptor;
    private final DubboRequestSpanNameProvider spanNameProvider;

    public DubboClientRequestResponseInterceptor(DubboClientRequestInterceptor clientRequestInterceptor,
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
