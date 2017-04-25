package com.alibaba.dubbo.rpc;

import com.alibaba.dubbo.common.utils.Assert;
import com.alibaba.dubbo.common.utils.NamedThreadFactory;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * @author Xs
 */
public class AsyncContext<T> {

    private static final ListeningExecutorService asyncExecutor =
            MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(32, new NamedThreadFactory("asyncContext", Boolean.TRUE)));

    private final AsyncTarget<T> asyncTarget;

    private final List<AsyncListener> listeners = new ArrayList<AsyncListener>();

    private volatile boolean started;

    public AsyncContext(AsyncTarget<T> asyncTarget) {
        Assert.notNull(asyncTarget, "asyncTarget must not be null");
        this.asyncTarget = asyncTarget;
    }

    public ListenableFuture<T> start() {
        ListenableFuture<T> future = asyncExecutor.submit(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return asyncTarget.invoke();
            }
        });
        for (AsyncListener asyncListener : listeners) {
            Futures.addCallback(future, asyncListener);
        }
        started = true;
        return future;
    }

    public static <T> ListenableFuture<T> start(Callable<T> callable) {
        return asyncExecutor.submit(callable);
    }

    public static void addListener(ListenableFuture future, AsyncListener asyncListener) {
        Futures.addCallback(future, asyncListener);
    }

    public AsyncContext addListener(AsyncListener listener) {
        check();
        listeners.add(listener);
        return this;
    }

    private void check() {
        if (started) {
            throw new IllegalStateException("listener should be added before context started.");
        }
    }
}
