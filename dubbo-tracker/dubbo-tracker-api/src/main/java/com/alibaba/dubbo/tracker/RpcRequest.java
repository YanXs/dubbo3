package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;

public class RpcRequest {

    private final Invoker invoker;

    private final Invocation invocation;

    public RpcRequest(Invoker invoker, Invocation invocation) {
        this.invoker = invoker;
        this.invocation = invocation;
    }

    public Invoker getInvoker() {
        return invoker;
    }

    public Invocation getInvocation() {
        return invocation;
    }

}
