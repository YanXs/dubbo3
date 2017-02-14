package com.alibaba.dubbo.tracker.zipkin.http;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.tracker.RpcTracker;
import com.alibaba.dubbo.tracker.RpcTrackerManager;
import com.alibaba.dubbo.tracker.http.ServletFilter;
import com.alibaba.dubbo.tracker.http.ServletFilterBuilder;

public class BraveServletFilterBuilder implements ServletFilterBuilder {

    @Override
    public ServletFilter build(URL url) {
        RpcTracker rpcTracker = RpcTrackerManager.getRpcTracker(url);
        if (rpcTracker == null) {
            return null;
        }
        return new BraveServletFilter(rpcTracker, HttpSpanNameProvider.getInstance());
    }
}
