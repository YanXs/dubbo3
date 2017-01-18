package com.alibaba.dubbo.tracker;

import com.sun.javafx.scene.traversal.TraversalEngine;

/**
 * @author Xs
 */
public interface RpcTracker {

    ClientRequestInterceptor clientRequestInterceptor();

    ClientResponseInterceptor clientResponseInterceptor();

    ServerRequestInterceptor serverRequestInterceptor();

    ServerResponseInterceptor serverResponseInterceptor();

    RpcTrackerEngine trackerEngine();
}
