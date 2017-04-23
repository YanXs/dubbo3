package com.alibaba.dubbo.rpc;

public interface AsyncTarget<T> {

    AsyncContext<T> async();

    T invoke() throws Exception;
}
