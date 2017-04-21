package com.alibaba.dubbo.rpc;

public interface AsyncTarget<T> {

    AsyncContext<T> startAsync();

    T invoke() throws Exception;
}
