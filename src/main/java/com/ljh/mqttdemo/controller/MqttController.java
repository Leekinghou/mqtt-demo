package com.ljh.mqttdemo.controller;

import com.ljh.mqttdemo.component.mqtt.model.ChannelName;
import com.ljh.mqttdemo.component.mqtt.model.CommonTopicReceiver;
import com.ljh.mqttdemo.model.receiver.SampleData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试mqtt模块可用性
 * @author lijinhao
 * @version 1.0
 * @date 2022/12/20 17:32
 */
@Slf4j
@RestController
public class MqttController {
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATUS_ONLINE, outputChannel = ChannelName.OUTBOUND)
    public void mqttAvailable(CommonTopicReceiver<SampleData> receiver) {
        log.info(receiver.getData().toString());
        // 处理逻辑
    }
}
