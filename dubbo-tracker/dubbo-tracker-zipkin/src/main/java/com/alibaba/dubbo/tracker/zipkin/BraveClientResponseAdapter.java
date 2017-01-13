package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.TrackerKeys;
import com.github.kristofa.brave.ClientResponseAdapter;
import com.github.kristofa.brave.KeyValueAnnotation;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Xs
 */
public class BraveClientResponseAdapter implements ClientResponseAdapter {

    private final BraveRpcResult rpcResult;

    public BraveClientResponseAdapter(BraveRpcResult rpcResult) {
        this.rpcResult = rpcResult;
    }

    @Override
    public Collection<KeyValueAnnotation> responseAnnotations() {
        if (rpcResult.hasException()) {
            KeyValueAnnotation statusAnnotation = KeyValueAnnotation.create(TrackerKeys.RETURN_STATUS, rpcResult.exception());
            return Collections.singletonList(statusAnnotation);
        }
        return Collections.emptyList();

    }
}
