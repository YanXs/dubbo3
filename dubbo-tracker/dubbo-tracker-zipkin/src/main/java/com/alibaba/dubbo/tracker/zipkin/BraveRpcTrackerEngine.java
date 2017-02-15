package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.tracker.RpcTrackerEngine;
import com.github.kristofa.brave.*;
import com.github.kristofa.brave.http.HttpSpanCollector;
import com.github.kristofa.brave.kafka.KafkaSpanCollector;
import com.github.kristofa.brave.scribe.ScribeSpanCollector;
import com.github.kristofa.brave.scribe.ScribeSpanCollectorParams;

import java.net.URLDecoder;

/**
 * 基于Zipkin的RpcTracker引擎
 *
 * @author Xs
 */
public class BraveRpcTrackerEngine implements RpcTrackerEngine {

    private static final SpanCollectorMetricsHandler DEFAULT_HANDLER = new LoggingSpanCollectorMetricsHandler();

    private final Brave brave;

    BraveRpcTrackerEngine(URL url) {
        Brave.Builder builder = new Brave.Builder(url.getParameter("application"));
        builder.spanCollector(createSpanCollector(url));
        builder.traceSampler(createSampler(url));
        brave = builder.build();
    }

    private SpanCollector createSpanCollector(URL url) {
        String transport = url.getParameter("transport", "http");
        Integer flushInterval = url.getParameter("flushiterval", 1);
        if (transport.equals("http")) {
            HttpSpanCollector.Config.Builder builder = HttpSpanCollector.Config.builder();
            builder.flushInterval(flushInterval);
            String baseUrl = "http://" + url.getHost() + ":" + url.getPort();
            return HttpSpanCollector.create(baseUrl, builder.build(), DEFAULT_HANDLER);
        } else if (transport.equals("kafka")) {
            String address = url.getParameter("address");
            if (StringUtils.isEmpty(address)) {
                throw new IllegalArgumentException("transport address must not be null");
            }
            KafkaSpanCollector.Config.Builder builder = KafkaSpanCollector.Config.builder(address);
            builder.flushInterval(flushInterval);
            return KafkaSpanCollector.create(builder.build(), DEFAULT_HANDLER);
        } else if (transport.equals("scribe")) {
            ScribeSpanCollectorParams params = new ScribeSpanCollectorParams();
            params.setMetricsHandler(DEFAULT_HANDLER);
            return new ScribeSpanCollector(url.getHost(), url.getPort(), params);
        } else {
            throw new IllegalArgumentException("unknown transport type, transport: " + transport);
        }
    }

    private Sampler createSampler(URL url) {
        String sampler = url.getParameter("sampler");
        if (StringUtils.isEmpty(sampler)) {
            return Sampler.ALWAYS_SAMPLE;
        } else {
            String rate = url.getParameter("rate");
            if (StringUtils.isEmpty(rate)) {
                return Sampler.ALWAYS_SAMPLE;
            }
            if (sampler.equals("counting")) {
                return CountingSampler.create(Float.valueOf(rate));
            } else if (sampler.equals("boundary")) {
                return BoundarySampler.create(Float.valueOf(rate));
            } else {
                throw new IllegalArgumentException("unknown sampler type, sampler: " + sampler);
            }
        }
    }

    public static BraveRpcTrackerEngine create(URL url) {
        return new BraveRpcTrackerEngine(url);
    }

    @Override
    public ClientRequestInterceptor clientRequestInterceptor() {
        return brave.clientRequestInterceptor();
    }

    @Override
    public ClientResponseInterceptor clientResponseInterceptor() {
        return brave.clientResponseInterceptor();
    }

    @Override
    public ServerRequestInterceptor serverRequestInterceptor() {
        return brave.serverRequestInterceptor();
    }

    @Override
    public ServerResponseInterceptor serverResponseInterceptor() {
        return brave.serverResponseInterceptor();
    }
}
