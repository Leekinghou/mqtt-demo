package com.example.mqttdemo.domain;

import java.io.Serializable;

/**
 * _______________________
 *
 * @author lijinhao
 * @version 2.0
 * @program mqtt-demo
 * @date 2022/7/13 00:39
 */

public class MyMessage implements Serializable {

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
