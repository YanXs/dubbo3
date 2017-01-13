package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcInvocation;

public class BraveRpcInvocation {

    private final RpcInvocation rpcInvocation;

    public BraveRpcInvocation(RpcInvocation rpcInvocation) {
        this.rpcInvocation = rpcInvocation;
    }

    public void addAttachment(String key, String value) {
        rpcInvocation.setAttachment(key, value);
    }


    public String getAttachment(String key) {
        return rpcInvocation.getAttachment(key);
    }

    public String providerAddress() {
        String address;
        Invoker invoker = rpcInvocation.getInvoker();
        if (invoker == null){

        }
        return rpcInvocation.getInvoker().getUrl().getAddress();
    }

    public RpcInvocation getRpcInvocation() {
        return rpcInvocation;
    }
}
