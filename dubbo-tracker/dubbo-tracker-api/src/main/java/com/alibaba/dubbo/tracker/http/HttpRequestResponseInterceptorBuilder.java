package com.alibaba.dubbo.tracker.http;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;
import okhttp3.Interceptor;

/**
 * @author Xs.
 */
@SPI("zipkin")
public interface HttpRequestResponseInterceptorBuilder {

    @Adaptive
    Interceptor build(URL url);

}
