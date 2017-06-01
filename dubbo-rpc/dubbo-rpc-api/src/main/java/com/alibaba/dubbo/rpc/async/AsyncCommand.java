package com.alibaba.dubbo.rpc.async;

import com.alibaba.dubbo.rpc.RpcException;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Xs
 */
public abstract class AsyncCommand<T> {


    public static final String COMMON_KEY = "common-command";

    public static final int COMMON_CORE_SIZE = 64;

    public static final int UNKNOWN_CORE_SIZE = 10;

    private final CommandExecutor<T> commandExecutor;

    private final CommandExecutorFactory factory = CommandExecutorFactory.getInstance();

    protected final AtomicReference<State> state = new AtomicReference<State>(State.LATENT);

    protected enum State {
        LATENT, STARTED, FINISHED
    }

    public AsyncCommand() {
        this(COMMON_KEY, COMMON_CORE_SIZE);
    }

    public AsyncCommand(String key) {
        this(key, UNKNOWN_CORE_SIZE);
    }

    public AsyncCommand(String key, int coreSize) {
        commandExecutor = initExecutor(key, coreSize);
    }

    private CommandExecutor<T> initExecutor(String key, int coreSize) {
        return factory.getCommandExecutor(key, coreSize);
    }

    public ListenableFuture<T> queue() {
        checkState();
        return commandExecutor.execute(this);
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
