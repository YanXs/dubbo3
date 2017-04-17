package com.alibaba.dubbo.tracker;

/**
 * @author Xs
 */
public interface TraceIdReporter {

    void report(TraceId traceId);

}
