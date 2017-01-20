package com.alibaba.dubbo.remoting.exchange.support.header;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.ExecutionException;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.exchange.*;
import com.alibaba.dubbo.remoting.exchange.support.DefaultFuture;
import com.alibaba.dubbo.remoting.transport.ChannelHandlerDelegate;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Xs
 */
public class HeaderExchangeHandlerV2 implements ChannelHandlerDelegate {

    protected static final Logger logger = LoggerFactory.getLogger(HeaderExchangeHandler.class);

    public static String KEY_READ_TIMESTAMP = HeartbeatHandler.KEY_READ_TIMESTAMP;

    public static String KEY_WRITE_TIMESTAMP = HeartbeatHandler.KEY_WRITE_TIMESTAMP;

    private final List<Interceptor> interceptors = new ArrayList<Interceptor>();

    private final ExchangeHandler handler;

    public HeaderExchangeHandlerV2(ExchangeHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("handler == null");
        }
        this.handler = handler;
    }

    private void handlerEvent(Channel channel, Request req) throws RemotingException {
        if (req.getData() != null && req.getData().equals(Request.READONLY_EVENT)) {
            channel.setAttribute(Constants.CHANNEL_ATTRIBUTE_READONLY_KEY, Boolean.TRUE);
        }
    }

    private Response handleRequest(ExchangeChannel channel, Request request) throws RemotingException {
        Response.Builder builder = new Response.Builder(request.getId());
        builder.version(request.getVersion());
        if (request.isBroken()) {
            Object data = request.getData();
            String msg;
            if (data == null) msg = null;
            else if (data instanceof Throwable) msg = StringUtils.toString((Throwable) data);
            else msg = data.toString();
            builder.errorMsg("Fail to decode request due to: " + msg);
            builder.status(Response.BAD_REQUEST);
            return builder.build();
        }
        Object msg = request.getData();
        try {
            Object result = handler.reply(channel, msg);
            builder.status(Response.OK).result(result);
        } catch (Throwable e) {
            builder.status(Response.SERVICE_ERROR).errorMsg(StringUtils.toString(e));
        }
        return builder.build();
    }

    private void handleResponse(Response response) throws RemotingException {
        if (response != null && !response.isHeartbeat()) {
            HeaderExchangeChannelV2.handleResponse(response);
        }
    }

    public void connected(Channel channel) throws RemotingException {
        channel.setAttribute(KEY_READ_TIMESTAMP, System.currentTimeMillis());
        channel.setAttribute(KEY_WRITE_TIMESTAMP, System.currentTimeMillis());
        ExchangeChannel exchangeChannel = HeaderExchangeChannelV2.getOrAddChannel(channel);
        try {
            handler.connected(exchangeChannel);
        } finally {
            HeaderExchangeChannelV2.removeChannelIfDisconnected(channel);
        }
    }

    public void disconnected(Channel channel) throws RemotingException {
        channel.setAttribute(KEY_READ_TIMESTAMP, System.currentTimeMillis());
        channel.setAttribute(KEY_WRITE_TIMESTAMP, System.currentTimeMillis());
        ExchangeChannel exchangeChannel = HeaderExchangeChannel.getOrAddChannel(channel);
        try {
            handler.disconnected(exchangeChannel);
        } finally {
            HeaderExchangeChannel.removeChannelIfDisconnected(channel);
        }
    }

    public void sent(Channel channel, Object message) throws RemotingException {
        Throwable exception = null;
        try {
            channel.setAttribute(KEY_WRITE_TIMESTAMP, System.currentTimeMillis());
            ExchangeChannel exchangeChannel = HeaderExchangeChannelV2.getOrAddChannel(channel);
            try {
                handler.sent(exchangeChannel, message);
            } finally {
                HeaderExchangeChannelV2.removeChannelIfDisconnected(channel);
            }
        } catch (Throwable t) {
            exception = t;
        }
        if (message instanceof Request) {
            Request request = (Request) message;
            DefaultFuture.sent(channel, request);
        }
        if (exception != null) {
            if (exception instanceof RuntimeException) {
                throw (RuntimeException) exception;
            } else if (exception instanceof RemotingException) {
                throw (RemotingException) exception;
            } else {
                throw new RemotingException(channel.getLocalAddress(), channel.getRemoteAddress(),
                        exception.getMessage(), exception);
            }
        }
    }

    private static boolean isClientSide(Channel channel) {
        InetSocketAddress address = channel.getRemoteAddress();
        URL url = channel.getUrl();
        return url.getPort() == address.getPort() &&
                NetUtils.filterLocalHost(url.getIp())
                        .equals(NetUtils.filterLocalHost(address.getAddress().getHostAddress()));
    }

    public void received(Channel channel, Object message) throws RemotingException {
        channel.setAttribute(KEY_READ_TIMESTAMP, System.currentTimeMillis());
        ExchangeChannel exchangeChannel = HeaderExchangeChannelV2.getOrAddChannel(channel);
        try {
            if (message instanceof Request) {
                Request request = (Request) message;
                if (request.isEvent()) {
                    handlerEvent(channel, request);
                } else {
                    if (request.isTwoWay()) {
                        Interceptor.Chain chain = new ExchangeHandlerChain(0, exchangeChannel, request);
                        Response response = chain.proceed(request);
                        channel.send(response);
                    } else {
                        handler.received(exchangeChannel, request.getData());
                    }
                }
            } else if (message instanceof Response) {
                handleResponse((Response) message);
            } else if (message instanceof String) {
                if (isClientSide(channel)) {
                    Exception e = new Exception("Dubbo client can not supported string message: " +
                            message + " in channel: " + channel + ", url: " + channel.getUrl());
                    logger.error(e.getMessage(), e);
                } else {
                    String echo = handler.telnet(channel, (String) message);
                    if (echo != null && echo.length() > 0) {
                        channel.send(echo);
                    }
                }
            } else {
                handler.received(exchangeChannel, message);
            }
        } finally {
            HeaderExchangeChannelV2.removeChannelIfDisconnected(channel);
        }
    }

    class ExchangeHandlerChain implements Interceptor.Chain {
        private final int index;
        private final ExchangeChannel exchangeChannel;
        private final Request request;

        public ExchangeHandlerChain(int index, ExchangeChannel exchangeChannel, Request request) {
            this.index = index;
            this.exchangeChannel = exchangeChannel;
            this.request = request;
        }

        @Override
        public Request request() {
            return request;
        }

        @Override
        public int timeout() {
            return 0;
        }

        @Override
        public Response proceed(Request request, int timeout) throws RemotingException {
            throw new UnsupportedOperationException("This is client side method");
        }

        @Override
        public Response proceed(Request request) throws RemotingException {
            if (index < interceptors.size()) {
                Interceptor.Chain chain = new ExchangeHandlerChain(index + 1, exchangeChannel, request);
                Interceptor interceptor = interceptors.get(index);
                Response response = interceptor.intercept(chain);
                if (response == null) {
                    throw new NullPointerException("interceptor " + interceptor + " returned null");
                }
                return response;
            }
            return handleRequest(exchangeChannel, request);
        }
    }

    public void caught(Channel channel, Throwable exception) throws RemotingException {
        if (exception instanceof ExecutionException) {
            ExecutionException e = (ExecutionException) exception;
            Object msg = e.getRequest();
            if (msg instanceof Request) {
                Request req = (Request) msg;
                if (req.isTwoWay() && !req.isHeartbeat()) {
                    Response.Builder builder = new Response.Builder(req.getId());
                    builder.version(req.getVersion()).status(Response.SERVER_ERROR).errorMsg(StringUtils.toString(e));
                    channel.send(builder.build());
                    return;
                }
            }
        }
        ExchangeChannel exchangeChannel = HeaderExchangeChannelV2.getOrAddChannel(channel);
        try {
            handler.caught(exchangeChannel, exception);
        } finally {
            HeaderExchangeChannelV2.removeChannelIfDisconnected(channel);
        }
    }

    public ChannelHandler getHandler() {
        if (handler instanceof ChannelHandlerDelegate) {
            return ((ChannelHandlerDelegate) handler).getHandler();
        } else {
            return handler;
        }
    }

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }
}
