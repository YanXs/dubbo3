package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.tracker.RpcTrackerEngine;
import com.alibaba.dubbo.tracker.RpcTrackerEngineFactory;

/**
 * @author Xs
 */
public class BraveRpcTrackerEngineFactory implements RpcTrackerEngineFactory {

    @Override
    public RpcTrackerEngine createRpcTrackerEngine(URL url) {
        return BraveRpcTrackerEngine.create(url);
    }
}
