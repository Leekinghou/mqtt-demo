package com.ljh.mqttdemo.component.mqtt.config;

import com.ljh.mqttdemo.component.mqtt.model.ChannelName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageHandler;

import java.time.Instant;

/**
 * 出站消息的客户端配置
 * @author lijinhao
 * @version 1.0
 * @date 2022/12/20 15:11
 */
@Configuration
public class MqttOutboundConfiguration {
    @Autowired
    private MqttConfiguration mqttConfiguration;

    @Autowired
    private MqttPahoClientFactory mqttClientFactory;

    /**
     * 出站消息通道的客户端
     */
    @Bean
    @ServiceActivator(inputChannel = ChannelName.OUTBOUND)
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
                mqttConfiguration.getClientId() + "_producer_" + Instant.now().toEpochMilli(),
                mqttClientFactory);
        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
        // 统一使用字节形式
        converter.setPayloadAsBytes(true);
        messageHandler.setAsync(true);
        /**
         * 0：At most once。消息发送者会想尽办法发送消息，但是遇到意外并不会重试。
         * 1：At least once。消息接收者如果没有知会或者知会本身丢失，消息发送者会再次发送以保证消息接收者至少会收到一次，当然可能造成重复消息。
         * 2：Exactly onces。保证这种语义肯待会减少并发或者增加延时，不过丢失或者重复消息是不可接受的时候，级别 2 是最合适的。
         * 订阅者收到 MQTT 消息的 QoS 级别，最终取决于发布消息的 QoS 和主题订阅的 QoS。
         */
        messageHandler.setDefaultQos(0);
        messageHandler.setConverter(converter);
        return messageHandler;
    }
}
