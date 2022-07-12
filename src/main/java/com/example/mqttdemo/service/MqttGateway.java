package com.example.mqttdemo.service;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * _______________________
 *
 * @author lijinhao
 * @version 2.0
 * @program mqtt-demo
 * @date 2022/7/12 21:05
 */

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateway {
    // 定义重载方法，用于消息发送
    void sendToMqtt(String payload);
    // 指定topic进行消息发送
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, String payload);
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String payload);
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, byte[] payload);
}
