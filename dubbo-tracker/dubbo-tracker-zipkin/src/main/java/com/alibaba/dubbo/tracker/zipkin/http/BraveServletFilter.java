package com.alibaba.dubbo.tracker.zipkin.http;

import com.alibaba.dubbo.tracker.RpcAttachment;
import com.alibaba.dubbo.tracker.RpcTracker;
import com.alibaba.dubbo.tracker.RpcTrackerEngine;
import com.alibaba.dubbo.tracker.http.ServletFilter;
import com.github.kristofa.brave.ServerRequestInterceptor;
import com.github.kristofa.brave.ServerResponseInterceptor;
import com.github.kristofa.brave.http.SpanNameProvider;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
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
        if (isSampleNeeded(request)) {
            delegate.doFilter(request, response, chain);
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isSampleNeeded(ServletRequest request) {
        return ((HttpServletRequest) request).getHeader(RpcAttachment.Sampled.getName()) != null;
    }

    @Override
    public void destroy() {
        delegate.destroy();
    }
}
