package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.rpc.protocol.dubbo.DecodeableRpcResult;

public class BraveRpcResult {

    private final DecodeableRpcResult rpcResult;

    public BraveRpcResult(DecodeableRpcResult rpcResult) {
        this.rpcResult = rpcResult;
    }
}
