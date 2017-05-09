package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.remoting.message.Response;
import com.alibaba.dubbo.rpc.RpcResult;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * @author Xs.
 */
public class DubboResponse {

    private final Response response;

    private RpcResult result;

    public DubboResponse(Response response) {
        this.response = response;
        if (response.getResult() instanceof RpcResult) {
            result = (RpcResult) response.getResult();
        }
    }

    public boolean returnOK() {
        return response.getStatus() == Response.OK
                && !result.hasException();
    }

    public String exceptionMessage() {
        if (returnOK()) {
            return "response OK";
        }
        Throwable throwable = result.getException();
        if (throwable != null) {
            return ExceptionUtils.getStackTrace(throwable);
        }
        return "response " + response.getStatus();
    }
}
