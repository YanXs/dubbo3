package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.github.kristofa.brave.SpanCollectorMetricsHandler;

/**
 * @author Xs
 */
public class LoggingSpanCollectorMetricsHandler implements SpanCollectorMetricsHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Override
    public void incrementAcceptedSpans(int quantity) {
        // NOP
    }

    @Override
    public void incrementDroppedSpans(int quantity) {
        LOGGER.warn(quantity + " spans were dropped !");
    }
}
