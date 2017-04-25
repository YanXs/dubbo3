package com.alibaba.dubbo.rpc.cluster.support;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.dubbo.rpc.cluster.Directory;
import com.alibaba.dubbo.rpc.cluster.LoadBalance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author Xs
 */
public class NotifyClusterInvoker<T> extends AbstractClusterInvoker<T> {

    private static final Logger logger = LoggerFactory.getLogger(NotifyClusterInvoker.class);

    public NotifyClusterInvoker(Directory<T> directory) {
        super(directory);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Result doInvoke(final Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadbalance) throws RpcException {
        checkInvokers(invokers, invocation);
        return doInvokeAsync(invocation, invokers);
    }

    private Result doInvokeAsync(final Invocation invocation, final List<Invoker<T>> invokers) {
        final CountDownLatch latch = new CountDownLatch(invokers.size());
        AsyncResultProcessor asyncResultProcessor = new AsyncResultProcessor(invokers.size());
        for (final Invoker<T> invoker : invokers) {
            AsyncInvocation<Result> asyncInvocation = new AsyncInvocationImpl(invoker, invocation, latch);
            asyncInvocation.async().start(asyncInvocation);
            asyncResultProcessor.addAsyncInvocation(asyncInvocation);
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RpcException(e.getMessage(), e);
        }
        asyncResultProcessor.processResult();
        if (asyncResultProcessor.getException() != null) {
            throw asyncResultProcessor.getException();
        }
        return asyncResultProcessor.getResult();
    }


    private class AsyncInvocationImpl implements AsyncInvocation<Result> {

        private final Invoker<T> invoker;

        private final Invocation invocation;

        private final CountDownLatch latch;

        private Result reVal;

        private RpcException exception;

        public AsyncInvocationImpl(Invoker<T> invoker, Invocation invocation, CountDownLatch latch) {
            this.invoker = invoker;
            this.invocation = invocation;
            this.latch = latch;
        }

        @Override
        public AsyncContext<Result> async() {
            return new AsyncContext<Result>(this);
        }

        @Override
        public Result invoke() throws Exception {
            return doInvoke(invoker, invocation);
        }

        @Override
        public void onSuccess(Result result) {
            try {
                reVal = result;
            } finally {
                latch.countDown();
            }
        }

        @Override
        public void onFailure(Throwable t) {
            try {
                if (t instanceof RpcException) {
                    exception = (RpcException) t;
                } else {
                    exception = new RpcException(t.getMessage(), t);
                }
            } finally {
                latch.countDown();
            }
        }

        public RpcException getException() {
            return exception;
        }

        public Result getResult() {
            return reVal;
        }
    }

    private class AsyncResultProcessor {

        private final List<AsyncInvocation> asyncInvocations;

        private RpcException exception;

        private Result result;

        public AsyncResultProcessor(int size) {
            asyncInvocations = new ArrayList<AsyncInvocation>(size);
        }

        public void addAsyncInvocation(AsyncInvocation asyncInvocation) {
            asyncInvocations.add(asyncInvocation);
        }

        public void processResult() {
            for (AsyncInvocation invocation : asyncInvocations) {
                exception = invocation.getException();
                if (exception != null) {
                    break;
                }
            }
            if (exception == null) {
                result = (Result) asyncInvocations.get(0).getResult();
            }
        }

        public Result getResult() {
            return result;
        }

        public RpcException getException() {
            return exception;
        }
    }

    private Result doInvoke(Invoker<T> invoker, Invocation invocation) {
        Result result = null;
        RpcException exception = null;
        try {
            result = invoker.invoke(invocation);
        } catch (RpcException e) {
            exception = e;
            logger.warn(e.getMessage(), e);
        } catch (Throwable e) {
            exception = new RpcException(e.getMessage(), e);
            logger.warn(e.getMessage(), e);
        }
        if (exception != null) {
            throw exception;
        }
        return result;
    }
}
