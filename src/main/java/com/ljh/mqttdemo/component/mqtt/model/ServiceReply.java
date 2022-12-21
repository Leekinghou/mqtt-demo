package com.ljh.mqttdemo.component.mqtt.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author lijinhao
 * @version 1.0
 * @date 2022/12/20 17:10
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceReply<T> {
    private Integer result;

    private T info;

    private T output;
}
