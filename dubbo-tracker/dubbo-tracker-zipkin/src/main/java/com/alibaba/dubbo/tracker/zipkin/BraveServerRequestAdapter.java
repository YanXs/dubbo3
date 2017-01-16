package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.RpcAttachment;
import com.alibaba.dubbo.tracker.RpcRequest;
import com.alibaba.dubbo.tracker.RpcRequestSpanNameProvider;
import com.alibaba.dubbo.tracker.TrackerKeys;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.ServerRequestAdapter;
import com.github.kristofa.brave.SpanId;
import com.github.kristofa.brave.TraceData;

import java.util.Collection;
import java.util.Collections;

import static com.github.kristofa.brave.IdConversion.convertToLong;

public class BraveServerRequestAdapter implements ServerRequestAdapter {

    private final RpcRequestSpanNameProvider spanNameProvider;

    private final RpcRequest rpcRequest;

    public BraveServerRequestAdapter(RpcRequest rpcRequest, RpcRequestSpanNameProvider spanNameProvider) {
        this.spanNameProvider = spanNameProvider;
        this.rpcRequest = rpcRequest;
    }

    @Override
    public TraceData getTraceData() {
        final String sampled = rpcRequest.getAttachment(RpcAttachment.Sampled.getName());
        if (sampled != null) {
            if (sampled.equals("0") || sampled.toLowerCase().equals("false")) {
                return TraceData.builder().sample(false).build();
            } else {
                final String parentSpanId = rpcRequest.getAttachment(RpcAttachment.ParentSpanId.getName());
                final String traceId = rpcRequest.getAttachment(RpcAttachment.TraceId.getName());
                final String spanId = rpcRequest.getAttachment(RpcAttachment.SpanId.getName());

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
        return spanNameProvider.spanName(rpcRequest);
    }

    @Override
    public Collection<KeyValueAnnotation> requestAnnotations() {
        KeyValueAnnotation annotation = KeyValueAnnotation.create(TrackerKeys.PROVIDER_ADDR, rpcRequest.providerAddress());
        return Collections.singletonList(annotation);
    }

    private SpanId getSpanId(String traceId, String spanId, String parentSpanId) {
        return SpanId.builder()
                .traceId(convertToLong(traceId))
                .spanId(convertToLong(spanId))
                .parentId(parentSpanId == null ? null : convertToLong(parentSpanId)).build();
    }
}
