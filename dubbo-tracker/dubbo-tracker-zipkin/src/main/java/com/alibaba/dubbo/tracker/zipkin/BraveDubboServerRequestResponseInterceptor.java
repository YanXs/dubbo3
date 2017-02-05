package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.remoting.exception.RemotingException;
import com.alibaba.dubbo.remoting.message.Interceptor;
import com.alibaba.dubbo.remoting.message.Request;
import com.alibaba.dubbo.remoting.message.Response;
import com.alibaba.dubbo.tracker.DubboRequest;
import com.alibaba.dubbo.tracker.DubboRequestSpanNameProvider;
import com.alibaba.dubbo.tracker.DubboResponse;
import com.alibaba.dubbo.tracker.RpcTracker;

public class BraveDubboServerRequestResponseInterceptor implements Interceptor {

    private final RpcTracker rpcTracker;

    private final DubboRequestSpanNameProvider spanNameProvider;

    public BraveDubboServerRequestResponseInterceptor(RpcTracker rpcTracker, DubboRequestSpanNameProvider spanNameProvider) {
        this.rpcTracker = rpcTracker;
        this.spanNameProvider = spanNameProvider;
    }

    @Override
    public Response intercept(Chain chain) throws RemotingException {
        Request request = chain.request();
        rpcTracker.trackServerRequest(new BraveDubboServerRequestAdapter(new DubboRequest(request), spanNameProvider));
        Response response = chain.proceed(request);
        rpcTracker.trackServerResponse(new BraveDubboServerResponseAdapter(new DubboResponse(response)));
        return response;
    }
}
