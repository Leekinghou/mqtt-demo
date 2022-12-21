# mqtt-demo

## Introduce
Spring Integration是一种轻量级消息传递模块，并支持通过声明式适配器与外部系统集成。
简单来说，Spring Integration 抽象了用于消息传递的一套规范，并且基于这套规范提供了很多企业级的中间件的集成。
支持基于 AMQP 的消息队列、MQTT、RMI 等等中间件。

MQTT通信方式默认是发布/订阅模式的。
通信系统中有发布者和订阅者。发布者发布消息而订阅者接收消息。我们把发布者和订阅者统称为客户端。客户端可以同时是发布者和订阅者。
在系统中有另外一个角色，它接收发布者的消息并且将消息派发给订阅者。我们一般称这个角色为消息`Broker`。
在`MQTT`中默认是广播的，也就是说订阅了相同`topic`的订阅者都能收到发布者发送的消息。

## Integration
本项目使用的MQTT协议功能基于`Spring Integration`，使用前先在pom文件中引入maven依赖
```xml
<dependency>
    <groupId>org.springframework.integration</groupId>
    <artifactId>spring-integration-mqtt</artifactId>
    <version>5.5.5</version>
</dependency>
```

## Running
1. 在配置文件application.yml中修改配置：
```yml
mqtt:
  protocol: tcp
  host: mqtt broker ip # 192.168.1.1
  port: 1883
  username: JavaServer
  password: 123456
  client-id: 123456
  # Topics that need to be subscribed when initially connecting to mqtt, multiple topics are divided by ",".
  inbound-topic: sys/+/available
```
### 主题
MQTT 协议基于主题 (Topic) 进行消息路由，主题 (Topic) 类似 URL 路径，例如：
```
chat/room/1
sensor/10/temperature
sensor/+/temperature
```
主题 (Topic) 通过'/'分割层级，支持'+', '#'通配符：
```
'+': 表示通配一个层级，例如 a/+，匹配 a/x, a/y
'#': 表示通配多个层级，例如 a/#，匹配 a/x, a/b/c/d
订阅者可以订阅含通配符主题，但发布者不允许向含通配符主题发布消息。
```

### QoS
为了满足不同的场景，MQTT 支持三种不同级别的服务质量（Quality of Service，QoS）为不同场景提供消息可靠性：
```
0：At most once。消息发送者会想尽办法发送消息，但是遇到意外并不会重试。
1：At least once。消息接收者如果没有知会或者知会本身丢失，消息发送者会再次发送以保证消息接收者至少会收到一次，当然可能造成重复消息。
2：Exactly onces。保证这种语义肯待会减少并发或者增加延时，不过丢失或者重复消息是不可接受的时候，级别 2 是最合适的。
```
**订阅者收到 MQTT 消息的 QoS 级别，最终取决于发布消息的 QoS 和主题订阅的 QoS**。

2. 搭建或使用一个现成的mqtt服务器
[Docker - 在容器中搭建运行EMQ服务器（MQTT服务器）](https://www.hangge.com/blog/cache/detail_2609.html)
   

## Configuration
1. Inbound（消息驱动）通道适配器:
入站通道适配器由`MqttPahoMessageDrivenChannelAdapter`实现，通常会在里面配置： 
- 客户端ID
- MQTT Broker URL
- 待订阅的主题列表 
- 待订阅的主题QoS值列表

```java
public MessageProducerSupport mqttInbound() {
     MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
             mqttConfiguration.getClientId() + "_consumer_" + Instant.now().toEpochMilli(),
             mqttClientFactory, mqttConfiguration.getInboundTopic().split(","));
     DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
     // use byte types uniformly
     converter.setPayloadAsBytes(true);
     adapter.setConverter(converter);
     adapter.setQos(1);
     adapter.setOutputChannel(inboundChannel);
     return adapter;
 }
```
其中`mqttClientFactory`/`mqttConfiguration`配置了MQTT Broker URL、账户密码、主题列表等信息

## Essential Component
1. Message  
![](https://image-20220620.oss-cn-guangzhou.aliyuncs.com/image/20221221150118.png)

Message包含Header和Payload两部分
   
2. Message Channel  
![](https://image-20220620.oss-cn-guangzhou.aliyuncs.com/image/20221221143406.png)

消息通过消息网关发送出去，由`MessageChannel`的实例来实现`DirectChannel`处理发送的细节。MqttMessageChannel.java中实现了定义：
```java
@Configuration
public class MqttMessageChannel {
    @Autowired
    private Executor threadPool;

    @Bean(name = ChannelName.INBOUND)
    public MessageChannel inboundChannel() {
        return new ExecutorChannel(threadPool);
    }

    @Bean(name = ChannelName.OUTBOUND)
    public MessageChannel outboundChannel() {
        return new DirectChannel();
    }
}
```
MessageChannel用于解耦生产者和消费者，实现消息发送。

3. Message Router  
![](https://image-20220620.oss-cn-guangzhou.aliyuncs.com/image/20221221145852.png)  
```java
@Router(inputChannel = ChannelName.INBOUND_STATUS_ROUTER)
public String resolveStatusRouter(CommonTopicReceiver<SampleData> receiver) {
     // 根据状态内容判断是上线还是离线.
     if(receiver.getData().getStatus().equals(1))
         return ChannelName.INBOUND_STATUS_ONLINE;
     else if(receiver.getData().getStatus().equals(0))
         return ChannelName.INBOUND_STATUS_OFFLINE;
     else
         return ChannelName.DEFAULT;
 }
```
`MessageRouter`用来控制消息转发的`Channel`

4. Service Activator  
![](https://image-20220620.oss-cn-guangzhou.aliyuncs.com/image/20221221150321.png)  
```java
@ServiceActivator(inputChannel = ChannelName.DEFAULT)
public MessageHandler defaultInboundHandler() {
     return message -> {
         log.info("默认通道不处理消息." +
                 "\nTopic: " + message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC) +
                 "\nPayload: " + message.getPayload());
     };
}
```
`Service Activator`用来绑定`MessageHandler`和用于消费消息的`MessageChannel`
5. Channel Adapter  
![](https://image-20220620.oss-cn-guangzhou.aliyuncs.com/image/20221221150742.png)  
`ChannelAdapter`用来连接`MessageChannel`和具体的消息端口，例如通信的`topic`
   
## Interactive logic
1. 对于发布者：
消息通过消息网关发送出去，由`MessageChannel`的实例`DirectChannel`处理发送的细节。
`DirectChannel`收到消息后，内部通过`MessageHandler`的实例`MqttPahoMessageHandler`发送到指定的`Topic`。

2. 对于订阅者：
通过注入`MessageProducerSupport`的实例`MqttPahoMessageDrivenChannelAdapter`，实现订阅`Topic`和绑定消息消费的`MessageChannel`。
同样由`MessageChannel`的实例`DirectChannel`处理消费细节。`Channel`消息后会发送给我们自定义的`MqttInboundMessageHandler`实例进行消费。
