package com.ljh.mqttdemo.component.mqtt.service;

import com.ljh.mqttdemo.component.mqtt.model.CommonTopicResponse;
import com.ljh.mqttdemo.component.mqtt.model.ServiceReply;

import java.util.Optional;

public interface IMessageSenderService {
    /**
     * Publish a message to a specific topic.
     * @param topic target
     * @param response message
     */
    void publish(String topic, CommonTopicResponse response);

    /**
     * Use a specific qos to push messages to a specific topic.
     * @param topic target
     * @param qos   qos
     * @param response  message
     */
    void publish(String topic, int qos, CommonTopicResponse response);

    /**
     * 发送直播启动消息，同时接收响应.
     * @param topic
     * @param response  启动是否成功的通知.
     * @return
     */
    Optional<ServiceReply> publishWithReply(String topic, CommonTopicResponse response);

    /**
     * Send live streaming start message and receive a response at the same time.
     * @param clazz
     * @param topic
     * @param response
     * @param retryTime
     * @param <T>
     * @return
     */
    <T> Optional<T> publishWithReply(Class<T> clazz, String topic, CommonTopicResponse response, int retryTime);
}
