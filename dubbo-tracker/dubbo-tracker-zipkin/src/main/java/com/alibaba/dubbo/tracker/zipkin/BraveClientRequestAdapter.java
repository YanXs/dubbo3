package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.InvocationSpanNameProvider;
import com.alibaba.dubbo.tracker.RpcAttachment;
import com.alibaba.dubbo.tracker.RpcRequest;
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

    private final InvocationSpanNameProvider spanNameProvider;

    private final RpcRequest rpcRequest;

    public BraveClientRequestAdapter(RpcRequest rpcRequest, InvocationSpanNameProvider spanNameProvider) {
        this.rpcRequest = rpcRequest;
        this.spanNameProvider = spanNameProvider;
    }

    @Override
    public String getSpanName() {
        return spanNameProvider.spanName(invocation.getRpcInvocation());
    }

    @Override
    public void addSpanIdToRequest(SpanId spanId) {
        if (spanId == null) {
            invocation.addAttachment(RpcAttachment.Sampled.getName(), "0");
        } else {
            invocation.addAttachment(RpcAttachment.Sampled.getName(), "1");
            invocation.addAttachment(RpcAttachment.TraceId.getName(), IdConversion.convertToString(spanId.traceId));
            invocation.addAttachment(RpcAttachment.SpanId.getName(), IdConversion.convertToString(spanId.spanId));
            if (spanId.nullableParentId() != null) {
                invocation.addAttachment(RpcAttachment.ParentSpanId.getName(), IdConversion.convertToString(spanId.parentId));
            }
        }
    }

    @Override
    public Collection<KeyValueAnnotation> requestAnnotations() {
        KeyValueAnnotation annotation = KeyValueAnnotation.create(TrackerKeys.PROVIDER_ADDR, invocation.providerAddress());
        return Collections.singletonList(annotation);
    }

    @Override
    public Endpoint serverAddress() {
        return null;
    }
}
