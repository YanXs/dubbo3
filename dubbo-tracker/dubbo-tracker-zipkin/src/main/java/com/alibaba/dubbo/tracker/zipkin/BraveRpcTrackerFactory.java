package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.tracker.RpcTracker;
import com.alibaba.dubbo.tracker.RpcTrackerFactory;
import com.alibaba.dubbo.tracker.TrackerManager;
import com.alibaba.dubbo.tracker.zipkin.http.HttpRpcTracker;

/**
 * @author Xs
 */
public class BraveRpcTrackerFactory implements RpcTrackerFactory {

    @Override
    public RpcTracker createRpcTracker(URL url) {
        String protocol = url.getProtocol();
        RpcTracker rpcTracker = null;
        if (protocol.equals("dubbo")) {
            rpcTracker = new DubboRpcTracker((BraveRpcTrackerEngine) TrackerManager.getRpcTrackerEngine());
        } else if (protocol.equals("http") || protocol.equals("hessian")) {
            rpcTracker = new HttpRpcTracker();
        }
        return rpcTracker;
    }
}
