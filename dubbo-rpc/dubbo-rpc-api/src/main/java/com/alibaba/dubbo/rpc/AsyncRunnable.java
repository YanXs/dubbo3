package com.alibaba.dubbo.rpc;

public interface AsyncRunnable<T> {

    AsyncCommand<T> async();

    T run() throws Exception;
}
