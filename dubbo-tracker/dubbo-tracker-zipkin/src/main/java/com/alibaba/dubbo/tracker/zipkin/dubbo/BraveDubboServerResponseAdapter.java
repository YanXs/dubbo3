package com.alibaba.dubbo.tracker.zipkin.dubbo;

import com.alibaba.dubbo.tracker.dubbo.DubboResponse;
import com.alibaba.dubbo.tracker.TrackerKeys;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.ServerResponseAdapter;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Xs
 */
public class BraveDubboServerResponseAdapter implements ServerResponseAdapter, com.alibaba.dubbo.tracker.ServerResponseAdapter {

    private final DubboResponse response;

    public BraveDubboServerResponseAdapter(DubboResponse response) {
        this.response = response;
    }

    public boolean isTraceable() {
        return response.isTraceable();
    }

    @Override
    public Collection<KeyValueAnnotation> responseAnnotations() {
        KeyValueAnnotation statusAnnotation;
        if (response.returnOK()) {
            statusAnnotation = KeyValueAnnotation.create(TrackerKeys.RETURN_STATUS, "OK");
        } else {
            statusAnnotation = KeyValueAnnotation.create(TrackerKeys.RETURN_STATUS, response.exceptionMessage());
        }
        return Collections.singletonList(statusAnnotation);
    }

}
