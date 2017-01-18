package com.alibaba.dubbo.tracker.zipkin.http;

import com.alibaba.dubbo.tracker.DubboRequestSpanNameProvider;
import com.alibaba.dubbo.tracker.RpcAttachment;
import com.github.kristofa.brave.*;
import com.github.kristofa.brave.http.HttpClientRequest;
import com.github.kristofa.brave.internal.Nullable;
import com.twitter.zipkin.gen.Endpoint;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;

public class HttpClientRequestAdapter implements ClientRequestAdapter {

    private final HttpClientRequest request;
    private final DubboRequestSpanNameProvider spanNameProvider;

    public HttpClientRequestAdapter(HttpClientRequest request, DubboRequestSpanNameProvider spanNameProvider) {
        this.request = request;
        this.spanNameProvider = spanNameProvider;
    }

    @Override
    public String getSpanName() {
        return spanNameProvider.spanName();
    }

    @Override
    public void addSpanIdToRequest(@Nullable SpanId spanId) {
        if (spanId == null) {
            request.addHeader(RpcAttachment.Sampled.getName(), "0");
        } else {
            request.addHeader(RpcAttachment.Sampled.getName(), "1");
            request.addHeader(RpcAttachment.TraceId.getName(), IdConversion.convertToString(spanId.traceId));
            request.addHeader(RpcAttachment.SpanId.getName(), IdConversion.convertToString(spanId.spanId));
            if (spanId.nullableParentId() != null) {
                request.addHeader(RpcAttachment.ParentSpanId.getName(), IdConversion.convertToString(spanId.parentId));
            }
            request.addHeader(RpcAttachment.Method.getName(), getSpanName());
        }
    }

    @Override
    public Collection<KeyValueAnnotation> requestAnnotations() {
        URI uri = request.getUri();
        KeyValueAnnotation annotation = KeyValueAnnotation.create(TraceKeys.HTTP_URL, uri.toString());
        return Collections.singletonList(annotation);
    }

    @Override
    public Endpoint serverAddress() {
        return null;
    }
}
