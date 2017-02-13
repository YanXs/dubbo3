package com.alibaba.dubbo.tracker.zipkin.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.message.Interceptor;
import com.alibaba.dubbo.tracker.RpcTracker;
import com.alibaba.dubbo.tracker.RpcTrackerManager;
import com.alibaba.dubbo.tracker.dubbo.DubboRequestInterceptorBuilder;
import com.alibaba.dubbo.tracker.dubbo.DubboRequestSpanNameProvider;

/**
 * @author Xs.
 */
public class BraveDubboRequestInterceptorBuilder implements DubboRequestInterceptorBuilder {

    @Override
    public Interceptor build(URL url, DubboRequestSpanNameProvider spanNameProvider) {
        RpcTracker rpcTracker = RpcTrackerManager.getRpcTracker(url);
        if (rpcTracker == null) {
            return null;
        }
        if (url.getParameter(Constants.SIDE_KEY).equals("provider")) {
            return new BraveDubboServerRequestResponseInterceptor(rpcTracker, spanNameProvider);
        } else if (url.getParameter(Constants.SIDE_KEY).equals("consumer")) {
            return new BraveDubboClientRequestResponseInterceptor(rpcTracker, spanNameProvider);
        } else {
            throw new IllegalArgumentException("URL doesn't contain side key, " + url);
        }
    }
}
