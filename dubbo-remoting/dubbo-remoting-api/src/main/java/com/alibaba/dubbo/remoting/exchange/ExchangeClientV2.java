package com.alibaba.dubbo.remoting.exchange;

import com.alibaba.dubbo.remoting.Client;

public interface ExchangeClientV2 extends Client, ExchangeChannelV2 {

    void addInterceptor(Interceptor interceptor);

}
