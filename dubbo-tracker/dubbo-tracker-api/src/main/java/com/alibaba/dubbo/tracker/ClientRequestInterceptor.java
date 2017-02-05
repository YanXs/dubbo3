package com.alibaba.dubbo.tracker;

/**
 * @author Xs
 */
public interface ClientRequestInterceptor {

    void handle(ClientRequestAdapter clientRequestAdapter);
}
