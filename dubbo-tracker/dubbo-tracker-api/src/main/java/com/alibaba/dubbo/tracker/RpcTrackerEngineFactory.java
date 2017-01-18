package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;

/**
 * @author Xs
 */
@SPI("zipkin")
public interface RpcTrackerEngineFactory {

    @Adaptive("protocol")
    RpcTrackerEngine createRpcTrackerEngine(URL url);

    RpcTrackerEngine getTrackerEngine();
}
