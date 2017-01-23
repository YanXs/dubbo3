package com.alibaba.dubbo.tracker;


import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.remoting.message.Request;
import com.alibaba.dubbo.rpc.Invoker;
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

    public String getAttachment(String key) {
        return invocation.getAttachment(key);
    }

    public String address() {
        Invoker invoker = invocation.getInvoker();
        if (invoker != null) {
            return invoker.getUrl().getAddress();
        }
//        if (invocation instanceof DecodeableRpcInvocation){
//            Channel channel = ((DecodeableRpcInvocation) invocation).getChannel();
//            return channel.getUrl().getAddress();
//        }
        return NetUtils.getLocalHost();
    }

    public Request getRequest() {
        return request;
    }
}
