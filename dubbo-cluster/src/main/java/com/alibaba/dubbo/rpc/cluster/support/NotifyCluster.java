package com.alibaba.dubbo.rpc.cluster.support;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.cluster.Cluster;
import com.alibaba.dubbo.rpc.cluster.Directory;

/**
 * @author Xs
 */
public class NotifyCluster implements Cluster{

    public final static String NAME = "notify";

    @Override
    public <T> Invoker<T> join(Directory<T> directory) throws RpcException {
        return new NotifyClusterInvoker<T>(directory);
    }
}
