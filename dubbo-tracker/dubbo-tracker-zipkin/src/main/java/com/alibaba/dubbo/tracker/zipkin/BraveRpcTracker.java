package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.tracker.*;
import com.alibaba.dubbo.tracker.filter.ServletFilter;
import com.alibaba.dubbo.tracker.zipkin.filter.BraveServletFilter;
import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.http.DefaultSpanNameProvider;

/**
 * @author Xs
 */
public class BraveRpcTracker implements RpcTracker {

    private final BraveConsumerInvocationInterceptor clientRequestInterceptor;

    private final BraveConsumerResultInterceptor clientResponseInterceptor;

    private final BraveProviderInvocationInterceptor serverRequestInterceptor;

    private final BraveProviderResultInterceptor serverResponseInterceptor;

    private final BraveServletFilter braveServletFilter;

    static BraveRpcTracker create(URL url) {
        return new BraveRpcTracker(url);
    }

    private BraveRpcTracker(URL url) {
        Brave.Builder builder = new Brave.Builder(url.getParameter("application", "demo"));
        builder.spanCollector(SpanCollectorFactory.create(url));
        builder.traceSampler(SamplerFactory.create(url));
        Brave brave = builder.build();
        clientRequestInterceptor = new BraveConsumerInvocationInterceptor(brave.clientRequestInterceptor(), new RpcRequestSpanNameProvider());
        clientResponseInterceptor = new BraveConsumerResultInterceptor(brave.clientResponseInterceptor());
        serverRequestInterceptor = new BraveProviderInvocationInterceptor(brave.serverRequestInterceptor(), new RpcRequestSpanNameProvider());
        serverResponseInterceptor = new BraveProviderResultInterceptor(brave.serverResponseInterceptor());
        braveServletFilter = new BraveServletFilter(new com.github.kristofa.brave.servlet.BraveServletFilter(
                brave.serverRequestInterceptor(), brave.serverResponseInterceptor(), new DefaultSpanNameProvider()));
    }

    @Override
    public ConsumerInvocationInterceptor clientRequestInterceptor() {
        return clientRequestInterceptor;
    }

    @Override
    public ConsumerResultInterceptor clientResponseInterceptor() {
        return clientResponseInterceptor;
    }

    @Override
    public ProviderInvocationInterceptor serverRequestInterceptor() {
        return serverRequestInterceptor;
    }

    @Override
    public ProviderResultInterceptor serverResponseInterceptor() {
        return serverResponseInterceptor;
    }

    @Override
    public ServletFilter servletFilter() {
        return braveServletFilter;
    }
}
