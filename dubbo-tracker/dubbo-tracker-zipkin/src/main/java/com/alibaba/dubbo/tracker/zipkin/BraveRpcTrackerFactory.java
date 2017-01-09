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
    public RpcTracker getRpcTracker(URL url) {
        if (braveRpcTracker != null) {
            return braveRpcTracker;
        }
        braveRpcTracker = new BraveRpcTracker(url);
        return braveRpcTracker;
    }
}
