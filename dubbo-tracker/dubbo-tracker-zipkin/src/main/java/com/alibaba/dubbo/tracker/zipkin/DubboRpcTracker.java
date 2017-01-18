package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.*;

public class DubboRpcTracker implements RpcTracker {

    private final ClientRequestInterceptor clientRequestInterceptor;

    private final ClientResponseInterceptor clientResponseInterceptor;

    private final ServerRequestInterceptor serverRequestInterceptor;

    private final ServerResponseInterceptor serverResponseInterceptor;

    private final BraveRpcTrackerEngine trackerEngine;

    public DubboRpcTracker(BraveRpcTrackerEngine trackerEngine) {
        this.trackerEngine = trackerEngine;
        this.clientRequestInterceptor = new BraveDubboClientRequestInterceptor(
                (com.github.kristofa.brave.ClientRequestInterceptor) trackerEngine.clientRequestInterceptor());
        this.clientResponseInterceptor = new BraveDubboClientResponseInterceptor(
                (com.github.kristofa.brave.ClientResponseInterceptor) trackerEngine.clientResponseInterceptor());
        this.serverRequestInterceptor = new BraveDubboServerRequestInterceptor(
                (com.github.kristofa.brave.ServerRequestInterceptor) trackerEngine.serverRequestInterceptor());
        this.serverResponseInterceptor = new BraveDubboServerResponseInterceptor(
                (com.github.kristofa.brave.ServerResponseInterceptor) trackerEngine.serverResponseInterceptor());
    }

    @Override
    public ClientRequestInterceptor clientRequestInterceptor() {
        return clientRequestInterceptor;
    }

    @Override
    public ClientResponseInterceptor clientResponseInterceptor() {
        return clientResponseInterceptor;
    }

    @Override
    public ServerRequestInterceptor serverRequestInterceptor() {
        return serverRequestInterceptor;
    }

    @Override
    public ServerResponseInterceptor serverResponseInterceptor() {
        return serverResponseInterceptor;
    }

    @Override
    public RpcTrackerEngine trackerEngine() {
        return trackerEngine;
    }
}
