package com.alibaba.dubbo.tracker.zipkin.http;

import com.alibaba.dubbo.tracker.*;

public class HttpRpcTracker implements RpcTracker {

    @Override
    public ClientRequestInterceptor clientRequestInterceptor() {
        return null;
    }

    @Override
    public ClientResponseInterceptor clientResponseInterceptor() {
        return null;
    }

    @Override
    public ServerRequestInterceptor serverRequestInterceptor() {
        return null;
    }

    @Override
    public ServerResponseInterceptor serverResponseInterceptor() {
        return null;
    }

    @Override
    public RpcTrackerEngine trackerEngine() {
        return null;
    }
}
