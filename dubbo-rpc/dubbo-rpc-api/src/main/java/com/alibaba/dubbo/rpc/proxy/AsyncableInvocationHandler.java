package com.alibaba.dubbo.rpc.proxy;

import com.alibaba.dubbo.rpc.AsyncContext;
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
            AsyncContext.AsyncMethodWrapper methodWrapper = new AsyncContext.AsyncMethodWrapper(proxy, method, args);
            AsyncContext asyncContext = methodWrapper.startAsync();
            return asyncContext.start();
        }
    }

    private boolean isAsyncMethod(Method method) {
        return Future.class.isAssignableFrom(method.getReturnType()) &&
                method.getName().startsWith("async_");
    }
}
