package com.alibaba.dubbo.tracker.zipkin.filter;

import com.alibaba.dubbo.tracker.filter.ServletFilter;
import com.github.kristofa.brave.servlet.BraveServletFilter;

import javax.servlet.*;
import java.io.IOException;

public class BraveHttpServletFilter implements ServletFilter {

    private final BraveServletFilter servletFilter;

    public BraveHttpServletFilter(BraveServletFilter servletFilter) {
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
