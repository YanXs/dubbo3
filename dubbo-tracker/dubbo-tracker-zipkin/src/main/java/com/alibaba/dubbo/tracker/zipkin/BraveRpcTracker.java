package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.tracker.ClientRequestInterceptor;
import com.alibaba.dubbo.tracker.ClientResponseInterceptor;
import com.alibaba.dubbo.tracker.DubboSpanNameProvider;
import com.alibaba.dubbo.tracker.RpcTracker;
import com.alibaba.dubbo.tracker.filter.ServletFilter;
import com.alibaba.dubbo.tracker.zipkin.filter.BraveServletFilter;
import com.github.kristofa.brave.*;
import com.github.kristofa.brave.http.DefaultSpanNameProvider;
import com.github.kristofa.brave.http.HttpSpanCollector;
import com.github.kristofa.brave.kafka.KafkaSpanCollector;
import com.github.kristofa.brave.scribe.ScribeSpanCollector;
import com.github.kristofa.brave.scribe.ScribeSpanCollectorParams;

/**
 * @author Xs
 */
public class BraveRpcTracker implements RpcTracker {

    private final SpanCollectorMetricsHandler DEFAULT_HANDLER = new LoggingSpanCollectorMetricsHandler();

    private final BraveClientRequestInterceptor requestInterceptor;

    private final BraveServletFilter braveServletFilter;

    BraveRpcTracker create(URL url) {
        return new BraveRpcTracker(url);
    }

    private BraveRpcTracker(URL url) {
        Brave.Builder builder = new Brave.Builder(url.getParameter("service-name", "demo"));
        builder.spanCollector(createSpanCollector(url));
        builder.traceSampler(createSampler(url));
        Brave brave = builder.build();
        requestInterceptor = new BraveClientRequestInterceptor(brave.clientRequestInterceptor(), new DubboSpanNameProvider());
        braveServletFilter = new BraveServletFilter(new com.github.kristofa.brave.servlet.BraveServletFilter(
                brave.serverRequestInterceptor(), brave.serverResponseInterceptor(), new DefaultSpanNameProvider()));
    }

    private SpanCollector createSpanCollector(URL url) {
        String collector = url.getParameter("collector", "http");
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

    private Sampler createSampler(URL url) {
        String sampler = url.getParameter("sampler");
        if (StringUtils.isEmpty(sampler)) {
            return Sampler.ALWAYS_SAMPLE;
        } else {
            String rate = url.getParameter("rate");
            if (StringUtils.isEmpty(rate)) {
                throw new IllegalArgumentException("sample rate must not be null");
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


    @Override
    public ClientRequestInterceptor requestInterceptor() {
        return requestInterceptor;
    }

    @Override
    public ClientResponseInterceptor responseInterceptor() {
        return null;
    }

    @Override
    public ServletFilter servletFilter() {
        return braveServletFilter;
    }
}
