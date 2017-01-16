package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcInvocation;

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

    public boolean isTraceableRequest() {
        return invocation instanceof RpcInvocation;
    }

    public String getMethodName() {
        return invocation.getMethodName();
    }

    public void addAttachment(String key, String value) {
        ((RpcInvocation) invocation).setAttachment(key, value);
    }

    public String getAttachment(String key) {
        return invocation.getAttachment(key);
    }

    public String providerAddress() {
        return invoker.getUrl().getAddress();
    }
}
