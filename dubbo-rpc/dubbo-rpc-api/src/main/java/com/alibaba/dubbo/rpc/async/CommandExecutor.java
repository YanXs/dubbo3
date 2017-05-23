package com.alibaba.dubbo.rpc.async;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

import java.util.concurrent.Callable;

/**
 * @author Xs
 */
public class CommandExecutor<T> {

    private final ListeningExecutorService commandExecutor;

    public CommandExecutor(ListeningExecutorService commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public ListenableFuture<T> executeCommand(Callable<T> callable) {
        return commandExecutor.submit(callable);
    }
}
