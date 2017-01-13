package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;

public class RpcResponse {

    private final Invoker invoker;

    private final Result result;

    public RpcResponse(Invoker invoker, Result result) {
        this.invoker = invoker;
        this.result = result;
    }

    public Invoker getInvoker() {
        return invoker;
    }

    public Result getResult() {
        return result;
    }
}
