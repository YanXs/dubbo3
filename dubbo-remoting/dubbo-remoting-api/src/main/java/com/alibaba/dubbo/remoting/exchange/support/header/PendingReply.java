package com.alibaba.dubbo.remoting.exchange.support.header;


import com.alibaba.dubbo.remoting.message.Response;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * ADD CALLBACK HERE ??
 */
public class PendingReply {

    private volatile Long savedReplyTo;

    private final LinkedBlockingQueue<Response> queue;

    public PendingReply(Long savedReplyTo) {
        this.savedReplyTo = savedReplyTo;
        this.queue = new LinkedBlockingQueue<Response>();
    }

    public Long getSavedReplyTo() {
        return savedReplyTo;
    }

    public void setSavedReplyTo(Long savedReplyTo) {
        this.savedReplyTo = savedReplyTo;
    }

    public LinkedBlockingQueue<Response> getQueue() {
        return queue;
    }
}
