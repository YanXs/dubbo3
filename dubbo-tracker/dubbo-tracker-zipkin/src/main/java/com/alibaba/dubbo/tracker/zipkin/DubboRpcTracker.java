package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.*;

/**
 * 追踪dubbo协议同步调用
 *
 * @author Xs
 */
public class DubboRpcTracker implements RpcTracker {

    private final ClientRequestInterceptor clientRequestInterceptor;

    private final ClientResponseInterceptor clientResponseInterceptor;

    private final ServerRequestInterceptor serverRequestInterceptor;

    private final ServerResponseInterceptor serverResponseInterceptor;

    private final BraveRpcTrackerEngine trackerEngine;

    public DubboRpcTracker(BraveRpcTrackerEngine trackerEngine) {
        this.trackerEngine = trackerEngine;
        this.clientRequestInterceptor = new BraveDubboClientRequestInterceptor(
                trackerEngine.clientRequestInterceptor());
        this.clientResponseInterceptor = new BraveDubboClientResponseInterceptor(
                trackerEngine.clientResponseInterceptor());
        this.serverRequestInterceptor = new BraveDubboServerRequestInterceptor(
                trackerEngine.serverRequestInterceptor());
        this.serverResponseInterceptor = new BraveDubboServerResponseInterceptor(
                trackerEngine.serverResponseInterceptor());
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
