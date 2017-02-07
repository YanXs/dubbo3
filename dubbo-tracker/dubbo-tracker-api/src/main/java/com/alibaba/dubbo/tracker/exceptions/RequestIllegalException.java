package com.alibaba.dubbo.tracker.exceptions;

public class RequestIllegalException extends RuntimeException {

    public RequestIllegalException(String message) {
        super(message);
    }

    public RequestIllegalException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestIllegalException(Throwable cause) {
        super(cause);
    }

}
