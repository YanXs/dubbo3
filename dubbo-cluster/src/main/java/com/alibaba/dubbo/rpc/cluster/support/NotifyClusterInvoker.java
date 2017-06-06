package com.alibaba.dubbo.rpc.cluster.support;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.async.CommandListener;
import com.alibaba.dubbo.rpc.async.ListenableAsyncCommand;
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

    private Result doInvokeAsync(final Invocation invocation, List<Invoker<T>> invokers) {
        CountDownLatch latch = new CountDownLatch(invokers.size());
        AsyncResultProcessor asyncResultProcessor = new AsyncResultProcessor(invokers.size());
        for (final Invoker<T> invoker : invokers) {
            ListenableAsyncCommand<Result> command = new AsyncInvocationCommand(invoker, invocation);
            AsyncResult result = new AsyncResult(latch);
            command.addListener(result).submit();
            asyncResultProcessor.addAsyncResult(result);
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

    private class AsyncInvocationCommand extends ListenableAsyncCommand<Result> {

        private final Invoker<T> invoker;

        private final Invocation invocation;

        public AsyncInvocationCommand(Invoker<T> invoker, Invocation invocation) {
            this.invoker = invoker;
            this.invocation = invocation;
        }

        @Override
        public Result run() throws Exception {
            return doInvoke(invoker, invocation);
        }
    }

    private class AsyncResult implements CommandListener<Result> {

        private final CountDownLatch latch;

        private Result reVal;

        private RpcException exception;

        private AsyncResult(CountDownLatch latch) {
            this.latch = latch;
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

        private final List<AsyncResult> asyncResult;

        private RpcException exception;

        private Result result;

        public AsyncResultProcessor(int size) {
            asyncResult = new ArrayList<AsyncResult>(size);
        }

        public void addAsyncResult(AsyncResult asyncResult) {
            this.asyncResult.add(asyncResult);
        }

        void processResult() {
            for (AsyncResult invocation : asyncResult) {
                exception = invocation.getException();
                if (exception != null) {
                    break;
                }
            }
            if (exception == null) {
                result = asyncResult.get(0).getResult();
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
