package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.rpc.Invocation;

/**
 * @author Xs
 */
public class DubboSpanNameProvider {

    public String spanName(Invocation invocation) {
        return invocation.getMethodName();
    }
}
