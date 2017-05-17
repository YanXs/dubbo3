package com.alibaba.dubbo.rpc.proxy;

import com.alibaba.dubbo.rpc.AsyncCommand;
import com.alibaba.dubbo.rpc.AsyncTarget;
import com.alibaba.dubbo.rpc.Invoker;

import java.lang.reflect.Method;
import java.util.concurrent.Future;

public class AsyncableInvocationHandler extends InvokerInvocationHandler {

    public AsyncableInvocationHandler(Invoker<?> handler) {
        super(handler);
    }

    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        if (!isAsyncMethod(method)) {
            return super.invoke(proxy, method, args);
        } else {
            AsyncTarget asyncTarget = new AsyncMethodWrapper(proxy, method, args);
            return asyncTarget.async().execute();
        }
    }

    private boolean isAsyncMethod(Method method) {
        return Future.class.isAssignableFrom(method.getReturnType()) &&
                method.getName().startsWith("async_");
    }

    /**
     * async method wrapper
     */
    class AsyncMethodWrapper implements AsyncTarget<Object> {

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

        public AsyncCommand<Object> async() {
            return new AsyncCommand<Object>(this);
        }

        @Override
        public Object run() throws Exception {
            return getCorrespondingSyncMethod().invoke(proxy, args);
        }
    }
}
