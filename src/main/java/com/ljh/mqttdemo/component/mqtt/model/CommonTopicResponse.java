package com.ljh.mqttdemo.component.mqtt.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lijinhao
 * @version 1.0
 * @date 2022/12/20 17:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonTopicResponse<T> {
    /**
     *命令下发后，根据报文中的tid和bid字段进行匹配，返回的tid和bid字段保持一致。
     */
    private String tid;

    private String bid;

    private String method;

    private T data;

    private Long timestamp;
}
