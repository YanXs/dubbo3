package com.alibaba.dubbo.rpc.proxy.bytebuddy;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

/**
 * @author Xs.
 */
public enum ObjectMethodElementMatchers implements ElementMatcher<MethodDescription> {

    INSTANCE;

    private final Junction<MethodDescription> matcher;

    ObjectMethodElementMatchers() {
        matcher = ElementMatchers.named("getClass").and(takesArguments(0))
                .or(named("equals").and(takesArguments(Object.class)))
                .or(named("hashCode").and(takesArguments(0)))
                .or(named("wait").and(takesArguments(0)))
                .or(named("wait").and(takesArguments(long.class)))
                .or(named("wait").and(takesArguments(long.class, int.class)))
                .or(named("toString").and(takesArguments(0)))
                .or(named("clone").and(takesArguments(0)))
                .or(named("notify").and(takesArguments(0)))
                .or(named("notifyAll").and(takesArguments(0)))
                .or(named("finalize").and(takesArguments(0)));
    }

    @Override
    public boolean matches(MethodDescription target) {
        return matcher.matches(target);
    }
}
