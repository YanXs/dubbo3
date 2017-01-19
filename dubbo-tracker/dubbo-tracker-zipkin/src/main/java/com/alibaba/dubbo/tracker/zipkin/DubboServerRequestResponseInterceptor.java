package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.exchange.Interceptor;
import com.alibaba.dubbo.remoting.exchange.Request;
import com.alibaba.dubbo.remoting.exchange.Response;
import com.alibaba.dubbo.tracker.DubboRequest;
import com.alibaba.dubbo.tracker.DubboRequestSpanNameProvider;
import com.alibaba.dubbo.tracker.DubboResponse;

public class DubboServerRequestResponseInterceptor implements Interceptor {

    private final DubboServerRequestInterceptor serverRequestInterceptor;

    private final DubboServerResponseInterceptor serverResponseInterceptor;

    private final DubboRequestSpanNameProvider spanNameProvider;

    public DubboServerRequestResponseInterceptor(DubboServerRequestInterceptor serverRequestInterceptor,
                                                 DubboServerResponseInterceptor serverResponseInterceptor,
                                                 DubboRequestSpanNameProvider spanNameProvider) {
        this.serverRequestInterceptor = serverRequestInterceptor;
        this.serverResponseInterceptor = serverResponseInterceptor;
        this.spanNameProvider = spanNameProvider;
    }

    @Override
    public Response intercept(Chain chain) throws RemotingException {
        Request request = chain.request();
        serverRequestInterceptor.handle(new DubboServerRequestAdapter(new DubboRequest(request), spanNameProvider));
        Response response = chain.proceed(request);
        serverResponseInterceptor.handle(new DubboServerResponseAdapter(new DubboResponse(response)));
        return response;
    }
}
