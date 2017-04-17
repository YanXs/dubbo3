package com.alibaba.dubbo.rpc;

import com.alibaba.dubbo.common.utils.NamedThreadFactory;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Xs
 */
public class AsyncContext {

    private static final ListeningExecutorService asyncExecutor =
            MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(16, new NamedThreadFactory("asyncContext", Boolean.TRUE)));

    private final AsyncMethodWrapper asyncMethodWrapper;

    public AsyncContext(AsyncMethodWrapper asyncMethodWrapper) {
        this.asyncMethodWrapper = asyncMethodWrapper;
    }

    public Future start() {
        return asyncExecutor.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return asyncMethodWrapper.invokeSyncMethod();
            }
        });
    }

    /**
     * async method wrapper
     */
    public static class AsyncMethodWrapper {

        private final Object proxy;

        private final Method wrapped;

        private final Object[] args;

        public AsyncMethodWrapper(Object proxy, Method wrapped, Object[] args) {
            this.proxy = proxy;
            this.wrapped = wrapped;
            this.args = args;
        }

        private Method getCorrespondingSyncMethod() throws Exception {
            String methodName = wrapped.getName();
            Class<?>[] parameterTypes = wrapped.getParameterTypes();
            String syncMethodName = methodName.substring(methodName.indexOf("async_") + "async_".length());
            return proxy.getClass().getDeclaredMethod(syncMethodName, parameterTypes);
        }

        public Object invokeSyncMethod() throws Exception {
            return getCorrespondingSyncMethod().invoke(proxy, args);
        }

        public AsyncContext startAsync() {
            return new AsyncContext(this);
        }
    }
}
