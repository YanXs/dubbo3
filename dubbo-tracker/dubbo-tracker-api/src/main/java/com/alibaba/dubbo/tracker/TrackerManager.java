package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xs
 */
public class TrackerManager {

    private static RpcTrackerFactory rpcTrackerFactory;

    private static RpcTrackerEngineFactory rpcTrackerEngineFactory;

    private static RpcTrackerEngine rpcTrackerEngine;

    private static Map<String, RpcTracker> rpcTrackerMap = new HashMap<String, RpcTracker>();

    static {
        rpcTrackerEngineFactory = ExtensionLoader.getExtensionLoader(RpcTrackerEngineFactory.class).getDefaultExtension();
        rpcTrackerFactory = ExtensionLoader.getExtensionLoader(RpcTrackerFactory.class).getDefaultExtension();
    }

    public static RpcTrackerFactory getTrackerFactory() {
        return rpcTrackerFactory;
    }

    public static RpcTrackerEngineFactory getRpcTrackerEngineFactory() {
        return rpcTrackerEngineFactory;
    }

    public static RpcTrackerEngine getRpcTrackerEngine() {
        if (rpcTrackerEngine == null) {
            throw new IllegalStateException("rpcTrackerEngine should create first");
        }
        return rpcTrackerEngine;
    }

    public static synchronized RpcTrackerEngine createRpcTrackerEngine(URL url) {
        if (rpcTrackerEngine == null) {
            rpcTrackerEngine = rpcTrackerEngineFactory.createRpcTrackerEngine(url);
        }
        return rpcTrackerEngine;
    }

    public static synchronized RpcTracker createRpcTracker(URL url) {
        String protocol = url.getProtocol();
        RpcTracker rpcTracker = rpcTrackerMap.get(protocol);
        if (rpcTracker == null) {
            rpcTracker = rpcTrackerFactory.createRpcTracker(url);
            if (rpcTracker == null) {
                throw new IllegalStateException("rpcTracker should not be null here, url: " + url);
            }
            rpcTrackerMap.put(protocol, rpcTracker);
        }
        return rpcTracker;
    }

    public static RpcTracker getRpcTracker(String protocol) {
        RpcTracker rpcTracker = rpcTrackerMap.get(protocol);
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
