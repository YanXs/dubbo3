package com.alibaba.dubbo.tracker;

/**
 * @author Xs
 */
public interface ConsumerInvocationInterceptor {

    void handle(RpcRequest request);
}
