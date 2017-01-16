package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.RpcAttachment;
import com.alibaba.dubbo.tracker.RpcRequest;
import com.alibaba.dubbo.tracker.RpcRequestSpanNameProvider;
import com.alibaba.dubbo.tracker.TrackerKeys;
import com.github.kristofa.brave.ClientRequestAdapter;
import com.github.kristofa.brave.IdConversion;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.SpanId;
import com.twitter.zipkin.gen.Endpoint;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Xs
 */
public class BraveClientRequestAdapter implements ClientRequestAdapter {

    private final RpcRequestSpanNameProvider spanNameProvider;

    private final RpcRequest rpcRequest;

    public BraveClientRequestAdapter(RpcRequest rpcRequest, RpcRequestSpanNameProvider spanNameProvider) {
        this.rpcRequest = rpcRequest;
        this.spanNameProvider = spanNameProvider;
    }

    @Override
    public String getSpanName() {
        return spanNameProvider.spanName(rpcRequest);
    }

    @Override
    public void addSpanIdToRequest(SpanId spanId) {
        if (spanId == null) {
            rpcRequest.addAttachment(RpcAttachment.Sampled.getName(), "0");
        } else {
            rpcRequest.addAttachment(RpcAttachment.Sampled.getName(), "1");
            rpcRequest.addAttachment(RpcAttachment.TraceId.getName(), IdConversion.convertToString(spanId.traceId));
            rpcRequest.addAttachment(RpcAttachment.SpanId.getName(), IdConversion.convertToString(spanId.spanId));
            if (spanId.nullableParentId() != null) {
                rpcRequest.addAttachment(RpcAttachment.ParentSpanId.getName(), IdConversion.convertToString(spanId.parentId));
            }
        }
    }

    @Override
    public Collection<KeyValueAnnotation> requestAnnotations() {
        KeyValueAnnotation annotation = KeyValueAnnotation.create(TrackerKeys.PROVIDER_ADDR, rpcRequest.providerAddress());
        return Collections.singletonList(annotation);
    }

    @Override
    public Endpoint serverAddress() {
        return null;
    }
}
