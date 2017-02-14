package com.alibaba.dubbo.tracker.http;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;

@SPI("zipkin")
public interface ServletFilterBuilder {

    @Adaptive("tracker")
    ServletFilter build(URL url);

}
