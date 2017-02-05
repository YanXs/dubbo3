package com.alibaba.dubbo.tracker;

import com.alibaba.dubbo.common.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xs.
 */
public class RpcProtocol {

    public static final RpcProtocol DUBBO = new RpcProtocol("dubbo");
    public static final RpcProtocol HESSIAN = new RpcProtocol("hessian");
    public static final RpcProtocol HTTP = new RpcProtocol("http");

    private static final Map<String, RpcProtocol> PROTOCOL_MAP = new HashMap<String, RpcProtocol>();

    static {
        PROTOCOL_MAP.put(DUBBO.protocol(), DUBBO);
        PROTOCOL_MAP.put(HESSIAN.protocol(), HESSIAN);
        PROTOCOL_MAP.put(HTTP.protocol(), HTTP);
    }

    private final String protocol;

    public RpcProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String protocol() {
        return protocol;
    }

    public static RpcProtocol valueOf(String protocol) {
        if (StringUtils.isEmpty(protocol)) {
            throw new IllegalArgumentException("protocol must not be null!");
        }
        RpcProtocol rpcProtocol = PROTOCOL_MAP.get(protocol);
        if (rpcProtocol == null) {
            throw new IllegalArgumentException("cannot find protocol, " + protocol);
        }
        return rpcProtocol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RpcProtocol that = (RpcProtocol) o;
        return protocol.equals(that.protocol);

    }

    @Override
    public int hashCode() {
        return 31 * protocol.hashCode() + 29;
    }
}
