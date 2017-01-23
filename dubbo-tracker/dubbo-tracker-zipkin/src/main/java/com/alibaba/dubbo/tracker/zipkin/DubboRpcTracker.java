package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.*;

/**
 * 追踪dubbo协议同步调用
 *
 * @author Xs
 */
public class DubboRpcTracker implements RpcTracker {

    private final DubboClientRequestInterceptor clientRequestInterceptor;

    private final DubboClientResponseInterceptor clientResponseInterceptor;

    private final DubboServerRequestInterceptor serverRequestInterceptor;

    private final DubboServerResponseInterceptor serverResponseInterceptor;

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
    public DubboClientRequestInterceptor clientRequestInterceptor() {
        return clientRequestInterceptor;
    }

    @Override
    public DubboClientResponseInterceptor clientResponseInterceptor() {
        return clientResponseInterceptor;
    }

    @Override
    public DubboServerRequestInterceptor serverRequestInterceptor() {
        return serverRequestInterceptor;
    }

    @Override
    public DubboServerResponseInterceptor serverResponseInterceptor() {
        return serverResponseInterceptor;
    }

    @Override
    public RpcTrackerEngine trackerEngine() {
        return trackerEngine;
    }
}
