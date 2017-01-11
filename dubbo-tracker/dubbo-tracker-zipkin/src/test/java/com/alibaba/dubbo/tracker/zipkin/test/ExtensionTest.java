package com.alibaba.dubbo.tracker.zipkin.test;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.tracker.RpcTracker;
import com.alibaba.dubbo.tracker.RpcTrackerFactory;
import com.alibaba.dubbo.tracker.TrackerManager;
import org.junit.Assert;
import org.junit.Test;

public class ExtensionTest {

    @Test
    public void test_rpc_factory() {
        RpcTrackerFactory factory = TrackerManager.getTrackerFactory();
        RpcTrackerFactory factory1 = TrackerManager.getTrackerFactory();
        Assert.assertEquals(factory, factory1);
    }

    @Test
    public void test_create_tracker() {
        RpcTrackerFactory factory = ExtensionLoader.getExtensionLoader(RpcTrackerFactory.class).getDefaultExtension();
        URL url = URL.valueOf("zipkin://localhost:9411?service-name=test&collector=http&sampler=counting&rate=0.2");
        RpcTracker rpcTracker = factory.createRpcTracker(url);
    }
}
