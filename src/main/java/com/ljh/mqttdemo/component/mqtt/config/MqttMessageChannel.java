package com.ljh.mqttdemo.component.mqtt.config;

import com.ljh.mqttdemo.component.mqtt.model.ChannelName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.messaging.MessageChannel;

import java.util.concurrent.Executor;

/**
 * @author lijinhao
 * @version 1.0
 * @date 2022/12/20 14:44
 */
@Configuration
public class MqttMessageChannel {
    @Autowired
    private Executor threadPool;

    @Bean(name = ChannelName.INBOUND)
    public MessageChannel inboundChannel() {
        return new ExecutorChannel(threadPool);
    }

    @Bean(name = ChannelName.OUTBOUND)
    public MessageChannel outboundChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_STATUS)
    public MessageChannel statusChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.DEFAULT)
    public MessageChannel defaultChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_STATUS_ONLINE)
    public MessageChannel statusOnlineChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_STATUS_OFFLINE)
    public MessageChannel statusOffChannel() {
        return new DirectChannel();
    }
}
