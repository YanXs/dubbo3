package com.alibaba.dubbo.rpc.async;

import com.google.common.util.concurrent.FutureCallback;

public interface CommandListener<T> extends FutureCallback<T> {
}
