package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.rpc.RpcContext;

public abstract class RpcContextMethodNameProvider {

    public String getMethodNameFromContext() {
        return RpcContext.getContext().getMethodName();
    }
}
