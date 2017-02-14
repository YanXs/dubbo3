package com.alibaba.dubbo.tracker.zipkin.http;

import com.alibaba.dubbo.tracker.RpcTracker;
import com.alibaba.dubbo.tracker.RpcTrackerEngine;
import com.alibaba.dubbo.tracker.http.ServletFilter;
import com.github.kristofa.brave.ServerRequestInterceptor;
import com.github.kristofa.brave.ServerResponseInterceptor;
import com.github.kristofa.brave.http.SpanNameProvider;

import javax.servlet.*;
import java.io.IOException;

public class BraveServletFilter implements ServletFilter {

    private final Filter delegate;

    public BraveServletFilter(RpcTracker rpcTracker, SpanNameProvider spanNameProvider) {
        RpcTrackerEngine trackerEngine = rpcTracker.trackerEngine();
        delegate = new com.github.kristofa.brave.servlet.BraveServletFilter((ServerRequestInterceptor) trackerEngine.serverRequestInterceptor(),
                (ServerResponseInterceptor) trackerEngine.serverResponseInterceptor(), spanNameProvider);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        delegate.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        delegate.doFilter(request, response, chain);
    }

    @Override
    public void destroy() {
        delegate.destroy();
    }
}
