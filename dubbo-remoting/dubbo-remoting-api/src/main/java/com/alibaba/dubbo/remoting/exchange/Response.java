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
package com.alibaba.dubbo.remoting.exchange;

/**
 * Response
 *
 * @author qian.lei
 * @author william.liangf
 */
public class Response {

    public static final String HEARTBEAT_EVENT = null;
    public static final String READONLY_EVENT = "R";
    /**
     * ok.
     */
    public static final byte OK = 20;
    /**
     * client side timeout.
     */
    public static final byte CLIENT_TIMEOUT = 30;
    /**
     * server side timeout.
     */
    public static final byte SERVER_TIMEOUT = 31;
    /**
     * request format error.
     */
    public static final byte BAD_REQUEST = 40;
    /**
     * response format error.
     */
    public static final byte BAD_RESPONSE = 50;
    /**
     * service not found.
     */
    public static final byte SERVICE_NOT_FOUND = 60;
    /**
     * service error.
     */
    public static final byte SERVICE_ERROR = 70;
    /**
     * internal server error.
     */
    public static final byte SERVER_ERROR = 80;
    /**
     * internal server error.
     */
    public static final byte CLIENT_ERROR = 90;

    private final long mId;
    private final String mVersion;
    private final byte mStatus;
    private final boolean mEvent;
    private final String mErrorMsg;
    private final Object mResult;

    public Response(Builder builder) {
        this.mId = builder.mId;
        this.mVersion = builder.mVersion;
        this.mStatus = builder.mStatus;
        this.mEvent = builder.mEvent;
        this.mErrorMsg = builder.mErrorMsg;
        this.mResult = builder.mResult;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public static class Builder {
        private long mId = 0;
        private String mVersion;
        private byte mStatus = OK;
        private boolean mEvent = false;
        private String mErrorMsg;
        private Object mResult;

        public Builder() {
        }

        public Builder(long mId) {
            this.mId = mId;
        }

        public Builder(Response response) {
            this.mId = response.mId;
            this.mVersion = response.mVersion;
            this.mStatus = response.mStatus;
            this.mEvent = response.mEvent;
            this.mErrorMsg = response.mErrorMsg;
            this.mResult = response.mResult;
        }

        public Builder id(long mId) {
            this.mId = mId;
            return this;
        }

        public Builder version(String mVersion) {
            this.mVersion = mVersion;
            return this;
        }

        public Builder status(byte mStatus) {
            this.mStatus = mStatus;
            return this;
        }

        public Builder isEvent(boolean mEvent) {
            this.mEvent = mEvent;
            return this;
        }

        public Builder errorMsg(String mErrorMsg) {
            this.mErrorMsg = mErrorMsg;
            return this;
        }

        public Builder result(Object mResult) {
            this.mResult = mResult;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }

    public long getId() {
        return mId;
    }

    public String getVersion() {
        return mVersion;
    }

    public byte getStatus() {
        return mStatus;
    }

    public boolean isEvent() {
        return mEvent;
    }

    public boolean isHeartbeat() {
        return mEvent && HEARTBEAT_EVENT == mResult;
    }

    public Object getResult() {
        return mResult;
    }

    public String getErrorMessage() {
        return mErrorMsg;
    }


    @Override
    public String toString() {
        return "Response [id=" + mId + ", version=" + mVersion + ", status=" + mStatus + ", event=" + mEvent
                + ", error=" + mErrorMsg + ", result=" + (mResult == this ? "this" : mResult) + "]";
    }
}