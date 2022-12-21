package com.ljh.mqttdemo.component.mqtt.config;

import com.ljh.mqttdemo.component.mqtt.model.ChannelName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.annotation.Resource;
import java.time.Instant;

/**
 * 入站消息的客户端配置
 * @author lijinhao
 * @version 1.0
 * @date 2022/12/20 14:32
 */

@Slf4j
@Configuration
@IntegrationComponentScan
public class MqttInboundConfiguration {

    @Autowired
    private MqttConfiguration mqttConfiguration;

    @Autowired
    private MqttPahoClientFactory mqttClientFactory;

    @Resource(name = ChannelName.INBOUND)
    private MessageChannel inboundChannel;

    /**
     * 入站消息通道的客户端
     * @return
     */
    @Bean(name = "adapter")
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

    /**
     * 定义一个默认通道来处理无效的消息.
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = ChannelName.DEFAULT)
    public MessageHandler defaultInboundHandler() {
        return message -> {
            log.info("默认通道不处理消息." +
                    "\nTopic: " + message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC) +
                    "\nPayload: " + message.getPayload());
        };
    }

}
