package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.tracker.filter.ServletFilter;

/**
 * @author Xs
 */
public interface RpcTracker {

    ClientRequestInterceptor clientRequestInterceptor();

    ClientResponseInterceptor clientResponseInterceptor();

    ServletFilter servletFilter();

    RpcTrackerEngine trackerEngine();
}
