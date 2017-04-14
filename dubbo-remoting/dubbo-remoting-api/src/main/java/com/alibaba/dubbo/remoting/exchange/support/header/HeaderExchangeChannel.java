/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.remoting.exchange.support.header;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.Version;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.remoting.exception.RemotingException;
import com.alibaba.dubbo.remoting.exception.TimeoutException;
import com.alibaba.dubbo.remoting.exchange.ExchangeChannel;
import com.alibaba.dubbo.remoting.exchange.ExchangeHandler;
import com.alibaba.dubbo.remoting.message.Request;
import com.alibaba.dubbo.remoting.message.Response;
import com.alibaba.dubbo.remoting.transport.Channel;
import com.alibaba.dubbo.remoting.transport.ChannelHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * ExchangeReceiver
 *
 * @author william.liangf
 */
final class HeaderExchangeChannel implements ExchangeChannel {

    private static final Logger logger = LoggerFactory.getLogger(HeaderExchangeChannel.class);

    private static final String CHANNEL_KEY = HeaderExchangeChannel.class.getName() + ".CHANNEL";

    public static final ConcurrentHashMap<Long, PendingReply> REPLY_HOLDER = new ConcurrentHashMap<Long, PendingReply>();

    private final Channel channel;

    private volatile boolean closed = false;

    HeaderExchangeChannel(Channel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("channel == null");
        }
        this.channel = channel;
    }

    static HeaderExchangeChannel getOrAddChannel(Channel ch) {
        if (ch == null) {
            return null;
        }
        HeaderExchangeChannel ret = (HeaderExchangeChannel) ch.getAttribute(CHANNEL_KEY);
        if (ret == null) {
            ret = new HeaderExchangeChannel(ch);
            if (ch.isConnected()) {
                ch.setAttribute(CHANNEL_KEY, ret);
            }
        }
        return ret;
    }

    static void removeChannelIfDisconnected(Channel ch) {
        if (ch != null && !ch.isConnected()) {
            ch.removeAttribute(CHANNEL_KEY);
        }
    }

    public void send(Object message) throws RemotingException {
        send(message, getUrl().getParameter(Constants.SENT_KEY, false));
    }

    public void send(Object message, boolean sent) throws RemotingException {
        if (closed) {
            throw new RemotingException(this.getLocalAddress(), null, "Failed to send message " + message +
                    ", cause: The channel " + this + " is closed!");
        }
        if (message instanceof Request
                || message instanceof Response
                || message instanceof String) {
            channel.send(message, sent);
        } else {
            Request.Builder builder = new Request.Builder();
            builder.newId().version(Version.getVersion()).twoWay(false).data(message);
            Request request = builder.build();
            channel.send(request, sent);
        }
    }

    @Override
    public Response request(Request request) throws RemotingException {
        return request(request, channel.getUrl().getPositiveParameter(Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT));
    }

    @Override
    public Response request(Request request, int timeout) throws RemotingException {
        if (closed) {
            throw new RemotingException(this.getLocalAddress(), null, "Failed to send request " + request +
                    ", cause: The channel " + this + " is closed!");
        }
        PendingReply pendingReply = new PendingReply(request.getId());
        addPendingReply(pendingReply);
        long startTimeMs = System.currentTimeMillis();
        try {
            channel.send(request);
        } catch (RemotingException e) {
            removePendingReply(pendingReply);
            throw e;
        }
        LinkedBlockingQueue<Response> reply = pendingReply.getQueue();
        Response response = null;
        try {
            response = (timeout <= 0) ? reply.take() : reply.poll(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RemotingException(this, e);
        } finally {
            removePendingReply(pendingReply);
        }
        // timeout
        if (response == null) {
            throw new TimeoutException(channel, getTimeoutMessage(request, timeout, startTimeMs));
        }
        return response;
    }

    public String getTimeoutMessage(Request request, int timeout, long startTimeMs) {
        long nowTimestamp = System.currentTimeMillis();
        return "elapsed: " + (nowTimestamp - startTimeMs) + " ms, timeout: "
                + timeout + " ms, request: " + request + ", channel: " + channel.getLocalAddress()
                + " -> " + channel.getRemoteAddress();
    }

    private static void addPendingReply(PendingReply pendingReply) {
        REPLY_HOLDER.putIfAbsent(pendingReply.getSavedReplyTo(), pendingReply);
    }

    private static void removePendingReply(PendingReply pendingReply) {
        REPLY_HOLDER.remove(pendingReply.getSavedReplyTo());
    }

    public static void handleResponse(Response response) {
        PendingReply pendingReply = REPLY_HOLDER.get(response.getId());
        if (pendingReply == null) {
            logger.warn("Response received after timeout for " + response);
            return;
        }
        LinkedBlockingQueue<Response> replyHandoff = pendingReply.getQueue();
        replyHandoff.add(response);
    }

    public boolean isClosed() {
        return closed;
    }

    public void close() {
        try {
            channel.close();
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
    }

    // gracefully close
    public void close(int timeout) {
        if (closed) {
            return;
        }
        closed = true;
        if (timeout > 0) {
            long start = System.currentTimeMillis();
            while (shouldWait(start, timeout)) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
        close();
    }

    private boolean shouldWait(long start, long timeout) {
        return (System.currentTimeMillis() - start < timeout) && !REPLY_HOLDER.isEmpty();
    }

    public InetSocketAddress getLocalAddress() {
        return channel.getLocalAddress();
    }

    public InetSocketAddress getRemoteAddress() {
        return channel.getRemoteAddress();
    }

    public URL getUrl() {
        return channel.getUrl();
    }

    public boolean isConnected() {
        return channel.isConnected();
    }

    public ChannelHandler getChannelHandler() {
        return channel.getChannelHandler();
    }

    public ExchangeHandler getExchangeHandler() {
        return (ExchangeHandler) channel.getChannelHandler();
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (channel.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        HeaderExchangeChannel other = (HeaderExchangeChannel) obj;
        return channel.equals(other.channel);
    }

    @Override
    public String toString() {
        return channel.toString();
    }

}