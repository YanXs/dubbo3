package com.alibaba.dubbo.tracker;

/**
 * @author Xs
 */
public interface ClientResponseInterceptor {

    void handle(ClientResponseAdapter clientResponseAdapter);

}
