package com.alibaba.dubbo.remoting.exchange.support.header;

import com.alibaba.dubbo.remoting.transport.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ReplyManager {

    private static final ReplyManager instance = new ReplyManager();

    private final Map<Long, PendingReply> replyStore = new StrictConcurrentHashMap<Long, PendingReply>();

    private final Map<Channel, ChannelReplyCounter> channelReplyCounters = new ConcurrentHashMap<Channel, ChannelReplyCounter>();

    private ReplyManager() {
    }

    public static ReplyManager get() {
        return instance;
    }

    public void initReplyCounter(Channel channel) {
        ChannelReplyCounter counter = channelReplyCounters.get(channel);
        if (counter == null) {
            counter = new ChannelReplyCounter(channel);
            channelReplyCounters.put(channel, counter);
        }
    }

    public void destroyReplyCounter(Channel channel) {
        channelReplyCounters.remove(channel);
    }

    public void addPendingReply(Long id, PendingReply pendingReply, Channel channel) {
        replyStore.put(id, pendingReply);
        ChannelReplyCounter counter = channelReplyCounters.get(channel);
        if (counter == null) {
            throw new IllegalStateException("channelReplyCounter should not be null here");
        }
        counter.increment();
    }

    public PendingReply getPendingReply(Long messageId) {
        return replyStore.get(messageId);
    }

    public void removePendingReply(PendingReply pendingReply, Channel channel) {
        replyStore.remove(pendingReply.getMessageId());
        ChannelReplyCounter counter = channelReplyCounters.get(channel);
        if (counter == null) {
            throw new IllegalStateException("channelReplyCounter should not be null here");
        }
        counter.decrement();
    }

    public boolean isChannelHoldingReplies(Channel channel) {
        ChannelReplyCounter counter = channelReplyCounters.get(channel);
        return counter != null && counter.get() > 0;
    }

    private class StrictConcurrentHashMap<K, V> extends ConcurrentHashMap<K, V> {
        public V put(K key, V value) {
            if (contains(key)) {
                throw new IllegalArgumentException("contains value for key: " + key);
            }
            return super.put(key, value);
        }
    }


    private class ChannelReplyCounter {

        private final Channel channel;

        private final AtomicInteger counter = new AtomicInteger(0);

        private ChannelReplyCounter(Channel channel) {
            this.channel = channel;
        }

        public Channel getChannel() {
            return channel;
        }

        public void increment() {
            counter.incrementAndGet();
        }

        public void decrement() {
            counter.decrementAndGet();
        }

        public int get() {
            return counter.get();
        }
    }
}
