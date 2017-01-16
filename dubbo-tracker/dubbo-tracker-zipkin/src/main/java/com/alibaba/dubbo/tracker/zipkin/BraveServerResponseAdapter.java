package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.RpcResponse;
import com.alibaba.dubbo.tracker.TrackerKeys;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.ServerResponseAdapter;

import java.util.Collection;
import java.util.Collections;

public class BraveServerResponseAdapter implements ServerResponseAdapter {

    private final RpcResponse rpcResponse;

    public BraveServerResponseAdapter(RpcResponse rpcResponse) {
        this.rpcResponse = rpcResponse;
    }

    @Override
    public Collection<KeyValueAnnotation> responseAnnotations() {
        KeyValueAnnotation statusAnnotation;
        if (rpcResponse.returnSuccessfully()) {
            statusAnnotation = KeyValueAnnotation.create(TrackerKeys.RETURN_STATUS, "OK");
        } else {
            statusAnnotation = KeyValueAnnotation.create(TrackerKeys.RETURN_STATUS, rpcResponse.exceptionMessage());
        }
        return Collections.singletonList(statusAnnotation);
    }
}
