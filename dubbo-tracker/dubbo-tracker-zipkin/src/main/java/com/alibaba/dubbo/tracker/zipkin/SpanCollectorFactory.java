package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.common.URL;
import com.github.kristofa.brave.SpanCollector;
import com.github.kristofa.brave.SpanCollectorMetricsHandler;
import com.github.kristofa.brave.http.HttpSpanCollector;
import com.github.kristofa.brave.kafka.KafkaSpanCollector;
import com.github.kristofa.brave.scribe.ScribeSpanCollector;
import com.github.kristofa.brave.scribe.ScribeSpanCollectorParams;

/**
 * @author Xs.
 */
public class SpanCollectorFactory {

    private static final SpanCollectorMetricsHandler DEFAULT_HANDLER = new LoggingSpanCollectorMetricsHandler();

    public static SpanCollector create(URL url) {
        String collector = url.getParameter("transport", "http");
        if (collector.equals("http")) {
            HttpSpanCollector.Config config = HttpSpanCollector.Config.builder().build();
            String baseUrl = "http://" + url.getHost() + ":" + url.getPort();
            return HttpSpanCollector.create(baseUrl, config, DEFAULT_HANDLER);
        } else if (collector.equals("kafka")) {
            return KafkaSpanCollector.create(url.getHost() + ":" + url.getPort(), DEFAULT_HANDLER);
        } else if (collector.equals("scribe")) {
            ScribeSpanCollectorParams params = new ScribeSpanCollectorParams();
            params.setMetricsHandler(DEFAULT_HANDLER);
            return new ScribeSpanCollector(url.getHost(), url.getPort(), params);
        } else {
            throw new IllegalArgumentException("unknown collector type, collector: " + collector);
        }
    }
}
