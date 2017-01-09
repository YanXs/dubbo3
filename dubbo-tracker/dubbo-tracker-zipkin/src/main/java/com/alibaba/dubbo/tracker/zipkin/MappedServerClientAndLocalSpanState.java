package com.alibaba.dubbo.tracker.zipkin;

import com.github.kristofa.brave.ServerClientAndLocalSpanState;
import com.github.kristofa.brave.ServerSpan;
import com.github.kristofa.brave.internal.Util;
import com.twitter.zipkin.gen.Endpoint;
import com.twitter.zipkin.gen.Span;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Xs
 */
public class MappedServerClientAndLocalSpanState implements ServerClientAndLocalSpanState {

    private final Endpoint endpoint;

    public MappedServerClientAndLocalSpanState(int ip, int port, String serviceName) {
        Util.checkNotBlank(serviceName, "Service name must be specified.");
        endpoint = Endpoint.create(serviceName, ip, port);
    }

    @Override
    public Span getCurrentClientSpan() {
        return null;
    }

    @Override
    public void setCurrentClientSpan(Span span) {

    }

    @Override
    public Span getCurrentLocalSpan() {
        return null;
    }

    @Override
    public void setCurrentLocalSpan(Span span) {

    }

    @Override
    public ServerSpan getCurrentServerSpan() {
        return null;
    }

    @Override
    public void setCurrentServerSpan(ServerSpan span) {

    }

    @Override
    public Boolean sample() {
        return null;
    }

    @Override
    public Endpoint endpoint() {
        return null;
    }
}
