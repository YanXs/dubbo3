package com.alibaba.dubbo.tracker.zipkin.http;

import com.alibaba.dubbo.tracker.RpcTrackerEngine;
import com.alibaba.dubbo.tracker.zipkin.BraveRpcTrackerEngine;

/**
 * @author Xs
 */
public class BraveHttpRpcTracker extends FacadeHttpRpcTracker {

    private final RpcTrackerEngine trackerEngine;

    public BraveHttpRpcTracker(RpcTrackerEngine trackerEngine) {
        this.trackerEngine = trackerEngine;
    }

    @Override
    public RpcTrackerEngine trackerEngine() {
        return trackerEngine;
    }
}
