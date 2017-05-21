package com.alibaba.dubbo.rpc.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.netflix.hystrix.*;

/**
 * for test now
 *
 * @author Xs
 */
@Activate(group = Constants.CONSUMER, value = "threads")
public class HystrixFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        return new DefaultCommand(invoker, invocation).execute();
    }

    private static class DefaultCommand extends HystrixCommand<Result> {

        private final Invoker<?> invoker;
        private final Invocation invocation;

        public DefaultCommand(Invoker<?> invoker, Invocation invocation) {
            super(Setter
                    .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey(invoker)))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey(invoker, invocation)))
                    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                            .withCircuitBreakerRequestVolumeThreshold(20)
                            .withCircuitBreakerSleepWindowInMilliseconds(30000)
                            .withCircuitBreakerErrorThresholdPercentage(50)
                            .withExecutionTimeoutEnabled(false))
                    .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(threads(invoker))));
            this.invoker = invoker;
            this.invocation = invocation;
        }

        private static String groupKey(Invoker<?> invoker) {
            return invoker.getInterface().getName() + getVersion(invoker);
        }

        private static String getVersion(Invoker<?> invoker) {
            return invoker.getUrl().getParameter("version", "");
        }

        private static String commandKey(Invoker<?> invoker, Invocation invocation) {
            return String.format("%s_%d", invoker.getInterface().getName() + invocation.getMethodName(),
                    invocation.getArguments() == null ? 0 : invocation.getArguments().length);
        }

        private static int threads(Invoker<?> invoker) {
            return invoker.getUrl().getParameter("threads", 20);
        }

        @Override
        protected Result run() throws Exception {
            return invoker.invoke(invocation);
        }
    }
}
