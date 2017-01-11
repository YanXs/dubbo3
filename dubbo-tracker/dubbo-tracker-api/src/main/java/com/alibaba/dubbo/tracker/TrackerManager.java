package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.common.extension.ExtensionLoader;

public class TrackerManager {

    public static RpcTrackerFactory getTrackerFactory() {
        return ExtensionLoader.getExtensionLoader(RpcTrackerFactory.class).getDefaultExtension();
    }
}
