package com.alibaba.dubbo.tracker;

/**
 * @author Xs
 */
public class DubboRequestSpanNameProvider extends RpcContextMethodNameProvider {

    public String spanName(DubboRequest dubboRequest) {
        String spanName = getMethodNameFromContext();
        if (spanName == null) {
            spanName = getSpanNameFromAttachment(dubboRequest);
        }
        return spanName;
    }

    private String getSpanNameFromAttachment(DubboRequest dubboRequest) {
        return dubboRequest.getAttachment(RpcAttachment.SpanName.getName());
    }
}
