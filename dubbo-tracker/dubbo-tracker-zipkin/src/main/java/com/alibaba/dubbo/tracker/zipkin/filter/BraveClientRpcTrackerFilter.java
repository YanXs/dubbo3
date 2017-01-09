package com.alibaba.dubbo.tracker.zipkin.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.tracker.RpcTrackerFactory;
import com.alibaba.dubbo.tracker.filter.ClientRpcTrackerFilter;

/**
 * @author Xs
 */
@Activate(group = Constants.CONSUMER, order = Integer.MAX_VALUE)
public class BraveClientRpcTrackerFilter implements ClientRpcTrackerFilter {

    private static final Logger logger = LoggerFactory.getLogger(BraveClientRpcTrackerFilter.class);

    private RpcTrackerFactory rpcTrackerFactory;

    public void setRpcTrackerFactory(RpcTrackerFactory rpcTrackerFactory) {
        this.rpcTrackerFactory = rpcTrackerFactory;
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        return invoker.invoke(invocation);
    }
}
