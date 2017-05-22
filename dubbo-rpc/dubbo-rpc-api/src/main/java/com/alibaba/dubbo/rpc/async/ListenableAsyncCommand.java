package com.alibaba.dubbo.rpc.async;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * @author Xs
 */
public abstract class ListenableAsyncCommand<T> extends AsyncCommand<T> {

    private AsyncListener<T> listener;

    public void queue() {
        checkState();
        ListenableFuture<T> future = submit();
        if (listener != null) {
            Futures.addCallback(future, listener);
        }

    }

    public ListenableAsyncCommand addListener(AsyncListener<T> listener) {
        check();
        this.listener = listener;
        return this;
    }


    private void check() {
        if (state.get() != State.LATENT) {
            throw new IllegalStateException("listener should be added before command started.");
        }
    }
}
