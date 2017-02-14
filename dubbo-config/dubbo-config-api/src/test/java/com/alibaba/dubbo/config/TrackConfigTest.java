package com.alibaba.dubbo.config;

import org.junit.Test;

public class TrackConfigTest {

    @Test
    public void test_trackConfig(){
        TrackerConfig trackerConfig = new TrackerConfig();
        trackerConfig.setProtocol("zipkin");
        trackerConfig.setAddress("localhost:9411");
        trackerConfig.setApplication("test");
        trackerConfig.setTransport("kafka");
        trackerConfig.setSampler("counting");
        trackerConfig.setSamplerate(0.1f);
        trackerConfig.setFlushinterval(2);
        System.out.println(trackerConfig.toString());
    }
}
