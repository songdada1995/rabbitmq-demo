package com.example.provider.config.rabbitmq.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * 消息失败返回，比如路由不到队列时触发回调
 *
 * @Author songbo
 * @Date 2020/8/12 16:14
 * @Version 1.0
 */
@Slf4j
@Configuration
public class RabbitTemplateReturnCallbackConfig implements RabbitTemplate.ReturnCallback {

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("ReturnCallback:消息主体 message : " + message);
        log.info("ReturnCallback:消息主体 message : " + replyCode);
        log.info("ReturnCallback:描述：" + replyText);
        log.info("ReturnCallback:消息使用的交换器 exchange : " + exchange);
        log.info("ReturnCallback:消息使用的路由键 routing : " + routingKey);
    }
}
