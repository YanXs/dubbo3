package com.alibaba.dubbo.tracker.zipkin.dubbo;

import com.alibaba.dubbo.tracker.dubbo.DubboResponse;
import com.alibaba.dubbo.tracker.TrackerKeys;
import com.github.kristofa.brave.ClientResponseAdapter;
import com.github.kristofa.brave.KeyValueAnnotation;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Xs.
 */
public class BraveDubboClientResponseAdapter implements ClientResponseAdapter, com.alibaba.dubbo.tracker.ClientResponseAdapter {

    private final DubboResponse response;

    public BraveDubboClientResponseAdapter(DubboResponse response) {
        this.response = response;
    }

    public boolean isTraceable() {
        return response.isTraceable();
    }

    @Override
    public Collection<KeyValueAnnotation> responseAnnotations() {
        if (!response.returnOK()) {
            KeyValueAnnotation statusAnnotation = KeyValueAnnotation.create(TrackerKeys.RETURN_STATUS, response.exceptionMessage());
            return Collections.singletonList(statusAnnotation);
        }
        return Collections.emptyList();
    }
}
