package com.ljh.mqttdemo.component.mqtt.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljh.mqttdemo.model.receiver.SampleData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.util.CollectionUtils;

/**
 * @author lijinhao
 * @version 1.0
 * @date 2022/12/20 21:42
 */
@MessageEndpoint
public class StatusRouter {
    @Autowired
    private ObjectMapper mapper;

    /**
     * 将客户端发送的状态数据转换为对象.
     * @param message
     * @return
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATUS, outputChannel = ChannelName.INBOUND_STATUS_ROUTER)
    public CommonTopicReceiver<SampleData> resolveStatus(Message<?> message) {
        CommonTopicReceiver<SampleData> statusReceiver = new CommonTopicReceiver<>();
        try {
            statusReceiver = mapper.readValue(
                    (byte[])message.getPayload(),
                    new TypeReference<CommonTopicReceiver<SampleData>>() {});

            String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();

            // set gateway's sn
//
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusReceiver;
    }

    /**
     * 处理消息主题为【状态】的路由
     * 根据数据的不同，它被分配到不同的通道进行处理。
     * @param receiver
     * @return
     */
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
}
