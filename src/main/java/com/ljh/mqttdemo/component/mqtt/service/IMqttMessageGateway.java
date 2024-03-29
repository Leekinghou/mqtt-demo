package com.ljh.mqttdemo.component.mqtt.service;


import com.ljh.mqttdemo.component.mqtt.model.ChannelName;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * 定义一个 @MessagingGateway 修饰的接口，用于消息的发送，
 * @MessagingGateway 的 defaultRequestChannel 参数用于绑定具体的 MessageChannel
 */
@Component
@MessagingGateway(defaultRequestChannel = ChannelName.OUTBOUND)
public interface IMqttMessageGateway {
    /**
     * Publish a message to a specific topic.
     * @param topic target
     * @param payload   message
     */
    void publish(@Header(MqttHeaders.TOPIC) String topic, byte[] payload);

    /**
     * Use a specific qos to push messages to a specific topic.
     * @param topic     target
     * @param payload   message
     * @param qos   qos
     */
    void publish(@Header(MqttHeaders.TOPIC) String topic, byte[] payload, @Header(MqttHeaders.QOS) int qos);
}
