package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.DubboResponse;
import com.alibaba.dubbo.tracker.TrackerKeys;
import com.github.kristofa.brave.ClientResponseAdapter;
import com.github.kristofa.brave.KeyValueAnnotation;

import java.util.Collection;
import java.util.Collections;

public class DubboClientResponseAdapter implements ClientResponseAdapter {

    private final DubboResponse response;

    public DubboClientResponseAdapter(DubboResponse response) {
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
