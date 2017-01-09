package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.tracker.DecodeableResponse;
import com.github.kristofa.brave.ClientResponseAdapter;
import com.github.kristofa.brave.KeyValueAnnotation;

import java.util.Collection;

/**
 * @author Xs
 */
public class BraveClientResponseAdapter implements ClientResponseAdapter {


    private DecodeableResponse response;

    public BraveClientResponseAdapter(DecodeableResponse response) {
        this.response = response;
    }

    @Override
    public Collection<KeyValueAnnotation> responseAnnotations() {
        return null;
    }
}
