package com.alibaba.dubbo.tracker.zipkin;


import com.alibaba.dubbo.remoting.exception.RemotingException;
import com.alibaba.dubbo.remoting.message.Interceptor;
import com.alibaba.dubbo.remoting.message.Request;
import com.alibaba.dubbo.remoting.message.Response;
import com.alibaba.dubbo.tracker.DubboRequest;
import com.alibaba.dubbo.tracker.DubboRequestSpanNameProvider;
import com.alibaba.dubbo.tracker.DubboResponse;
import com.alibaba.dubbo.tracker.RpcTracker;

/**
 * dubbo协议client request拦截器
 *
 * @author Xs
 */
public class BraveDubboClientRequestResponseInterceptor implements Interceptor {

    private final RpcTracker rpcTracker;

    private final DubboRequestSpanNameProvider spanNameProvider;

    public BraveDubboClientRequestResponseInterceptor(RpcTracker rpcTracker, DubboRequestSpanNameProvider spanNameProvider) {
        this.rpcTracker = rpcTracker;
        this.spanNameProvider = spanNameProvider;
    }

    @Override
    public Response intercept(Chain chain) throws RemotingException {
        Request request = chain.request();
        rpcTracker.trackClientRequest(new BraveDubboClientRequestAdapter(new DubboRequest(request), spanNameProvider));
        Response response = chain.proceed(request, chain.timeout());
        rpcTracker.trackClientResponse(new BraveDubboClientResponseAdapter(new DubboResponse(response)));
        return response;
    }
}
