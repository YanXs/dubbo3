package com.alibaba.dubbo.rpc.async;

import com.alibaba.dubbo.common.utils.Assert;
import com.google.common.util.concurrent.Futures;

/**
 * @author Xs
 */
public abstract class ListenableAsyncCommand<T> extends AsyncCommand<T> {

    private CommandListener<T> listener;

    public void submit() {
        if (listener != null) {
            Futures.addCallback(queue(), listener);
        } else {
            throw new IllegalStateException("listener must not be null, you can use queue() instead");
        }
    }

    public void submitWithListener(CommandListener<T> listener) {
        Assert.notNull(listener, "listener must be null");
        this.listener = listener;
        this.submit();
    }

    public ListenableAsyncCommand addListener(CommandListener<T> listener) {
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
