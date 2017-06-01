/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.rpc.proxy.jdk;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.proxy.AbstractProxyFactory;
import com.alibaba.dubbo.rpc.proxy.AbstractProxyInvoker;
import com.alibaba.dubbo.rpc.proxy.AsyncableInvocationHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JdkProxyFactory
 *
 * @author william.liangf
 */
public class JdkProxyFactory extends AbstractProxyFactory {

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                interfaces, new AsyncableInvocationHandler(invoker));
    }

    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) {
        return new CachedProxyInvoker<T>(proxy, type, url);
    }

    private static class CachedProxyInvoker<T> extends AbstractProxyInvoker<T> {

        static Map<String, Method> cachedMethods = new ConcurrentHashMap<String, Method>(128);

        public CachedProxyInvoker(T proxy, Class<T> type, URL url) {
            super(proxy, type, url);
        }

        @Override
        protected Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments) throws Throwable {
            String methodKey = methodKey(methodName, parameterTypes);
            Method previouslyCached = cachedMethods.get(methodKey);
            if (previouslyCached != null) {
                return previouslyCached;
            }
            synchronized (CachedProxyInvoker.class) {
                if (!cachedMethods.containsKey(methodKey)) {
                    Method method = proxy.getClass().getDeclaredMethod(methodName, parameterTypes);
                    cachedMethods.put(methodKey, method);
                }
            }
            return cachedMethods.get(methodKey).invoke(proxy, arguments);
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
    }
}