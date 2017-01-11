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

    public OkHttpConnectionFactory() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
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
