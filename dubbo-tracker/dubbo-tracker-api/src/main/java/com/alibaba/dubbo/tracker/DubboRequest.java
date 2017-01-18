package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.remoting.exchange.Request;
import com.alibaba.dubbo.rpc.RpcInvocation;

public class DubboRequest {

    private final Request request;

    private RpcInvocation invocation;

    private boolean isTraceable;

    public DubboRequest(Request request) {
        this.request = request;
        if (request.getData() instanceof RpcInvocation) {
            isTraceable = true;
            invocation = (RpcInvocation) request.getData();
        }
    }

    public String getMethodName() {
        return invocation.getMethodName();
    }

    public boolean isTraceable() {
        return isTraceable;
    }

    public void addAttachment(String key, String value) {
        invocation.setAttachment(key, value);
    }

    public String getAttachment(String key){
        return invocation.getAttachment(key);
    }

    public String providerAddress() {
        return invocation.getInvoker().getUrl().getAddress();
    }

    public Request getRequest() {
        return request;
    }
}
