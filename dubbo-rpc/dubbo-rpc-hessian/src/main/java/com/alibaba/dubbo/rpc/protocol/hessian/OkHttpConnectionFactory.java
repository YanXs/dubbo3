package com.alibaba.dubbo.rpc.protocol.hessian;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.tracker.RpcTracker;
import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianConnectionFactory;
import com.caucho.hessian.client.HessianProxyFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * @author Xs
 */
public class OkHttpConnectionFactory implements HessianConnectionFactory {

    private final OkHttpClient client;

    public OkHttpConnectionFactory(com.alibaba.dubbo.common.URL url) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        Boolean enableTrace = url.getParameter(Constants.ENABLE_TRACE_KEY, true);
        if (enableTrace) {
            RpcTracker rpcTracker = ExtensionLoader.getExtensionLoader(RpcTracker.class).getDefaultExtension();
        }
        int timeout = url.getParameter(Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT);
        builder.readTimeout(timeout, TimeUnit.MILLISECONDS);
        builder.writeTimeout(timeout, TimeUnit.MILLISECONDS);
        client = builder.build();
    }

    @Override
    public void setHessianProxyFactory(HessianProxyFactory hessianProxyFactory) {
        // NOP
    }

    @Override
    public HessianConnection open(URL url) throws IOException {
        return new OkHttpConnection(client, url);
    }
}
