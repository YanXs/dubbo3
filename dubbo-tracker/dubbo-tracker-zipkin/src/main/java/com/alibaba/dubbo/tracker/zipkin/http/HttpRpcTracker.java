package com.alibaba.dubbo.tracker.zipkin.http;

import com.alibaba.dubbo.tracker.RpcTrackerEngine;
import com.alibaba.dubbo.tracker.zipkin.BraveRpcTrackerEngine;

/**
 * @author Xs
 */
public class HttpRpcTracker extends FacadeHttpRpcTracker {

    private final BraveRpcTrackerEngine trackerEngine;

    public HttpRpcTracker(BraveRpcTrackerEngine trackerEngine) {
        this.trackerEngine = trackerEngine;
    }

    @Override
    public RpcTrackerEngine trackerEngine() {
        return trackerEngine;
    }
}
