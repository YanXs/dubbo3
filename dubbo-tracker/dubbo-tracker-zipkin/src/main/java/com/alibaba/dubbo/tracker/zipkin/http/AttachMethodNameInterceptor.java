package com.alibaba.dubbo.tracker.zipkin.http;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.tracker.RpcAttachment;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author Xs.
 */
public class AttachMethodNameInterceptor implements Interceptor {

    private static final AttachMethodNameInterceptor instance = new AttachMethodNameInterceptor();

    private AttachMethodNameInterceptor() {
    }

    public static AttachMethodNameInterceptor getInstance() {
        return instance;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder.addHeader(RpcAttachment.SpanName.getName(), RpcContext.getContext().getMethodName());
        return chain.proceed(builder.build());
    }
}
