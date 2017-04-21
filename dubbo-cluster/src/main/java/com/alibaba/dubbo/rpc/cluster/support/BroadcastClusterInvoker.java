/*
 * Copyright 1999-2012 Alibaba Group.
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
 * BroadcastClusterInvoker
 *
 * @author william.liangf
 * @author Xs
 */
public class BroadcastClusterInvoker<T> extends AbstractClusterInvoker<T> {

    private static final Logger logger = LoggerFactory.getLogger(BroadcastClusterInvoker.class);

    public BroadcastClusterInvoker(Directory<T> directory) {
        super(directory);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Result doInvoke(final Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadbalance) throws RpcException {
        checkInvokers(invokers, invocation);
        RpcContext.getContext().setInvokers((List) invokers);
        return doInvokeAsync(invocation, invokers);
    }

    private Result doInvokeSync(final Invocation invocation, final List<Invoker<T>> invokers) {
        RpcException exception = null;
        Result result = null;
        for (Invoker<T> invoker : invokers) {
            try {
                result = invoker.invoke(invocation);
            } catch (RpcException e) {
                exception = e;
                logger.warn(e.getMessage(), e);
            } catch (Throwable e) {
                exception = new RpcException(e.getMessage(), e);
                logger.warn(e.getMessage(), e);
            }
        }
        if (exception != null) {
            throw exception;
        }
        return result;
    }

    private Result doInvokeAsync(final Invocation invocation, final List<Invoker<T>> invokers) {
        CountDownLatch latch = new CountDownLatch(invokers.size());
        AsyncResultProcessor asyncResultProcessor = new AsyncResultProcessor(invokers.size());
        for (final Invoker<T> invoker : invokers) {
            AsyncInvocation asyncInvocation = new AsyncInvocation(invoker, invocation, latch);
            AsyncContext<Result> asyncContext = asyncInvocation.startAsync();
            asyncContext.addListener(asyncInvocation);
            asyncContext.start();
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


    private class AsyncInvocation implements AsyncTarget<Result>, AsyncListener<Result> {

        private final Invoker<T> invoker;

        private final Invocation invocation;

        private final CountDownLatch latch;

        private Result reVal;

        private RpcException exception;

        public AsyncInvocation(Invoker<T> invoker, Invocation invocation, CountDownLatch latch) {
            this.invoker = invoker;
            this.invocation = invocation;
            this.latch = latch;
        }

        @Override
        public AsyncContext<Result> startAsync() {
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
                if (invocation.getException() != null) {
                    exception = invocation.getException();
                    break;
                }
            }
            if (exception == null) {
                result = asyncInvocations.get(0).getResult();
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