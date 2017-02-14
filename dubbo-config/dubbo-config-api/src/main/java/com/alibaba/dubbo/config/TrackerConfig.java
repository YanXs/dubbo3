package com.alibaba.dubbo.config;

import java.util.Map;

public class TrackerConfig extends AbstractConfig {

    private String application;

    private String protocol;

    private String address;

    private Integer port;

    private String transport;

    private String sampler;

    private Float samplerate;

    private Integer flushinterval;

    private String group;

    private String version;

    /**
     * 自定义参数
     */
    private Map<String, String> parameters;

    public TrackerConfig() {
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getSampler() {
        return sampler;
    }

    public void setSampler(String sampler) {
        this.sampler = sampler;
    }

    public Float getSamplerate() {
        return samplerate;
    }

    public void setSamplerate(Float samplerate) {
        this.samplerate = samplerate;
    }

    public Integer getFlushinterval() {
        return flushinterval;
    }

    public void setFlushinterval(Integer flushinterval) {
        this.flushinterval = flushinterval;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        checkParameterName(parameters);
        this.parameters = parameters;
    }
}
