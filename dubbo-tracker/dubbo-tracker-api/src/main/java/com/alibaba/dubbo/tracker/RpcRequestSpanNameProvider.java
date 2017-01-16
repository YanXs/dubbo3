package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.rpc.Invocation;

/**
 * @author Xs
 */
public class RpcRequestSpanNameProvider {

    public String spanName(RpcRequest rpcRequest) {
        return rpcRequest.getMethodName();
    }
}
