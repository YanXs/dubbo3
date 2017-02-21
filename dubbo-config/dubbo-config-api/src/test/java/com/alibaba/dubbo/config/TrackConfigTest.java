package com.alibaba.dubbo.config;

import org.junit.Test;

public class TrackConfigTest {

    @Test
    public void test_trackConfig(){
        RpcTrackerEngineConfig trackerEngineConfig = new RpcTrackerEngineConfig();
        trackerEngineConfig.setProtocol("zipkin");
        trackerEngineConfig.setAddress("localhost:9411");
        trackerEngineConfig.setApplication("test");
        trackerEngineConfig.setTransport("kafka");
        trackerEngineConfig.setSampler("counting");
        trackerEngineConfig.setSamplerate(0.1f);
        trackerEngineConfig.setFlushinterval(2);
        System.out.println(trackerEngineConfig.toString());
    }
}
