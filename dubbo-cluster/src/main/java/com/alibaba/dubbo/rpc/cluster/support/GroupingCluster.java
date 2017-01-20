package com.alibaba.dubbo.rpc.cluster.support;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.cluster.Cluster;
import com.alibaba.dubbo.rpc.cluster.Directory;

public class GroupingCluster implements Cluster {

    public final static String NAME = "grouping";

    @Override
    public <T> Invoker<T> join(Directory<T> directory) throws RpcException {
        return new GroupingClusterInvoker<T>(directory);
    }
}
