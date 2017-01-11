package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.tracker.filter.ServletFilter;

/**
 * @author Xs
 */
public interface RpcTracker {

    ClientRequestInterceptor requestInterceptor();

    ClientResponseInterceptor responseInterceptor();

    ServletFilter servletFilter();
}
