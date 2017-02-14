package com.alibaba.dubbo.tracker.zipkin.http;

import com.alibaba.dubbo.tracker.RpcTracker;
import com.alibaba.dubbo.tracker.RpcTrackerEngine;
import com.github.kristofa.brave.ClientRequestInterceptor;
import com.github.kristofa.brave.ClientResponseInterceptor;
import com.github.kristofa.brave.http.SpanNameProvider;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author Xs.
 */
public class BraveOkHttpRequestResponseInterceptor implements Interceptor {

    private Interceptor delegate;

    public BraveOkHttpRequestResponseInterceptor(RpcTracker rpcTracker, SpanNameProvider spanNameProvider) {
        RpcTrackerEngine trackerEngine = rpcTracker.trackerEngine();
        delegate = new com.github.kristofa.brave.okhttp.BraveOkHttpRequestResponseInterceptor((ClientRequestInterceptor) trackerEngine.clientRequestInterceptor(),
                (ClientResponseInterceptor) trackerEngine.clientResponseInterceptor(), spanNameProvider);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return delegate.intercept(chain);
    }
}
