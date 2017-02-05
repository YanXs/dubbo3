package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.DubboRequest;
import com.alibaba.dubbo.tracker.DubboRequestSpanNameProvider;
import com.alibaba.dubbo.tracker.RpcAttachment;
import com.alibaba.dubbo.tracker.TrackerKeys;
import com.github.kristofa.brave.ClientRequestAdapter;
import com.github.kristofa.brave.IdConversion;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.SpanId;
import com.twitter.zipkin.gen.Endpoint;

import java.util.Collection;
import java.util.Collections;

public class BraveDubboClientRequestAdapter implements ClientRequestAdapter, com.alibaba.dubbo.tracker.ClientRequestAdapter {

    private final DubboRequest request;

    private final DubboRequestSpanNameProvider spanNameProvider;

    public BraveDubboClientRequestAdapter(DubboRequest request, DubboRequestSpanNameProvider spanNameProvider) {
        this.request = request;
        this.spanNameProvider = spanNameProvider;
    }

    public boolean isTraceable() {
        return request.isTraceable();
    }

    @Override
    public String getSpanName() {
        return spanNameProvider.spanName(request);
    }

    @Override
    public void addSpanIdToRequest(SpanId spanId) {
        if (spanId == null) {
            request.addAttachment(RpcAttachment.Sampled.getName(), "0");
        } else {
            request.addAttachment(RpcAttachment.Sampled.getName(), "1");
            request.addAttachment(RpcAttachment.TraceId.getName(), IdConversion.convertToString(spanId.traceId));
            request.addAttachment(RpcAttachment.SpanId.getName(), IdConversion.convertToString(spanId.spanId));
            if (spanId.nullableParentId() != null) {
                request.addAttachment(RpcAttachment.ParentSpanId.getName(), IdConversion.convertToString(spanId.parentId));
            }
            request.addAttachment(RpcAttachment.SpanName.getName(), getSpanName());
        }
    }

    @Override
    public Collection<KeyValueAnnotation> requestAnnotations() {
        KeyValueAnnotation annotation = KeyValueAnnotation.create(TrackerKeys.PROVIDER_ADDR, request.address());
        return Collections.singletonList(annotation);
    }

    @Override
    public Endpoint serverAddress() {
        return null;
    }
}
