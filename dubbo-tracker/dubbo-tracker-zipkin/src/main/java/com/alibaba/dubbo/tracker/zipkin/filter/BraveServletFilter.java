package com.alibaba.dubbo.tracker.zipkin.filter;

import com.alibaba.dubbo.tracker.filter.ServletFilter;

import javax.servlet.*;
import java.io.IOException;

public class BraveServletFilter implements ServletFilter {

    private final com.github.kristofa.brave.servlet.BraveServletFilter servletFilter;

    public BraveServletFilter(com.github.kristofa.brave.servlet.BraveServletFilter servletFilter) {
        this.servletFilter = servletFilter;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        servletFilter.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletFilter.doFilter(servletRequest, servletResponse, filterChain);
    }

    @Override
    public void destroy() {
        servletFilter.destroy();
    }
}
