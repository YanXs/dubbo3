package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.rpc.RpcContext;

/**
 * @author Xs
 */
public class DubboRequestSpanNameProvider {

    public String spanName() {
        return RpcContext.getContext().getMethodName();
    }
}
