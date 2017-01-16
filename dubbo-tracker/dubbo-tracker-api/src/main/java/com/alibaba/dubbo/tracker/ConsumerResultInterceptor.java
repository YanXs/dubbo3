package com.alibaba.dubbo.tracker;

/**
 * @author Xs
 */
public interface ConsumerResultInterceptor {

    void handle(RpcResponse response);

}
