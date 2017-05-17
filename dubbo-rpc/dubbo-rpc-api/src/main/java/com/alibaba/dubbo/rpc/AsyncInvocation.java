package com.alibaba.dubbo.rpc;

/**
 * @author Xs
 */
public interface AsyncInvocation<T> extends AsyncRunnable<T>, AsyncListener<T> {

    RpcException getException();

    T getResult();
}
