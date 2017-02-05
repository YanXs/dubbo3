package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.tracker.*;
import com.alibaba.dubbo.tracker.zipkin.http.BraveHttpRpcTracker;

/**
 * @author Xs
 */
public class BraveRpcTrackerFactory implements RpcTrackerFactory {

    @Override
    public RpcTracker createRpcTracker(URL url) {
        RpcTrackerEngine rpcTrackerEngine = RpcTrackerManager.getRpcTrackerEngine();
        if (rpcTrackerEngine == null) {
            return null;
        }
        RpcTracker rpcTracker = null;
        RpcProtocol rpcProtocol = RpcProtocol.valueOf(url.getProtocol());
        if (rpcProtocol.equals(RpcProtocol.DUBBO)) {
            rpcTracker = new BraveDubboRpcTracker((BraveRpcTrackerEngine) rpcTrackerEngine);
        } else if (rpcProtocol.equals(RpcProtocol.HTTP) || rpcProtocol.equals(RpcProtocol.HESSIAN)) {
            rpcTracker = new BraveHttpRpcTracker(rpcTrackerEngine);
        }
        return rpcTracker;
    }
}
