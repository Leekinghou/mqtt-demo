package com.ljh.mqttdemo.component.mqtt.model;

import java.util.regex.Pattern;

import static com.ljh.mqttdemo.component.mqtt.model.TopicConst.AVAILABLE;

/**
 * 信道名字
 * @author lijinhao
 * @version 1.0
 * @date 2022/12/20 14:36
 */

public class ChannelName {
    public static final String INBOUND = "inbound";

    public static final String DEFAULT = "default";

    public static final String INBOUND_STATUS = "inbound_status";

    public static final String INBOUND_STATUS_ROUTER = "inbound_status_router";

    public static final String OUTBOUND = "outbound";

    public static final String INBOUND_STATUS_ONLINE = "online";

    public static final String INBOUND_STATUS_OFFLINE = "offline";

}
