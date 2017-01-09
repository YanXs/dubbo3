package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.remoting.exchange.Request;
import com.alibaba.dubbo.rpc.RpcInvocation;

/**
 * @author Xs
 */
public class DecodeableRequest {

    private final RpcInvocation invocation;

    public DecodeableRequest(Request request) {
        this.invocation = (RpcInvocation) request.getData();
    }

    public RpcInvocation getInvocation() {
        return invocation;
    }

    public String getProviderAddress() {
        return invocation.getInvoker().getUrl().getAddress();
    }

    public void addAttachment(String key, String value) {
        invocation.setAttachment(key, value);
    }
}
