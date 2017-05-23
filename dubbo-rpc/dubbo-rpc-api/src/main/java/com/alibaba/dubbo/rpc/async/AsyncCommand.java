package com.alibaba.dubbo.rpc.async;

import com.alibaba.dubbo.common.utils.NamedThreadFactory;
import com.alibaba.dubbo.rpc.RpcException;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Xs
 */
public abstract class AsyncCommand<T> {

    private static final ListeningExecutorService asyncCommandExecutor =
            MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(32, new NamedThreadFactory("asyncCommand", Boolean.TRUE)));

    protected final AtomicReference<State> state = new AtomicReference<State>(State.LATENT);

    protected enum State {
        LATENT, STARTED, FINISHED
    }

    public ListenableFuture<T> queue() {
        checkState();
        return asyncCommandExecutor.submit(new Callable<T>() {
            @Override
            public T call() throws Exception {
                try {
                    return run();
                } finally {
                    state.compareAndSet(State.STARTED, State.FINISHED);
                }
            }
        });
    }

    public T execute() {
        try {
            return queue().get();
        } catch (Exception e) {
            throw new RpcException(e);
        }
    }

    protected void checkState() {
        Preconditions.checkState(state.compareAndSet(State.LATENT, State.STARTED), "command could not be executed more than once");
    }

    public abstract T run() throws Exception;
}
