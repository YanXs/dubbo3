package com.alibaba.dubbo.rpc.proxy.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Xs.
 */
public class ProxyClassBuilder {

    private static final ConcurrentMap<Class<?>, Class<?>> classCache = new ConcurrentHashMap<Class<?>, Class<?>>();

    private static final String DEFAULT_PREFIX = "BuddyProxy$";

    public static <T> Class<?> build(Class<T> origin, String name, MethodInclusion methodInclusion, Object interceptor) {
        DynamicType.Builder<T> builder = new ByteBuddy()
                .subclass(origin).name(proxyClassName(name));
        Class<?> cachedClass = classCache.get(origin);
        if (cachedClass != null) {
            return cachedClass;
        }
        Class<? extends T> proxied = builder
                .method(methodInclusion.getIncludes())
                .intercept(MethodDelegation.to(interceptor))
                .make()
                .load(Thread.currentThread().getContextClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
        classCache.putIfAbsent(origin, proxied);
        return proxied;
    }

    private static String proxyClassName(String name) {
        return DEFAULT_PREFIX + name;
    }
}
