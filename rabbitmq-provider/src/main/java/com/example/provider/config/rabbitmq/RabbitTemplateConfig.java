package com.example.provider.config.rabbitmq;

import com.example.provider.config.rabbitmq.callback.RabbitTemplateConfirmCallbackConfig;
import com.example.provider.config.rabbitmq.callback.RabbitTemplateReturnCallbackConfig;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;

@Configuration
public class RabbitTemplateConfig {

    @Resource
    private RabbitTemplateConfirmCallbackConfig rabbitTemplateConfirmCallbackConfig;

    @Resource
    private RabbitTemplateReturnCallbackConfig rabbitTemplateReturnCallbackConfig;

    @Resource
    private CachingConnectionFactory connectionFactory;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setConfirmCallback(rabbitTemplateConfirmCallbackConfig);
        rabbitTemplate.setReturnCallback(rabbitTemplateReturnCallbackConfig);
        /**
         * 当mandatory标志位设置为true时
         * 如果exchange根据自身类型和消息routingKey无法找到一个合适的queue存储消息
         * 那么broker会调用basic.return方法将消息返还给生产者
         * 当mandatory设置为false时，出现上述情况broker会直接将消息丢弃
         */
        rabbitTemplate.setMandatory(true);
        return rabbitTemplate;
    }

}
