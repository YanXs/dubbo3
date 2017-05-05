package com.alibaba.dubbo.remoting.exchange.support.header;


import com.alibaba.dubbo.remoting.message.Response;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Xs.
 */
public class PendingReply {

    private volatile Long messageId;

    private final BlockingQueue<Response> queue = new ArrayBlockingQueue<Response>(1);

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public void reply(Response reply) {
        this.queue.add(reply);
    }

    public Response get(long timeout, TimeUnit unit) throws InterruptedException {
        return (timeout <= 0) ? queue.take() : queue.poll(timeout, unit);
    }

    public Response get() throws InterruptedException {
        return queue.take();
    }
}
