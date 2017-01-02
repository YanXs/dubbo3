package com.alibaba.dubbo.rpc.protocol.hessian;

import com.caucho.hessian.client.HessianConnection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Xs
 */
public abstract class AbstractHessianConnection implements HessianConnection {

    protected final ByteArrayOutputStream output;

    AbstractHessianConnection() {
        this.output = new ByteArrayOutputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return output;
    }


    @Override
    public void destroy() throws IOException {
    }

    @Override
    public void close() throws IOException {
    }
}
