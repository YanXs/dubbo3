package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcResult;

public class RpcResponse {

    private final Invoker invoker;

    private final Result result;

    public RpcResponse(Invoker invoker, Result result) {
        this.invoker = invoker;
        this.result = result;
    }

    public Invoker getInvoker() {
        return invoker;
    }

    public Result getResult() {
        return result;
    }

    public boolean isTraceableResponse() {
        return result instanceof RpcResult;
    }

    public boolean returnSuccessfully() {
        return !result.hasException();
    }

    public String exceptionMessage(){
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
