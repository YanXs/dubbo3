package com.alibaba.dubbo.rpc.protocol.http;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import okhttp3.*;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.remoting.httpinvoker.AbstractHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

public class OkHttpInvokerRequestExecutor extends AbstractHttpInvokerRequestExecutor {

    private final OkHttpClient client;

    public OkHttpInvokerRequestExecutor(URL url) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(url.getParameter(Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT), TimeUnit.MILLISECONDS);
        builder.connectTimeout(url.getParameter(Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT), TimeUnit.MILLISECONDS);
        client = builder.build();
    }

    @Override
    protected RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration config, ByteArrayOutputStream baos) throws Exception {
        Request request = createPostRequest(config, baos);
        Response response = client.newCall(request).execute();
        return readRemoteInvocationResult(response.body().byteStream(), config.getCodebaseUrl());
    }

    private Request createPostRequest(HttpInvokerClientConfiguration config, ByteArrayOutputStream baos) {
        Request.Builder builder = new Request.Builder().url(config.getServiceUrl());
        LocaleContext locale = LocaleContextHolder.getLocaleContext();
        if (locale != null) {
            builder.addHeader(HTTP_HEADER_ACCEPT_LANGUAGE, StringUtils.toLanguageTag(locale.getLocale()));
        }
        if (isAcceptGzipEncoding()) {
            builder.addHeader(HTTP_HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse(getContentType()), baos.toByteArray());
        return builder.post(requestBody).build();
    }
}
