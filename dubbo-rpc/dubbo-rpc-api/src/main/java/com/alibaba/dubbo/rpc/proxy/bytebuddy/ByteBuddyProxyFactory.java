package com.alibaba.dubbo.rpc.proxy.bytebuddy;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.proxy.AbstractProxyFactory;
import com.alibaba.dubbo.rpc.proxy.AbstractProxyInvoker;
import com.alibaba.dubbo.rpc.proxy.AsyncableInvocationHandler;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.Callable;

import static net.bytebuddy.matcher.ElementMatchers.not;

/**
 * @author Xs
 */
public class ByteBuddyProxyFactory extends AbstractProxyFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                interfaces, new AsyncableInvocationHandler(invoker));
    }

    @Override
    public <T> Invoker<T> getInvoker(T proxied, Class<T> type, URL url) {
        Class<?> proxyClass = ProxyClassBuilder.build(proxied.getClass(),
                proxied.getClass().getSimpleName(), ProxyMethodInclusion.INSTANCE, new ProxyInterceptor());
        final Object proxy = createNewInstance(proxyClass);
        return new AbstractProxyInvoker<T>(proxied, type, url) {
            @Override
            protected Object doInvoke(T proxied, String methodName,
                                      Class<?>[] parameterTypes,
                                      Object[] arguments) throws Throwable {
                Method method = proxy.getClass().getMethod(methodName, parameterTypes);
                return method.invoke(proxy, arguments);
            }
        };
    }

    private Object createNewInstance(Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RpcException(e);
        }
    }

    public class ProxyInterceptor {

        @RuntimeType
        public Object intercept(@SuperCall Callable<?> superMethod) {
            try {
                return superMethod.call();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RpcException(e);
            }
        }
    }

    public enum ProxyMethodInclusion implements MethodInclusion {
        INSTANCE;

        @Override
        public ElementMatcher<MethodDescription> getIncludes() {
            return not(ElementMatchers.<MethodDescription>isPrivate()
                    .or(ObjectMethodElementMatchers.INSTANCE));
        }
    }
}
