package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;

/**
 * @author Xs
 */
public class TrackerManager {

    private static RpcTrackerFactory rpcTrackerFactory;

    private static RpcTracker rpcTracker;

    static {
        rpcTrackerFactory = ExtensionLoader.getExtensionLoader(RpcTrackerFactory.class).getAdaptiveExtension();
    }

    public static RpcTrackerFactory getTrackerFactory() {
        return rpcTrackerFactory;
    }

    public static synchronized RpcTracker create(URL url) {
        if (rpcTracker == null) {
            rpcTracker = rpcTrackerFactory.createRpcTracker(url);
        }
        return rpcTracker;
    }

    public static RpcTracker getRpcTracker() {
        if (rpcTracker == null) {
            throw new IllegalStateException("rpcTracker should be create first!");
        }
        return rpcTracker;
    }

    public static RpcTracker mockRpcTracker(String address, String application) {
        String urlStr = "zipkin://" + address + "?application=" + application + "&collector=http";
        URL url = URL.valueOf(urlStr);
        return create(url);
    }
}
