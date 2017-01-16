package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.RpcResponse;
import com.alibaba.dubbo.tracker.TrackerKeys;
import com.github.kristofa.brave.ClientResponseAdapter;
import com.github.kristofa.brave.KeyValueAnnotation;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Xs
 */
public class BraveClientResponseAdapter implements ClientResponseAdapter {

    private final RpcResponse rpcResponse;

    public BraveClientResponseAdapter(RpcResponse rpcResponse) {
        this.rpcResponse = rpcResponse;
    }

    @Override
    public Collection<KeyValueAnnotation> responseAnnotations() {
        if (!rpcResponse.returnSuccessfully()) {
            KeyValueAnnotation statusAnnotation = KeyValueAnnotation.create(TrackerKeys.RETURN_STATUS, rpcResponse.exceptionMessage());
            return Collections.singletonList(statusAnnotation);
        }
        return Collections.emptyList();

    }
}
