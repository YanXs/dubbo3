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

    private final BraveClientRequestInterceptor clientRequestInterceptor;

    private final BraveClientResponseInterceptor clientResponseInterceptor;

    private final BraveServerRequestInterceptor serverRequestInterceptor;

    private final BraveServerResponseInterceptor serverResponseInterceptor;


    private final BraveServletFilter braveServletFilter;

    static BraveRpcTracker create(URL url) {
        return new BraveRpcTracker(url);
    }

    private BraveRpcTracker(URL url) {
        Brave.Builder builder = new Brave.Builder(url.getParameter("application", "demo"));
        builder.spanCollector(SpanCollectorFactory.create(url));
        builder.traceSampler(SamplerFactory.create(url));
        Brave brave = builder.build();
        clientRequestInterceptor = new BraveClientRequestInterceptor(brave.clientRequestInterceptor(), new InvocationSpanNameProvider());
        clientResponseInterceptor = new BraveClientResponseInterceptor(brave.clientResponseInterceptor());
        serverRequestInterceptor = new BraveServerRequestInterceptor(brave.serverRequestInterceptor(), new InvocationSpanNameProvider());
        serverResponseInterceptor = new BraveServerResponseInterceptor(brave.serverResponseInterceptor());
        braveServletFilter = new BraveServletFilter(new com.github.kristofa.brave.servlet.BraveServletFilter(
                brave.serverRequestInterceptor(), brave.serverResponseInterceptor(), new DefaultSpanNameProvider()));
    }

    @Override
    public ClientRequestInterceptor clientRequestInterceptor() {
        return clientRequestInterceptor;
    }

    @Override
    public ClientResponseInterceptor clientResponseInterceptor() {
        return clientResponseInterceptor;
    }

    @Override
    public ServerRequestInterceptor serverRequestInterceptor() {
        return serverRequestInterceptor;
    }

    @Override
    public ServerResponseInterceptor serverResponseInterceptor() {
        return serverResponseInterceptor;
    }

    @Override
    public ServletFilter servletFilter() {
        return braveServletFilter;
    }
}
