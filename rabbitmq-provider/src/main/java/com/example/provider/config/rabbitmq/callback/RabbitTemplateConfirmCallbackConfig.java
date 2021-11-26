package com.example.provider.config.rabbitmq.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * 消息发送到 Broker 后触发回调，确认消息是否到达 Broker 服务器
 *
 * @Author songbo
 * @Date 2020/8/12 16:00
 * @Version 1.0
 */
@Slf4j
@Configuration
public class RabbitTemplateConfirmCallbackConfig implements RabbitTemplate.ConfirmCallback {

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("消息唯一标识：" + correlationData);
        if (ack) {
            log.info("消息发送成功!");
        } else {
            log.info("消息发送失败，失败原因：" + cause);
        }
    }

}
