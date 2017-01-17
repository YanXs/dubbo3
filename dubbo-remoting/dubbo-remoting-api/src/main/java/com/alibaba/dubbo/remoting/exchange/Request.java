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

import com.alibaba.dubbo.common.utils.StringUtils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Request.
 *
 * @author qian.lei
 * @author william.liangf
 */
public class Request {

    public static final String HEARTBEAT_EVENT = null;

    public static final String READONLY_EVENT = "R";

    private static final AtomicLong INVOKE_ID = new AtomicLong(0);

    private final long mId;

    private final String mVersion;

    private final boolean mTwoWay;

    private final boolean mEvent;

    private final boolean mBroken;

    private final Object mData;

    public Request(Builder builder) {
        this.mId = builder.mId;
        this.mVersion = builder.mVersion;
        this.mTwoWay = builder.mTwoWay;
        this.mEvent = builder.mEvent;
        this.mBroken = builder.mBroken;
        this.mData = builder.mData;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public static class Builder {
        private long mId;
        private String mVersion;
        private boolean mTwoWay = true;
        private boolean mEvent = false;
        private boolean mBroken = false;
        private Object mData;

        public Builder() {
        }

        public Builder(long mId) {
            this.mId = mId;
        }

        public Builder(Request request) {
            this.mId = request.mId;
            this.mVersion = request.mVersion;
            this.mTwoWay = request.mTwoWay;
            this.mEvent = request.mEvent;
            this.mBroken = request.mBroken;
            this.mData = request.mData;

        }

        public Builder id(long mId) {
            this.mId = mId;
            return this;
        }

        public Builder newId() {
            this.mId = Request.newId();
            return this;
        }

        public Builder version(String mVersion) {
            this.mVersion = mVersion;
            return this;
        }

        public Builder twoWay(boolean mTwoWay) {
            this.mTwoWay = mTwoWay;
            return this;
        }

        public Builder isEvent(boolean mEvent) {
            this.mEvent = mEvent;
            return this;
        }

        public Builder broken(boolean mBroken) {
            this.mBroken = mBroken;
            return this;
        }

        public Builder data(Object mData) {
            this.mData = mData;
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }

    public long getId() {
        return mId;
    }

    public String getVersion() {
        return mVersion;
    }

    public boolean isTwoWay() {
        return mTwoWay;
    }

    public boolean isEvent() {
        return mEvent;
    }

    public boolean isBroken() {
        return mBroken;
    }


    public Object getData() {
        return mData;
    }

    public boolean isHeartbeat() {
        return mEvent && HEARTBEAT_EVENT == mData;
    }

    private static long newId() {
        // getAndIncrement()增长到MAX_VALUE时，再增长会变为MIN_VALUE，负数也可以做为ID
        return INVOKE_ID.getAndIncrement();
    }

    @Override
    public String toString() {
        return "Request [id=" + mId + ", version=" + mVersion + ", twoway=" + mTwoWay + ", event=" + mEvent
                + ", broken=" + mBroken + ", data=" + (mData == this ? "this" : safeToString(mData)) + "]";
    }

    private static String safeToString(Object data) {
        if (data == null) return null;
        String dataStr;
        try {
            dataStr = data.toString();
        } catch (Throwable e) {
            dataStr = "<Fail toString of " + data.getClass() + ", cause: " +
                    StringUtils.toString(e) + ">";
        }
        return dataStr;
    }
}
