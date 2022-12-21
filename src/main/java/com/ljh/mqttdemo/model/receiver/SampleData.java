package com.ljh.mqttdemo.model.receiver;

import lombok.Data;

/**
 * @author lijinhao
 * @version 1.0
 * @date 2022/12/20 19:48
 */

@Data
public class SampleData {
    private String message;

    /**
     * 1： 上线
     * 0： 下线
     */
    private Integer status;
}
