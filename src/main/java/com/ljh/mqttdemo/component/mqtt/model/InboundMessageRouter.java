package com.ljh.mqttdemo.component.mqtt.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.router.AbstractMessageRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;

import static com.ljh.mqttdemo.component.mqtt.model.TopicConst.*;

/**
 * @author lijinhao
 * @version 1.0
 * @date 2022/12/20 20:20
 */
@Slf4j
@Component
public class InboundMessageRouter extends AbstractMessageRouter {
    @Resource(name = ChannelName.INBOUND)
    private MessageChannel inboundChannel;

    @Resource(name = ChannelName.INBOUND_STATUS)
    private MessageChannel statusChannel;

    @Resource(name = ChannelName.DEFAULT)
    private MessageChannel defaultChannel;

    private static final Pattern PATTERN_TOPIC_STATUS = Pattern.compile("^" + BASIC_PRE + REGEX_SN + AVAILABLE + "$");
//    private static final Pattern PATTERN_TOPIC_STATUS = Pattern.compile("sys/drone002/available");

    /**
     * All mqtt broker messages will arrive here before distributing them to different channels.
     * @param message message from mqtt broker
     * @return channel
     */
    @Override
    @Router(inputChannel = ChannelName.INBOUND)
    protected Collection<MessageChannel> determineTargetChannels(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        String topic = headers.get(MqttHeaders.RECEIVED_TOPIC).toString();
        byte[] payload = (byte[])message.getPayload();

        log.debug("received topic :{} \t payload :{}", topic, new String(payload));

        // status
        if (PATTERN_TOPIC_STATUS.matcher(topic).matches()) {
            return Collections.singleton(statusChannel);
        }
        return Collections.singleton(defaultChannel);
    }
}
