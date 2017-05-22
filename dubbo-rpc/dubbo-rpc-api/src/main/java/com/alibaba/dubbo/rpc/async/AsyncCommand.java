package com.alibaba.dubbo.rpc.async;

import com.alibaba.dubbo.common.utils.NamedThreadFactory;
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

    protected static final ListeningExecutorService asyncCommandExecutor =
            MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(32, new NamedThreadFactory("asyncCommand", Boolean.TRUE)));

    protected final AtomicReference<State> state = new AtomicReference<State>(State.LATENT);

    protected enum State {
        LATENT,
        STARTED,
        FINISHED
    }

    public ListenableFuture<T> execute() {
        checkState();
        return submit();
    }

    protected void checkState(){
        Preconditions.checkState(state.compareAndSet(State.LATENT, State.STARTED), "command could be execute more than once");
    }

    protected ListenableFuture<T> submit() {
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

    public abstract T run() throws Exception;
}
