package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.remoting.exchange.Response;
import com.alibaba.dubbo.rpc.RpcResult;

public class DubboResponse {

    private final Response response;

    private boolean isTraceable;

    private volatile RpcResult result;

    public DubboResponse(Response response) {
        this.response = response;
        if (response.getResult() instanceof RpcResult) {
            isTraceable = true;
            result = (RpcResult) response.getResult();
        }
    }

    public boolean returnSuccessfully() {
        return !result.hasException();
    }

    public boolean isTraceable() {
        return isTraceable;
    }

    public String exceptionMessage() {
        if (returnSuccessfully()) {
            return "result OK";
        }
        Throwable throwable = result.getException();
        if (throwable instanceof NullPointerException) {
            return "NullPointerException";
        }
        return throwable.getMessage();
    }
}
