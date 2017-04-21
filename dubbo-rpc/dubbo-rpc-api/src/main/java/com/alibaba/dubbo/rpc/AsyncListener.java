package com.alibaba.dubbo.rpc;

import com.google.common.util.concurrent.FutureCallback;

public interface AsyncListener<T> extends FutureCallback<T> {
}
