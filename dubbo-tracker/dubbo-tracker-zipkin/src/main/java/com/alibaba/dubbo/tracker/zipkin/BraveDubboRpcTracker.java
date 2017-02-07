package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.*;

/**
 * 追踪dubbo协议同步调用
 *
 * @author Xs
 */
public class BraveDubboRpcTracker implements RpcTracker {

    private final ClientRequestInterceptor clientRequestInterceptor;

    private final ClientResponseInterceptor clientResponseInterceptor;

    private final ServerRequestInterceptor serverRequestInterceptor;

    private final ServerResponseInterceptor serverResponseInterceptor;

    private final BraveRpcTrackerEngine trackerEngine;

    public BraveDubboRpcTracker(BraveRpcTrackerEngine trackerEngine) {
        this.trackerEngine = trackerEngine;
        this.clientRequestInterceptor = new BraveDubboClientRequestInterceptor(trackerEngine.clientRequestInterceptor());
        this.clientResponseInterceptor = new BraveDubboClientResponseInterceptor(
                trackerEngine.clientResponseInterceptor());
        this.serverRequestInterceptor = new BraveDubboServerRequestInterceptor(
                trackerEngine.serverRequestInterceptor());
        this.serverResponseInterceptor = new BraveDubboServerResponseInterceptor(
                trackerEngine.serverResponseInterceptor());
    }

    @Override
    public void trackClientRequest(ClientRequestAdapter clientRequestAdapter) {
        clientRequestInterceptor.handle(clientRequestAdapter);
    }

    @Override
    public void trackClientResponse(ClientResponseAdapter clientResponseAdapter) {
        clientResponseInterceptor.handle(clientResponseAdapter);
    }

    @Override
    public void trackServerRequest(ServerRequestAdapter serverRequestAdapter) {
        serverRequestInterceptor.handle(serverRequestAdapter);
    }

    @Override
    public void trackServerResponse(ServerResponseAdapter serverResponseAdapter) {
        serverResponseInterceptor.handle(serverResponseAdapter);
    }

    @Override
    public RpcTrackerEngine trackerEngine() {
        return trackerEngine;
    }

}
