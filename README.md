# dubbo3

dubbo3 基于 [dubbox 2.8.4] (https://github.com/dangdangdotcom/dubbox) 修改
具体的使用方法请参考[dubbox 2.8.4] (https://github.com/dangdangdotcom/dubbox)

## dubbo3修改和增加的功能

* 重新设计实现了consumer端的异步实现方式，支持guava的ListenableFuture，使用异步调用api更加简单

* 添加NotifyCluster和NotifyClusterInvoker代替BroadcastCluster和BroadcastClusterInvoker实现广播

* 实现基于dubbo、hessian、http协议的分布式服务链路监控和追踪功能，可以将服务链路信息报告给[Zipkin](http://zipkin.io/),Zipkin的用户界面可以浏览全链路每一个服务的延迟

* 修改了ExchangeChannel中同步调用的接口，增加Interceptor接口拦截request记录服务调用信息

* 添加了okhttp3支持hessian协议、http协议，目前对这两种协议的拦截基于OkhttpClient的Interceptor机制

### consumer异步调用

#### Example 1: 基于guava ListenableFuture示例
```java
ListenableFuture<Complex> future = simpleService.async_getComplex("abc");
Futures.addCallback(future, new FutureCallback<Complex>() {
       @Override
       public void onSuccess(Complex result) {
                
       }

       @Override
       public void onFailure(Throwable t) {

       }
});
```
dubbo3中异步实现思路是为同步接口生成对应的异步接口，consumer和provider使用生成的接口
在客户端的代理InvocationHandler中调用同步接口， 具体实现参考AsyncableInvocationHandler  
origin interface:  
```java
public interface SimpleService {
    Complex getComplex(String id);  
}
```
async interface:(使用auto-async自动生成)
```java
public interface Unified_SimpleService extends SimpleService {
  ListenableFuture<Complex> async_getComplex(String id);
}
```
consumer配置
```xml
<dubbo:reference id="service" interface="Unified_SimpleService"/>
```
provider配置
```xml
<dubbo:service id="service" interface="Unified_SimpleService"/>
```

#### auto-async自动生成异步接口
自动生成async interface需要使用[auto-async] (https://github.com/YanXs/auto-async)
```xml
<dependency>
    <groupId>net.vakilla</groupId>
    <artifactId>auto-async</artifactId>
    <version>1.0.0</version>
</dependency>
```
### NotifyCluster广播
dubbo中使用BroadcastCluster实现广播功能，实现方式是顺序调用所有的invoker。这种方式带来的问题是效率低，时间复杂度O(m*n),m代表invoker数量，n代表方法执行时间
NotifyCluster采用异步模式并行调用invoker，时间复杂度可以接近O(n),当然取决于线程数量和invoker的数量，但是相比于BroadcastCluster线性调用性能高出很多

使用方式与broadcastCluster相同，只需要cluster=notify即可
具体实现参考NotifyClusterInvoker


### rpcTracker分布式链路追踪

目前tracker的使用方式只支持spring xml配置,目前有如下两种方式

方式一：
```xml
<dubbo:tracker address="zipkin://127.0.0.1:9411" transport="http" sampler="counting" samplerate="1.0" flushinterval="2"/>
```

* address: zipkin transport address
* collector(transport): http\kafka\scribe
* sampler:采样器
* samplerate:采样率
* flushinterval:数据刷新频率

方式二：
使用方式一在dubbo内部创建tracker对象，因为目前只支持zipkin，tracker对应Brave，没有办法同其他系统，比如数据库的监控结合起来，推荐使用下面的方式
配置

```xml
<dubbo:tracker id="trackerEngine" protocol="zipkin" ref="braveRpcTrackerEngine"/>

<bean id="braveRpcTrackerEngine" class="brave.spring.BraveRpcTrackerEngineFactoryBean">
    <property name="brave" ref="brave"/>
</bean>

<bean id="brave" class="brave.spring.BraveFactoryBean">
    <property name="serviceName" value="demo-provider"/>
    <property name="transport" value="http"/>
    <property name="transportAddress" value="127.0.0.1:9411"/>
</bean>
```


## 依赖

服务链路追踪主要基于Zipkin的 [brave] (https://github.com/openzipkin/brave)
链路追踪功能参考另一个项目 [nightawk] (https://github.com/YanXs/nighthawk)
项目中需要引用

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>dubbo</artifactId>
    <version>3.0.1</version>
</dependency>

<dependency>
    <groupId>io.vakilla.nightawk</groupId>
    <artifactId>nightawk-core</artifactId>
    <version>1.0.0.RELEASE</version>
</dependency>

<dependency>
    <groupId>io.vakilla.nightawk</groupId>
    <artifactId>nightawk-dubbo3</artifactId>
    <version>1.0.0.RELEASE</version>
</dependency>
```

以上依赖需要自己使用maven编译打包(以后会提交到maven中央库)



