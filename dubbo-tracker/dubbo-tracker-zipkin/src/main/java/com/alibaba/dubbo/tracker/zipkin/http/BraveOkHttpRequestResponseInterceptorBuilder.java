package com.alibaba.dubbo.tracker.zipkin.http;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.tracker.RpcTracker;
import com.alibaba.dubbo.tracker.RpcTrackerManager;
import com.alibaba.dubbo.tracker.http.HttpRequestResponseInterceptorBuilder;
import okhttp3.Interceptor;

/**
 * @author Xs.
 */
public class BraveOkHttpRequestResponseInterceptorBuilder implements HttpRequestResponseInterceptorBuilder {

    @Override
    public Interceptor build(URL url) {
        RpcTracker rpcTracker = RpcTrackerManager.getRpcTracker(url);
        if (rpcTracker == null) {
            return null;
        }
        return new BraveOkHttpRequestResponseInterceptor(rpcTracker, HttpSpanNameProvider.getInstance());
    }
}
