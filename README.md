# dubbo3

dubbo3 fork [dubbox 2.8.4] (https://github.com/dangdangdotcom/dubbox)，具体的使用方法请参考[dubbox 2.8.4] (https://github.com/dangdangdotcom/dubbox)

## dubbo3修改和增加的功能

* 实现基于dubbo、hessian、http协议的分布式服务链路监控和追踪功能，可以将服务链路信息报告给[Zipkin](http://zipkin.io/),Zipkin的用户界面可以浏览全链路每一个服务的延迟

* 修改了ExchangeChannel中基于dubbo协议同步调用的接口，便于添加Interceptor拦截request记录服务调用信息

* 添加了okhttp3支持hessian协议、http协议，目前对这两种协议的拦截基于OkhttpClient的Interceptor机制

## 如何开启tracker

目前tracker的使用方式只支持spring xml配置，如下

```xml
<dubbo:tracker address="zipkin://127.0.0.1:9411" transport="http" sampler="counting" samplerate="1.0" flushinterval="2"/>
```

* address: zipkin transport address
* collector(transport): http\kafka\scribe
* sampler:采样器
* samplerate:采样率
* flushinterval:数据刷新频率

## 依赖

服务链路追踪主要基于Zipkin的[brave](https://github.com/openzipkin/brave)

```xml
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>3.2.0</version>
</dependency>

<dependency>
    <groupId>io.zipkin.brave</groupId>
    <artifactId>brave-core</artifactId>
    <version>3.9.0</version>
</dependency>

<dependency>
    <groupId>io.zipkin.brave</groupId>
    <artifactId>brave-http</artifactId>
    <version>3.9.0</version>
</dependency>

<dependency>
    <groupId>io.zipkin.brave</groupId>
    <artifactId>brave-web-servlet-filter</artifactId>
    <version>3.9.0</version>
</dependency>

<dependency>
    <groupId>io.zipkin.brave</groupId>
    <artifactId>brave-okhttp</artifactId>
    <version>3.9.0</version>
</dependency>

<dependency>
    <groupId>io.zipkin.brave</groupId>
    <artifactId>brave-spancollector-http</artifactId>
    <version>3.9.0</version>
</dependency>

<dependency>
    <groupId>io.zipkin.brave</groupId>
    <artifactId>brave-spancollector-kafka</artifactId>
    <version>3.9.0</version>
</dependency>

<dependency>
    <groupId>io.zipkin.brave</groupId>
    <artifactId>brave-spancollector-scribe</artifactId>
    <version>3.9.0</version>
</dependency>

<dependency>
    <groupId>com.google.auto.value</groupId>
    <artifactId>auto-value</artifactId>
    <version>1.3</version>
</dependency>
```

