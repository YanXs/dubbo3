package com.alibaba.dubbo.tracker;


import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.remoting.message.Request;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.alibaba.dubbo.tracker.exceptions.RequestIllegalException;

/**
 * @author Xs.
 */
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

    public String providerAddress() {
        Invoker invoker = invocation.getInvoker();
        if (invoker == null) {
            throw new RequestIllegalException("Invoker must not be null, request { " + request + " }");
        }
        return invoker.getUrl().getAddress();
    }

    public String remoteMethod() {
        String interfaceStr = invocation.getAttachment("interface");
        if (StringUtils.isEmpty(interfaceStr)) {
            return invocation.getMethodName();
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append(interfaceStr).append(".");
            builder.append(invocation.getMethodName()).append("(");
            Class<?>[] parameterTypes = invocation.getParameterTypes();
            int len = parameterTypes.length;
            for (int i = 0; i < len - 1; i++) {
                builder.append(parameterTypes[i].getCanonicalName()).append(" ,");
            }
            if (len > 0) {
                builder.append(parameterTypes[len - 1].getCanonicalName());
            }
            builder.append(")");
            return builder.toString();
        }
    }

    public String serviceVersion() {
        return invocation.getAttachment("version");
    }

    public Request getRequest() {
        return request;
    }
}
