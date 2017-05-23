package com.alibaba.dubbo.rpc.async;

import com.alibaba.dubbo.common.utils.NamedThreadFactory;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * @author Xs
 */
public class CommandExecutorFactory {

    private static final Map<String, CommandExecutor> threadPools = new ConcurrentHashMap<String, CommandExecutor>();

    @SuppressWarnings("unchecked")
    public static <T> CommandExecutor<T> getCommandExecutor(String key, int coreSize) {
        CommandExecutor<T> previouslyCached = threadPools.get(key);
        if (previouslyCached != null) {
            return previouslyCached;
        }
        synchronized (CommandExecutorFactory.class) {
            if (!threadPools.containsKey(key)) {
                CommandExecutor commandExecutor = new CommandExecutor(MoreExecutors.listeningDecorator(
                        Executors.newFixedThreadPool(coreSize,
                                new NamedThreadFactory("asyncCommand", Boolean.TRUE))));
                threadPools.put(key, commandExecutor);
            }
        }
        return threadPools.get(key);
    }

    public static CommandExecutorFactory getInstance() {
        return Holder.INSTANCE;
    }

    public static class Holder {
        public static CommandExecutorFactory INSTANCE = new CommandExecutorFactory();
    }
}
