package com.alibaba.dubbo.config;

import org.junit.Test;

public class TrackConfigTest {

    @Test
    public void test_trackConfig(){
        TrackerConfig trackerConfig = new TrackerConfig();
        trackerConfig.setProtocol("zipkin");
        trackerConfig.setAddress("localhost:9411");
        trackerConfig.setApplication("test");
        trackerConfig.setCollector("kafka");
        trackerConfig.setSampler("counting");
        trackerConfig.setSampleRate(0.1f);
        trackerConfig.setFlushInterval(2);
        System.out.println(trackerConfig.toString());
    }
}
