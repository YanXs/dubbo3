package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.tracker.DubboSpanNameProvider;
import com.alibaba.dubbo.tracker.ClientRequestInterceptor;
import com.alibaba.dubbo.tracker.ClientResponseInterceptor;
import com.alibaba.dubbo.tracker.RpcTracker;
import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.Sampler;

/**
 * @author Xs
 */
public class BraveRpcTracker implements RpcTracker {

    private final BraveClientRequestInterceptor requestInterceptor;

    public BraveRpcTracker(URL url) {
        Brave.Builder builder = new Brave.Builder(url.getParameter("service-name", "demo"));
        SpanCollectorFactory spanCollectorFactory = new SpanCollectorFactory();
        builder.spanCollector(spanCollectorFactory.create(url));
        builder.traceSampler(Sampler.ALWAYS_SAMPLE);
        Brave brave = builder.build();
        requestInterceptor = new BraveClientRequestInterceptor(brave.clientRequestInterceptor(), new DubboSpanNameProvider());
    }


    @Override
    public ClientRequestInterceptor requestInterceptor() {
        return requestInterceptor;
    }

    @Override
    public ClientResponseInterceptor responseInterceptor() {
        return null;
    }
}
