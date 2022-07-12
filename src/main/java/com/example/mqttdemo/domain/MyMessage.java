package com.example.mqttdemo.domain;

/**
 * _______________________
 *
 * @author lijinhao
 * @version 2.0
 * @program mqtt-demo
 * @date 2022/7/12 21:11
 */

public class MyMessage {
    private String topic;
    private String content;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
