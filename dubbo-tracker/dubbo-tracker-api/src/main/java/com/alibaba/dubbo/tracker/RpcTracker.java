package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.tracker.filter.ServletFilter;

/**
 * @author Xs
 */
public interface RpcTracker {

    ConsumerInvocationInterceptor clientRequestInterceptor();

    ConsumerResultInterceptor clientResponseInterceptor();

    ProviderInvocationInterceptor serverRequestInterceptor();

    ProviderResultInterceptor serverResponseInterceptor();

    ServletFilter servletFilter();
}
