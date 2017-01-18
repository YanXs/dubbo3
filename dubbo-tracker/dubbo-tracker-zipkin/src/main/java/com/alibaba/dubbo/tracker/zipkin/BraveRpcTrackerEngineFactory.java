package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.tracker.RpcTrackerEngine;
import com.alibaba.dubbo.tracker.RpcTrackerEngineFactory;

/**
 * @author Xs
 */
public class BraveRpcTrackerEngineFactory implements RpcTrackerEngineFactory {

    private volatile BraveRpcTrackerEngine braveRpcTrackerEngine;

    @Override
    public RpcTrackerEngine createRpcTrackerEngine(URL url) {
        if (braveRpcTrackerEngine == null) {
            braveRpcTrackerEngine = BraveRpcTrackerEngine.create(url);
        }
        return braveRpcTrackerEngine;
    }

    @Override
    public RpcTrackerEngine getTrackerEngine() {
        return braveRpcTrackerEngine;
    }
}
