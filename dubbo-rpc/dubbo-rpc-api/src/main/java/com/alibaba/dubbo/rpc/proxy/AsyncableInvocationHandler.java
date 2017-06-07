package com.alibaba.dubbo.rpc.proxy;

import com.alibaba.dubbo.common.utils.MethodCache;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.async.AsyncCommand;

import java.lang.reflect.Method;
import java.util.concurrent.Future;

public class AsyncableInvocationHandler extends InvokerInvocationHandler {

    private static final MethodCache methodCache = MethodCache.newCache();

    public AsyncableInvocationHandler(Invoker<?> handler) {
        super(handler);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!isAsyncMethod(method)) {
            return invokeSuper(proxy, method, args);
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
    class AsyncMethodCommand extends AsyncCommand<Object> {

        private final Object proxy;

        private final Method asyncMethod;

        private final Object[] args;

        public AsyncMethodCommand(Object proxy, Method asyncMethod, Object[] args) {
            this.proxy = proxy;
            this.asyncMethod = asyncMethod;
            this.args = args;
        }

        private Method getCorrespondingSyncMethod() throws Exception {
            String methodName = asyncMethod.getName();
            Class<?>[] parameterTypes = asyncMethod.getParameterTypes();
            String syncMethodName = methodName.substring(methodName.indexOf("async_") + "async_".length());
            return methodCache.get(proxy.getClass(), syncMethodName, parameterTypes);
        }

        @Override
        public Object run() throws Exception {
            return AsyncableInvocationHandler.this.invokeSuper(proxy, getCorrespondingSyncMethod(), args);
        }
    }

    private Object invokeSuper(Object proxy, Method method, Object[] args) {
        try {
            return super.invoke(proxy, method, args);
        } catch (Throwable e) {
            throw new RpcException(e.getMessage(), e);
        }
    }
}
