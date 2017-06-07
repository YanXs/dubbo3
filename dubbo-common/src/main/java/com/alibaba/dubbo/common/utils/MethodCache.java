package com.alibaba.dubbo.common.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodCache {

    private final Map<Integer, Method> cachedMethods = new ConcurrentHashMap<Integer, Method>(32);

    private final Object lock = new Object();

    private MethodCache() {
    }

    public static MethodCache newCache() {
        return new MethodCache();
    }

    private Integer methodKey(Class<?> targetClass, String methodName, Class<?>[] parameterTypes) {
        return methodName.hashCode() + Arrays.hashCode(parameterTypes) + targetClass.hashCode();
    }

    public Method get(Class<?> targetClass, String methodName, Class<?>[] parameterTypes) throws Exception {
        Assert.notNull(targetClass, "object must not be null!");
        if (StringUtils.isEmpty(methodName)) {
            throw new IllegalArgumentException("methodName must not be null or empty");
        }
        Integer methodKey = methodKey(targetClass, methodName, parameterTypes);
        Method previouslyCached = cachedMethods.get(methodKey);
        if (previouslyCached != null) {
            return previouslyCached;
        }
        synchronized (lock) {
            if (!cachedMethods.containsKey(methodKey)) {
                Method method = targetClass.getDeclaredMethod(methodName, parameterTypes);
                cachedMethods.put(methodKey, method);
            }
        }
        return cachedMethods.get(methodKey);
    }
}
