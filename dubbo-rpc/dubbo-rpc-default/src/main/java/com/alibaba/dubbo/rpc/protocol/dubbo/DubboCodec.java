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
package com.alibaba.dubbo.rpc.protocol.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.Version;
import com.alibaba.dubbo.common.io.Bytes;
import com.alibaba.dubbo.common.io.UnsafeByteArrayInputStream;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.serialize.ObjectInput;
import com.alibaba.dubbo.common.serialize.ObjectOutput;
import com.alibaba.dubbo.common.serialize.OptimizedSerialization;
import com.alibaba.dubbo.common.serialize.Serialization;
import com.alibaba.dubbo.common.utils.ReflectUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.remoting.transport.Channel;
import com.alibaba.dubbo.remoting.Codec2;
import com.alibaba.dubbo.remoting.exchange.codec.ExchangeCodec;
import com.alibaba.dubbo.remoting.message.Request;
import com.alibaba.dubbo.remoting.message.Response;
import com.alibaba.dubbo.remoting.transport.CodecSupport;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcInvocation;

import java.io.IOException;
import java.io.InputStream;

import static com.alibaba.dubbo.rpc.protocol.dubbo.CallbackServiceCodec.encodeInvocationArgument;

/**
 * Dubbo codec.
 *
 * @author qianlei
 * @author chao.liuc
 */
public class DubboCodec extends ExchangeCodec implements Codec2 {

    private static final Logger log = LoggerFactory.getLogger(DubboCodec.class);

    public static final String NAME = "dubbo";

    public static final String DUBBO_VERSION = Version.getVersion(DubboCodec.class, Version.getVersion());

    public static final byte RESPONSE_WITH_EXCEPTION = 0;

    public static final byte RESPONSE_VALUE = 1;

    public static final byte RESPONSE_NULL_VALUE = 2;

    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];

    protected Object decodeBody(Channel channel, InputStream is, byte[] header) throws IOException {
        byte flag = header[2], proto = (byte) (flag & SERIALIZATION_MASK);
        Serialization s = CodecSupport.getSerialization(channel.getUrl(), proto);
        // get request id.
        long id = Bytes.bytes2long(header, 4);
        if ((flag & FLAG_REQUEST) == 0) {
            // decode response.
            Response.Builder builder = new Response.Builder(id);
            boolean isEvent = (flag & FLAG_EVENT) != 0;
            builder.isEvent(isEvent);
            if (isEvent) {
                builder.result(Response.HEARTBEAT_EVENT);
            }
            // get status.
            byte status = header[3];
            builder.status(status);
            Response res = builder.build();
            if (status == Response.OK) {
                try {
                    Object data;
                    if (isEvent) {
                        data = decodeEventData(channel, deserialize(s, channel.getUrl(), is));
                    } else {
                        DecodeableRpcResult result;
                        if (channel.getUrl().getParameter(
                                Constants.DECODE_IN_IO_THREAD_KEY,
                                Constants.DEFAULT_DECODE_IN_IO_THREAD)) {
                            result = new DecodeableRpcResult(channel, res, is, proto);
                            result.decode();
                        } else {
                            result = new DecodeableRpcResult(channel, res,
                                    new UnsafeByteArrayInputStream(readMessageData(is)), proto);
                        }
                        data = result;
                    }
                    res = res.newBuilder().result(data).build();
                } catch (Throwable t) {
                    if (log.isWarnEnabled()) {
                        log.warn("Decode response failed: " + t.getMessage(), t);
                    }
                    res = res.newBuilder().status(Response.CLIENT_ERROR).errorMsg(StringUtils.toString(t)).build();
                }
            } else {
                res = res.newBuilder().errorMsg(deserialize(s, channel.getUrl(), is).readUTF()).build();
            }
            return res;
        } else {
            // decode request.
            Request.Builder builder = new Request.Builder(id);
            builder.version(Version.getVersion()).twoWay((flag & FLAG_TWOWAY) != 0);
            boolean isEvent = (flag & FLAG_EVENT) != 0;
            builder.isEvent(isEvent);
            if (isEvent) {
                builder.data(Request.HEARTBEAT_EVENT);
            }
            Request req = builder.build();
            try {
                Object data;
                if (isEvent) {
                    data = decodeEventData(channel, deserialize(s, channel.getUrl(), is));
                } else {
                    DecodeableRpcInvocation inv;
                    if (channel.getUrl().getParameter(
                            Constants.DECODE_IN_IO_THREAD_KEY,
                            Constants.DEFAULT_DECODE_IN_IO_THREAD)) {

                        inv = new DecodeableRpcInvocation(channel, req, is, proto);
                        inv.decode();
                    } else {
                        inv = new DecodeableRpcInvocation(channel, req,
                                new UnsafeByteArrayInputStream(readMessageData(is)), proto);
                    }
                    data = inv;
                }
                req = req.newBuilder().data(data).build();
            } catch (Throwable t) {
                if (log.isWarnEnabled()) {
                    log.warn("Decode request failed: " + t.getMessage(), t);
                }
                req = req.newBuilder().broken(true).data(t).build();
            }
            return req;
        }
    }

    private ObjectInput deserialize(Serialization serialization, URL url, InputStream is)
            throws IOException {
        return serialization.deserialize(url, is);
    }

    private byte[] readMessageData(InputStream is) throws IOException {
        if (is.available() > 0) {
            byte[] result = new byte[is.available()];
            is.read(result);
            return result;
        }
        return new byte[]{};
    }

    @Override
    protected void encodeRequestData(Channel channel, ObjectOutput out, Object data) throws IOException {
        RpcInvocation inv = (RpcInvocation) data;
        out.writeUTF(inv.getAttachment(Constants.DUBBO_VERSION_KEY, DUBBO_VERSION));
        out.writeUTF(inv.getAttachment(Constants.PATH_KEY));
        out.writeUTF(inv.getAttachment(Constants.VERSION_KEY));
        out.writeUTF(inv.getMethodName());
        if (getSerialization(channel) instanceof OptimizedSerialization && !containComplexArguments(inv)) {
            out.writeInt(inv.getParameterTypes().length);
        } else {
            out.writeInt(-1);
            out.writeUTF(ReflectUtils.getDesc(inv.getParameterTypes()));
        }

        Object[] args = inv.getArguments();
        if (args != null)
            for (int i = 0; i < args.length; i++) {
                out.writeObject(encodeInvocationArgument(channel, inv, i));
            }
        out.writeObject(inv.getAttachments());
    }

    @Override
    protected void encodeResponseData(Channel channel, ObjectOutput out, Object data) throws IOException {
        Result result = (Result) data;
        Throwable th = result.getException();
        if (th == null) {
            Object ret = result.getValue();
            if (ret == null) {
                out.writeByte(RESPONSE_NULL_VALUE);
            } else {
                out.writeByte(RESPONSE_VALUE);
                out.writeObject(ret);
            }
        } else {
            out.writeByte(RESPONSE_WITH_EXCEPTION);
            out.writeObject(th);
        }
    }

    // workaround for the target method matching of kryo & fst
    private boolean containComplexArguments(RpcInvocation invocation) {
        for (int i = 0; i < invocation.getParameterTypes().length; i++) {
            if (invocation.getArguments()[i] == null || invocation.getParameterTypes()[i] != invocation.getArguments()[i].getClass()) {
                return true;
            }
        }
        return false;
    }
}