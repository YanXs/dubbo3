package com.alibaba.dubbo.tracker.zipkin.http;

import com.github.kristofa.brave.ClientRequestInterceptor;
import com.github.kristofa.brave.ClientResponseInterceptor;
import com.github.kristofa.brave.http.SpanNameProvider;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

public class HttpRequestResponseInterceptor implements Interceptor {

    private final ClientRequestInterceptor clientRequestInterceptor;
    private final ClientResponseInterceptor clientResponseInterceptor;

    public HttpRequestResponseInterceptor(ClientRequestInterceptor clientRequestInterceptor,
                                          ClientResponseInterceptor clientResponseInterceptor,
                                          SpanNameProvider spanNameProvider) {
        this.clientRequestInterceptor = clientRequestInterceptor;
        this.clientResponseInterceptor = clientResponseInterceptor;
        this.spanNameProvider = spanNameProvider;
    }

    private final SpanNameProvider spanNameProvider;

    @Override
    public Response intercept(Chain chain) throws IOException {
        return null;
    }

}
