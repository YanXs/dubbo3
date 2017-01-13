package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.rpc.Invocation;

/**
 * @author Xs
 */
public class InvocationSpanNameProvider {

    public String spanName(Invocation invocation) {
        return invocation.getMethodName();
    }
}
