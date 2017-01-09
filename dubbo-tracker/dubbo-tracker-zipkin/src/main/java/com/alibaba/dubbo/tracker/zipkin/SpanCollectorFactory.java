package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.common.URL;
import com.github.kristofa.brave.SpanCollector;
import com.github.kristofa.brave.http.HttpSpanCollector;

/**
 * @author Xs
 */
public class SpanCollectorFactory {

    public SpanCollector create(URL url) {
        return HttpSpanCollector.create("http://192.168.150.132:9411", new LoggingSpanCollectorMetricsHandler());
    }
}
