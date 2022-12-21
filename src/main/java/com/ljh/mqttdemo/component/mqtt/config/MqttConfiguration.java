package com.ljh.mqttdemo.component.mqtt.config;

import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

/**
 * @author lijinhao
 * @version 1.0
 * @date 2022/12/20 13:25
 */

/**
 * 从spring3开始spring就支持两种bean的配置方式， 一种是基于xml文件方式、另一种就是JavaConfig
 * 在Spring Boot中，Java Config的使用也已完全替代了applicationContext.xml
 * 在实现JavaConfig配置的时候就需要使用@Configuration和@Bean注解
 * @Configuration的作用：标注在类上，配置spring容器(应用上下文)。相当于把该类作为spring的xml配置文件中的<beans>
 * @Configuration注解的定义如下：
 * @Target(ElementType.TYPE)
 * @Retention(RetentionPolicy.RUNTIME)
 * @Documented
 * @Component
 * public @interface Configuration {
 *     String value() default "";
 * }
 * 从定义来看，底层是含有@Component ，所以@Configuration 具有和 @Component 的作用。
 * 因此context:component-scan/或者@ComponentScan都能处理@Configuration注解的类。
 */
@Data
@Configuration
@ConfigurationProperties("mqtt") // 统一前缀
public class MqttConfiguration {
    private String protocol;

    private String host;

    private String port;

    private String username;

    private String password;

    private String clientId;

    /**
     * 客户端一连接就要订阅的话题
     */
    private String inboundTopic;

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setServerURIs(new String[] {
                new StringBuffer()
                    .append(protocol.trim())
                    .append("://")
                    .append(host.trim())
                    .append(":")
                    .append(port.trim())
                    .toString()});
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setAutomaticReconnect(true);
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
        mqttConnectOptions.setCleanSession(false);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*10秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        mqttConnectOptions.setKeepAliveInterval(10);
        return mqttConnectOptions;
    }

    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory() {
        DefaultMqttPahoClientFactory defaultMqttPahoClientFactory = new DefaultMqttPahoClientFactory();
        defaultMqttPahoClientFactory.setConnectionOptions(mqttConnectOptions());
        return defaultMqttPahoClientFactory;
    }
}
