package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.ClientRequestInterceptor;
import com.alibaba.dubbo.tracker.ClientResponseInterceptor;
import com.alibaba.dubbo.tracker.RpcTracker;
import com.alibaba.dubbo.tracker.RpcTrackerEngine;
import com.alibaba.dubbo.tracker.filter.ServletFilter;

public class DubboRpcTracker implements RpcTracker {

    @Override
    public ClientRequestInterceptor clientRequestInterceptor() {
        return null;
    }

    @Override
    public ClientResponseInterceptor clientResponseInterceptor() {
        return null;
    }

    @Override
    public ServletFilter servletFilter() {
        return null;
    }

    @Override
    public RpcTrackerEngine trackerEngine() {
        return null;
    }
}
