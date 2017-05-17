package com.alibaba.dubbo.rpc;

import com.alibaba.dubbo.common.utils.Assert;
import com.alibaba.dubbo.common.utils.NamedThreadFactory;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Xs
 */
public class AsyncCommand<T> {

    private static final ListeningExecutorService asyncCommandExecutor =
            MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(32, new NamedThreadFactory("asyncCommand", Boolean.TRUE)));

    private final AsyncRunnable<T> asyncRunnable;

    private final Set<AsyncListener> listeners = new HashSet<AsyncListener>(8);

    private final AtomicReference<State> state = new AtomicReference<State>(State.LATENT);

    private enum State {
        LATENT,
        STARTED,
        FINISHED
    }

    public AsyncCommand(AsyncRunnable<T> asyncRunnable) {
        Assert.notNull(asyncRunnable, "asyncTarget must not be null");
        this.asyncRunnable = asyncRunnable;
    }

    public ListenableFuture<T> execute() {
        Preconditions.checkState(state.compareAndSet(State.LATENT, State.STARTED), "command could be execute more than once");
        ListenableFuture<T> future = asyncCommandExecutor.submit(new Callable<T>() {
            @Override
            public T call() throws Exception {
                try {
                    return asyncRunnable.run();
                } finally {
                    state.compareAndSet(State.STARTED, State.FINISHED);
                }
            }
        });
        for (AsyncListener asyncListener : listeners) {
            Futures.addCallback(future, asyncListener);
        }
        return future;
    }

    public AsyncCommand addListener(AsyncListener listener) {
        check();
        listeners.add(listener);
        return this;
    }

    private void check() {
        if (state.get() != State.LATENT) {
            throw new IllegalStateException("listener should be added before command started.");
        }
    }
}
