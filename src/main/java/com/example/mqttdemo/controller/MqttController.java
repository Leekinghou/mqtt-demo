package com.example.mqttdemo.controller;

import com.example.mqttdemo.domain.MyMessage;
import com.example.mqttdemo.service.MqttGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * _______________________
 *
 * @author lijinhao
 * @version 2.0
 * @program mqtt-demo
 * @date 2022/7/12 21:10
 */
@RestController
public class MqttController {

    @Resource
    private MqttGateway mqttGateway;

    @PostMapping("/send")
    public String send(@RequestBody MyMessage myMessage) {
        // 发送消息到指定主题
        mqttGateway.sendToMqtt(myMessage.getTopic(), 1, myMessage.getContent());
        return "send topic: " + myMessage.getTopic()+ ", message : " + myMessage.getContent();
    }

}

