package com.alibaba.dubbo.rpc;

/**
 * @author Xs
 */
public interface AsyncInvocation<T> extends AsyncTarget<T>, AsyncListener<T> {

    RpcException getException();

    T getResult();
}
