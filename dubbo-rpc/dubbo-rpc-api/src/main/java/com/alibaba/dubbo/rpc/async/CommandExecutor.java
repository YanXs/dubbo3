package com.alibaba.dubbo.rpc.async;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

import java.io.Closeable;
import java.util.concurrent.Callable;

/**
 * @author Xs
 */
public class CommandExecutor<T> implements Closeable {

    private final ListeningExecutorService commandExecutor;

    public CommandExecutor(ListeningExecutorService commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public ListenableFuture<T> execute(final AsyncCommand<T> command) {
        return commandExecutor.submit(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return command.run();
            }
        });
    }

    public void close() {
        if (commandExecutor != null) {
            commandExecutor.shutdown();
        }
    }
}
