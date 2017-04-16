package com.alibaba.dubbo.tracker;

/**
 * @author Xs
 */
public interface RpcTracker {

    void trackClientRequest(ClientRequestAdapter clientRequestAdapter);

    void trackClientResponse(ClientResponseAdapter clientResponseAdapter);

    void trackServerRequest(ServerRequestAdapter serverRequestAdapter);

    void trackServerResponse(ServerResponseAdapter serverResponseAdapter);

    RpcTrackerEngine trackerEngine();
}
