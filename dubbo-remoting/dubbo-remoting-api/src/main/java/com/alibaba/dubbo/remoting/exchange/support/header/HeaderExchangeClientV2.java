package com.alibaba.dubbo.remoting.exchange.support.header;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.Version;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.Client;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.exchange.*;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yanss
 */
public class HeaderExchangeClientV2 implements ExchangeClientV2 {

    private static final Logger logger = LoggerFactory.getLogger(HeaderExchangeClient.class);

    private final Client client;

    private final HeaderExchangeChannelV2 channel;

    private final List<Interceptor> interceptors = new ArrayList<Interceptor>();

    public HeaderExchangeClientV2(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("client == null");
        }
        this.client = client;
        this.channel = new HeaderExchangeChannelV2(client);
        String dubbo = client.getUrl().getParameter(Constants.DUBBO_VERSION_KEY);
    }

    public URL getUrl() {
        return channel.getUrl();
    }

    public InetSocketAddress getRemoteAddress() {
        return channel.getRemoteAddress();
    }

    public ChannelHandler getChannelHandler() {
        return channel.getChannelHandler();
    }

    public boolean isConnected() {
        return channel.isConnected();
    }

    public InetSocketAddress getLocalAddress() {
        return channel.getLocalAddress();
    }

    @Override
    public Response request(Request request) throws RemotingException {
        return null;
    }

    @Override
    public Response request(Request request, int timeout) throws RemotingException {
        return null;
    }

    public ExchangeHandler getExchangeHandler() {
        return channel.getExchangeHandler();
    }

    public void send(Object message) throws RemotingException {
        channel.send(message);
    }

    public void send(Object message, boolean sent) throws RemotingException {
        channel.send(message, sent);
    }

    public boolean isClosed() {
        return channel.isClosed();
    }

    public void close() {
        channel.close();
    }

    public void close(int timeout) {
        channel.close(timeout);
    }

    public void reset(URL url) {
        client.reset(url);
    }

    @Deprecated
    public void reset(com.alibaba.dubbo.common.Parameters parameters) {
        reset(getUrl().addParameters(parameters.getParameters()));
    }

    public void reconnect() throws RemotingException {
        client.reconnect();
    }

    public Object getAttribute(String key) {
        return channel.getAttribute(key);
    }

    public void setAttribute(String key, Object value) {
        channel.setAttribute(key, value);
    }

    public void removeAttribute(String key) {
        channel.removeAttribute(key);
    }

    public boolean hasAttribute(String key) {
        return channel.hasAttribute(key);
    }


    public Response request(Object request) throws RemotingException {
        return request(request, channel.getUrl().getPositiveParameter(Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT));
    }

    public Response request(Object request, int timeout) throws RemotingException {
        Request.Builder builder = new Request.Builder();
        builder.newId().version(Version.getVersion()).twoWay(true).data(request);
        Request req = builder.build();
        Interceptor.Chain chain = new ExchangeClientInterceptorChain(0, req);
        return chain.proceed(req, timeout);
    }

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    /**
     * @author Xs request interceptor
     */
    class ExchangeClientInterceptorChain implements Interceptor.Chain {

        private final int index;

        private final Request request;

        ExchangeClientInterceptorChain(int index, Request request) {
            this.index = index;
            this.request = request;
        }

        @Override
        public Request request() {
            return request;
        }

        @Override
        public Response proceed(Request request, int timeout) throws RemotingException {
            if (index < interceptors.size()) {
                Interceptor.Chain chain = new ExchangeClientInterceptorChain(index + 1, request);
                Interceptor interceptor = interceptors.get(index);
                Response response = interceptor.intercept(chain);
                if (response == null) {
                    throw new NullPointerException("interceptor " + interceptor
                            + " returned null");
                }
                return response;
            }
            return channel.request(request, timeout);
        }
    }

    @Override
    public String toString() {
        return "HeaderExchangeClient [channel=" + channel + "]";
    }
}
