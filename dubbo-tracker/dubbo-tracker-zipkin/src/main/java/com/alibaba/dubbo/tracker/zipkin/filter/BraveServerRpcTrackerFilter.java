package com.alibaba.dubbo.tracker.zipkin.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.tracker.RpcRequest;
import com.alibaba.dubbo.tracker.RpcTracker;
import com.alibaba.dubbo.tracker.TrackerManager;
import com.alibaba.dubbo.tracker.filter.ServerRpcTrackerFilter;

/**
 * @author Xs
 */
@Activate(group = Constants.PROVIDER, order = Integer.MIN_VALUE)
public class BraveServerRpcTrackerFilter implements ServerRpcTrackerFilter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcTracker rpcTracker = TrackerManager.mockRpcTracker("provider");
        rpcTracker.serverRequestInterceptor().handle(new RpcRequest(invoker, invocation));
        Result result = invoker.invoke(invocation);
        rpcTracker.serverResponseInterceptor().handle(result);
        return result;
    }
}
