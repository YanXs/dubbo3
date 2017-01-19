package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.tracker.RpcTrackerEngine;
import com.github.kristofa.brave.*;

/**
 * 基于Zipkin的RpcTracker引擎
 *
 * @author Xs
 */
public class BraveRpcTrackerEngine implements RpcTrackerEngine {

    private final Brave brave;

    BraveRpcTrackerEngine(URL url) {
        Brave.Builder builder = new Brave.Builder(url.getParameter("application", "demo"));
        builder.spanCollector(SpanCollectorFactory.create(url));
        builder.traceSampler(SamplerFactory.create(url));
        brave = builder.build();
    }

    public static BraveRpcTrackerEngine create(URL url) {
        return new BraveRpcTrackerEngine(url);
    }

    @Override
    public ClientRequestInterceptor clientRequestInterceptor() {
        return brave.clientRequestInterceptor();
    }

    @Override
    public ClientResponseInterceptor clientResponseInterceptor() {
        return brave.clientResponseInterceptor();
    }

    @Override
    public ServerRequestInterceptor serverRequestInterceptor() {
        return brave.serverRequestInterceptor();
    }

    @Override
    public ServerResponseInterceptor serverResponseInterceptor() {
        return brave.serverResponseInterceptor();
    }
}
