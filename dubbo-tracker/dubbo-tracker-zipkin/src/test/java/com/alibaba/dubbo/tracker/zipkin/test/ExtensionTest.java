package com.alibaba.dubbo.tracker.zipkin.test;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.tracker.RpcTracker;
import com.alibaba.dubbo.tracker.RpcTrackerEngine;
import com.alibaba.dubbo.tracker.RpcTrackerFactory;
import com.alibaba.dubbo.tracker.TrackerManager;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ExtensionTest {

    private static RpcTrackerEngine trackerEngine;

    @BeforeClass
    public static void before(){
        URL url = URL.valueOf("zipkin://localhost:9411?application=test&collector=http&sampler=counting&rate=0.2");
        trackerEngine = TrackerManager.createRpcTrackerEngine(url);
    }

    @Test
    public void test_rpc_factory() {
        RpcTrackerFactory factory = TrackerManager.getTrackerFactory();
        RpcTrackerFactory factory1 = TrackerManager.getTrackerFactory();
        Assert.assertEquals(factory, factory1);
    }

    @Test
    public void test_create_tracker() {
        URL url = URL.valueOf("dubbo://localhost:9411?application=test&collector=http&sampler=counting&rate=0.2");
        RpcTracker rpcTracker = TrackerManager.createRpcTracker(url);
        RpcTracker rpcTracker1 = TrackerManager.createRpcTracker(url);
        Assert.assertEquals(rpcTracker, rpcTracker1);
    }

    @Test
    public void test_create_engine(){
        URL url = URL.valueOf("zipkin://localhost:9411?application=test&collector=http&sampler=counting&rate=0.2");
        RpcTrackerEngine trackerEngine = TrackerManager.getRpcTrackerEngine();
    }
}
