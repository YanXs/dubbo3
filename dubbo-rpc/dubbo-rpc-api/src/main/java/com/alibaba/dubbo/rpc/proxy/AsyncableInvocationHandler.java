package com.alibaba.dubbo.rpc.proxy;

import com.alibaba.dubbo.common.utils.MethodCache;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.async.AsyncCommand;

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
            AsyncCommand command = new AsyncMethodCommand(proxy, method, args);
            return command.queue();
        }
    }

    private boolean isAsyncMethod(Method method) {
        return Future.class.isAssignableFrom(method.getReturnType()) &&
                method.getName().startsWith("async_");
    }

    /**
     * async method wrapper
     */
    static class AsyncMethodCommand extends AsyncCommand<Object> {

        private static final MethodCache methodCache = MethodCache.newCache();

        private final Object proxy;

        private final Method wrapped;

        private final Object[] args;

        public AsyncMethodCommand(Object proxy, Method wrapped, Object[] args) {
            this.proxy = proxy;
            this.wrapped = wrapped;
            this.args = args;
        }

        private Method getCorrespondingSyncMethod() throws Exception {
            String methodName = wrapped.getName();
            Class<?>[] parameterTypes = wrapped.getParameterTypes();
            String syncMethodName = methodName.substring(methodName.indexOf("async_") + "async_".length());
            return methodCache.get(wrapped, syncMethodName, parameterTypes);
        }

        @Override
        public Object run() throws Exception {
            return getCorrespondingSyncMethod().invoke(proxy, args);
        }
    }
}
