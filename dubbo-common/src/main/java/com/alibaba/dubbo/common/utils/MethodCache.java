package com.alibaba.dubbo.common.utils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodCache {

    private final Map<String, Method> cachedMethods = new ConcurrentHashMap<String, Method>(32);

    private final Object lock = new Object();

    private MethodCache() {
    }

    public static MethodCache newCache() {
        return new MethodCache();
    }

    private String methodKey(String methodName, Class<?>[] parameterTypes) {
        StringBuilder builder = new StringBuilder(methodName);
        if (parameterTypes != null && parameterTypes.length > 0) {
            for (Class<?> clz : parameterTypes) {
                builder.append(clz.getName());
            }
        }
        return builder.toString();
    }

    public Method get(Object object, String methodName, Class<?>[] parameterTypes) throws Exception {
        String methodKey = methodKey(methodName, parameterTypes);
        Method previouslyCached = cachedMethods.get(methodKey);
        if (previouslyCached != null) {
            return previouslyCached;
        }
        synchronized (lock) {
            if (!cachedMethods.containsKey(methodKey)) {
                Method method = object.getClass().getDeclaredMethod(methodName, parameterTypes);
                cachedMethods.put(methodKey, method);
            }
        }
        return cachedMethods.get(methodKey);
    }
}
