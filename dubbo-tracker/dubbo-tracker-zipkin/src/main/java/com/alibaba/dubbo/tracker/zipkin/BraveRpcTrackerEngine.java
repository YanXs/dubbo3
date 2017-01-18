package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.tracker.RpcTrackerEngine;
import com.github.kristofa.brave.Brave;

public class BraveRpcTrackerEngine implements RpcTrackerEngine {

    private final Brave brave;

    public BraveRpcTrackerEngine(URL url) {
        Brave.Builder builder = new Brave.Builder(url.getParameter("application", "demo"));
        builder.spanCollector(SpanCollectorFactory.create(url));
        builder.traceSampler(SamplerFactory.create(url));
        brave = builder.build();
    }

    @Override
    public Object clientRequestInterceptor() {
        return brave.clientRequestInterceptor();
    }

    @Override
    public Object clientResponseInterceptor() {
        return brave.clientResponseInterceptor();
    }

    @Override
    public Object serverRequestInterceptor() {
        return brave.serverRequestInterceptor();
    }

    @Override
    public Object serverResponseInterceptor() {
        return brave.serverResponseInterceptor();
    }
}
