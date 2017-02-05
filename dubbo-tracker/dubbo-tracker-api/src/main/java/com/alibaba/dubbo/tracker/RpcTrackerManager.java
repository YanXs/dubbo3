package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.common.URL;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xs
 */
public final class RpcTrackerManager {

    private static RpcTrackerFactory rpcTrackerFactory;

    private static RpcTrackerEngine rpcTrackerEngine;

    private static Map<RpcProtocol, RpcTracker> rpcTrackerMap = new HashMap<RpcProtocol, RpcTracker>();

    public static RpcTrackerFactory getTrackerFactory() {
        return rpcTrackerFactory;
    }

    public static synchronized void setRpcTrackerFactory(RpcTrackerFactory rpcTrackerFactory) {
        RpcTrackerManager.rpcTrackerFactory = rpcTrackerFactory;
    }

    public static synchronized RpcTrackerEngine getRpcTrackerEngine() {
        return rpcTrackerEngine;
    }

    public static synchronized RpcTrackerEngine createRpcTrackerEngine(RpcTrackerEngineFactory factory, URL url) {
        if (rpcTrackerEngine == null) {
            rpcTrackerEngine = factory.createRpcTrackerEngine(url);
        }
        return rpcTrackerEngine;
    }

    public static synchronized RpcTracker createRpcTracker(URL url) {
        String protocol = url.getProtocol();
        if (rpcTrackerEngine == null || rpcTrackerFactory == null) {
            return null;
        }
        RpcTracker rpcTracker = rpcTrackerMap.get(RpcProtocol.valueOf(protocol));
        if (rpcTracker == null) {
            rpcTracker = rpcTrackerFactory.createRpcTracker(url);
            if (rpcTracker == null) {
                throw new IllegalStateException("rpcTracker should not be null here, url: " + url);
            }
            rpcTrackerMap.put(RpcProtocol.valueOf(protocol), rpcTracker);
        }
        return rpcTracker;
    }

    public static RpcTracker getRpcTracker(String protocol) {
        RpcTracker rpcTracker = rpcTrackerMap.get(RpcProtocol.valueOf(protocol));
        if (rpcTracker == null) {
            throw new IllegalStateException("rpcTracker should be create first!");
        }
        return rpcTracker;
    }

    public static RpcTracker mockRpcTracker(String address, String application) {
        String urlStr = "zipkin://" + address + "?application=" + application + "&collector=http";
        URL url = URL.valueOf(urlStr);
        return createRpcTracker(url);
    }
}
