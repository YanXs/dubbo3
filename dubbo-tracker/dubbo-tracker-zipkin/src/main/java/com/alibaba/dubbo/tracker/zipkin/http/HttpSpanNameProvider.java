package com.alibaba.dubbo.tracker.zipkin.http;

import com.alibaba.dubbo.tracker.RpcAttachment;
import com.alibaba.dubbo.tracker.RpcContextMethodNameProvider;
import com.github.kristofa.brave.http.HttpClientRequest;
import com.github.kristofa.brave.http.HttpRequest;
import com.github.kristofa.brave.http.HttpServerRequest;
import com.github.kristofa.brave.http.SpanNameProvider;

public class HttpSpanNameProvider extends RpcContextMethodNameProvider implements SpanNameProvider {

    @Override
    public String spanName(HttpRequest request) {
        String spanName;
        if (request instanceof HttpClientRequest) {
            spanName = getMethodNameFromContext();
        } else if (request instanceof HttpServerRequest) {
            spanName = getSpanNameFromHeader((HttpServerRequest) request);
        } else {
            throw new IllegalArgumentException("wrong type http request, " + request);
        }
        return spanName;
    }


    private String getSpanNameFromHeader(HttpServerRequest httpServerRequest) {
        return httpServerRequest.getHttpHeaderValue(RpcAttachment.SpanName.getName());
    }

}
