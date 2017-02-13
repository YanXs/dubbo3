package com.alibaba.dubbo.tracker.zipkin.http;

import com.alibaba.dubbo.tracker.*;

/**
 * @author Xs.
 */
public abstract class FacadeHttpRpcTracker implements RpcTracker {

    @Override
    public void trackClientRequest(ClientRequestAdapter clientRequestAdapter) {
        // NOP
    }

    @Override
    public void trackClientResponse(ClientResponseAdapter clientResponseAdapter) {
        // NOP
    }

    @Override
    public void trackServerRequest(ServerRequestAdapter serverRequestAdapter) {
        // NOP
    }

    @Override
    public void trackServerResponse(ServerResponseAdapter serverResponseAdapter) {
        // NOP
    }
}
