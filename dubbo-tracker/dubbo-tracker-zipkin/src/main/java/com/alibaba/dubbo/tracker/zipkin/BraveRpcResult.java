package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.rpc.RpcResult;

public class BraveRpcResult {

    private final RpcResult rpcResult;

    public BraveRpcResult(RpcResult rpcResult) {
        this.rpcResult = rpcResult;
    }

    public boolean hasException() {
        return rpcResult.hasException();
    }

    public String exception() {
        if (!hasException()) {
            return "result OK";
        }
        Throwable throwable = rpcResult.getException();
        if (throwable instanceof NullPointerException) {
            return "NullPointerException";
        }
        return throwable.getMessage();
    }
}
