package com.alibaba.dubbo.tracker;

/**
 * @author Xs
 */
public class TraceId {

    public static final TraceId NOT_TRACE = new TraceId(false, "");

    private final boolean sampled;

    private final String traceId;

    public TraceId(boolean sampled, String traceId) {
        this.sampled = sampled;
        this.traceId = traceId;
    }

    public boolean isSampled() {
        return sampled;
    }

    public String getTraceId() {
        return traceId;
    }
}
