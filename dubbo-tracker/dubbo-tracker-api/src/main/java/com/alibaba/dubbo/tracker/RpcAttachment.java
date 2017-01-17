package com.alibaba.dubbo.tracker;

/**
 * @author Xs
 */
public enum RpcAttachment {

    TraceId("X-B3-TraceId"),

    SpanId("X-B3-SpanId"),

    ParentSpanId("X-B3-ParentSpanId"),

    Sampled("X-B3-Sampled"),

    Method("X-B3-Method");

    private final String name;

    RpcAttachment(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
