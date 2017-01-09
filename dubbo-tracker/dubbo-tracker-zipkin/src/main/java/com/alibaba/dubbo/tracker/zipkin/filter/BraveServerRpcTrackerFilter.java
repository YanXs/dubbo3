package com.alibaba.dubbo.tracker.zipkin.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.tracker.RpcTrackerFactory;
import com.alibaba.dubbo.tracker.filter.ServerRpcTrackerFilter;

/**
 * @author Xs
 */
@Activate(group = Constants.PROVIDER, order = Integer.MIN_VALUE)
public class BraveServerRpcTrackerFilter implements ServerRpcTrackerFilter {

    private RpcTrackerFactory rpcTrackerFactory;

    public void setRpcTrackerFactory(RpcTrackerFactory rpcTrackerFactory) {
        this.rpcTrackerFactory = rpcTrackerFactory;
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        return invoker.invoke(invocation);
    }
}
