package com.ljh.mqttdemo.component.mqtt.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 统一的主题接收格式
 * @author lijinhao
 * @version 1.0
 * @date 2022/12/20 19:49
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonTopicReceiver<T> {
    private String tid;

    private String bid;

    private String method;

    private Long timestamp;

    private T data;

    private String gateway;

    private Integer needReply;

    private String from;
}
