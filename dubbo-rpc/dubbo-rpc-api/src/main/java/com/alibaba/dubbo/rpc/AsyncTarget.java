package com.alibaba.dubbo.rpc;

public interface AsyncTarget<T> {

    AsyncCommand<T> async();

    T run() throws Exception;
}
