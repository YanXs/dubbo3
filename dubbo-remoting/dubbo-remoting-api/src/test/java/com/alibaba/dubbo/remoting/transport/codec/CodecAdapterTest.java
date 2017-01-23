package com.alibaba.dubbo.remoting.transport.codec;

import org.junit.Before;

import com.alibaba.dubbo.remoting.codec.ExchangeCodecTest;

/**
 * @author <a href="mailto:gang.lvg@taobao.com">kimi</a>
 */
public class CodecAdapterTest extends ExchangeCodecTest {

    @Before
    public void setUp() throws Exception {
        codec = new CodecAdapter(new DeprecatedExchangeCodec());
    }

}
