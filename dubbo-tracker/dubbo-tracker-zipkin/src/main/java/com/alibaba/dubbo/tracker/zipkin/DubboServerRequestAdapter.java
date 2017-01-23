package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.DubboRequest;
import com.alibaba.dubbo.tracker.DubboRequestSpanNameProvider;
import com.alibaba.dubbo.tracker.RpcAttachment;
import com.alibaba.dubbo.tracker.TrackerKeys;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.ServerRequestAdapter;
import com.github.kristofa.brave.SpanId;
import com.github.kristofa.brave.TraceData;

import java.util.Collection;
import java.util.Collections;

import static com.github.kristofa.brave.IdConversion.convertToLong;

/**
 * @author Xs
 */
public class DubboServerRequestAdapter implements ServerRequestAdapter {

    private final DubboRequest request;

    private final DubboRequestSpanNameProvider spanNameProvider;

    public DubboServerRequestAdapter(DubboRequest request, DubboRequestSpanNameProvider spanNameProvider) {
        this.request = request;
        this.spanNameProvider = spanNameProvider;
    }

    public boolean isTraceable() {
        return request.isTraceable();
    }

    @Override
    public TraceData getTraceData() {
        final String sampled = request.getAttachment(RpcAttachment.Sampled.getName());
        if (sampled != null) {
            if (sampled.equals("0") || sampled.toLowerCase().equals("false")) {
                return TraceData.builder().sample(false).build();
            } else {
                final String parentSpanId = request.getAttachment(RpcAttachment.ParentSpanId.getName());
                final String traceId = request.getAttachment(RpcAttachment.TraceId.getName());
                final String spanId = request.getAttachment(RpcAttachment.SpanId.getName());

                if (traceId != null && spanId != null) {
                    SpanId span = getSpanId(traceId, spanId, parentSpanId);
                    return TraceData.builder().sample(true).spanId(span).build();
                }
            }
        }
        return TraceData.builder().build();
    }

    @Override
    public String getSpanName() {
        return spanNameProvider.spanName(request);
    }

    @Override
    public Collection<KeyValueAnnotation> requestAnnotations() {
        KeyValueAnnotation annotation = KeyValueAnnotation.create(TrackerKeys.PROVIDER_ADDR, request.address());
        return Collections.singletonList(annotation);
    }

    private SpanId getSpanId(String traceId, String spanId, String parentSpanId) {
        return SpanId.builder()
                .traceId(convertToLong(traceId))
                .spanId(convertToLong(spanId))
                .parentId(parentSpanId == null ? null : convertToLong(parentSpanId)).build();
    }
}
