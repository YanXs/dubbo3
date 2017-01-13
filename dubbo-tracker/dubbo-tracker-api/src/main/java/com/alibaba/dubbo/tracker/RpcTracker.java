package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.tracker.filter.ServletFilter;

/**
 * @author Xs
 */
public interface RpcTracker {

    ClientRequestInterceptor clientRequestInterceptor();

    ClientResponseInterceptor clientResponseInterceptor();

    ServerRequestInterceptor serverRequestInterceptor();

    ServerResponseInterceptor serverResponseInterceptor();

    ServletFilter servletFilter();
}
