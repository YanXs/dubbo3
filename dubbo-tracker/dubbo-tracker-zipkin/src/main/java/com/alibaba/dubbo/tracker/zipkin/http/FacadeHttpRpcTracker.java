package com.alibaba.dubbo.tracker.zipkin.http;

import com.alibaba.dubbo.tracker.*;

public class FacadeHttpRpcTracker implements RpcTracker {

    @Override
    public void trackClientRequest(ClientRequestAdapter clientRequestAdapter) {

    }

    @Override
    public void trackClientResponse(ClientResponseAdapter clientResponseAdapter) {

    }

    @Override
    public void trackServerRequest(ServerRequestAdapter serverRequestAdapter) {

    }

    @Override
    public void trackServerResponse(ServerResponseAdapter serverResponseAdapter) {

    }

    @Override
    public RpcTrackerEngine trackerEngine() {
        return null;
    }
}
