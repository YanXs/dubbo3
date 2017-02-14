package com.alibaba.dubbo.tracker.dubbo;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;
import com.alibaba.dubbo.remoting.message.Interceptor;

/**
 * @author Xs.
 */
@SPI("zipkin")
public interface DubboRequestInterceptorBuilder {

    @Adaptive({"tracker"})
    Interceptor build(URL url, DubboRequestSpanNameProvider spanNameProvider);

}
