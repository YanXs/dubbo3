package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.remoting.message.Response;
import com.alibaba.dubbo.rpc.RpcResult;

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
        return response.getStatus() == Response.OK && !result.hasException();
    }

    public String exceptionMessage() {
        if (returnOK()) {
            return "response OK";
        }
        Throwable throwable = result.getException();
        if (throwable instanceof NullPointerException) {
            return "NullPointerException";
        }
        return throwable.getMessage();
    }
}
