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

    public String get() {
        return traceId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        TraceId that = (TraceId) object;
        return traceId.equals(that.traceId);
    }

    @Override
    public int hashCode() {
        return traceId.hashCode();
    }
}
