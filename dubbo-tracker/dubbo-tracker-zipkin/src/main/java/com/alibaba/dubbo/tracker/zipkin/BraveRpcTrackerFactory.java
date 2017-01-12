package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.tracker.RpcTracker;
import com.alibaba.dubbo.tracker.RpcTrackerFactory;

/**
 * @author Xs
 */
public class BraveRpcTrackerFactory implements RpcTrackerFactory {

    private volatile BraveRpcTracker braveRpcTracker;

    @Override
    public RpcTracker createRpcTracker(URL url) {
        if (braveRpcTracker == null) {
            braveRpcTracker = BraveRpcTracker.create(url);
        }
        return braveRpcTracker;
    }

    @Override
    public RpcTracker getTracker() {
        if (braveRpcTracker == null) {
            throw new IllegalStateException("braveRpcTracker must not be null");
        }
        return braveRpcTracker;
    }
}
