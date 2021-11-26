package com.example.provider.config.rabbitmq.exchange;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * DirectExchange：根据发送端的routingKey绑定的routingKey匹配队列
 *
 * @author songbo
 */
@Configuration
public class RabbitMqDirectExchangeConfig {

    /* 队列名 */
    public static final String DIRECT_QUEUE_1 = "direct.queue-1";
    public static final String DIRECT_QUEUE_2 = "direct.queue-2";
    /* 交换机名 */
    public static final String DIRECT_EXCHANGE = "direct-exchange";
    /* 路由键名 */
    public static final String DIRECT_QUEUE_1_ROUTING_KEY_A = DIRECT_QUEUE_1 + ".blue";
    public static final String DIRECT_QUEUE_1_ROUTING_KEY_B = DIRECT_QUEUE_1 + ".pink";
    public static final String DIRECT_QUEUE_2_ROUTING_KEY_A = DIRECT_QUEUE_2 + ".pink";
    public static final String DIRECT_QUEUE_2_ROUTING_KEY_B = DIRECT_QUEUE_2 + ".green";

    /**
     * 注册queue
     *
     * @return Queue
     */
    @Bean(name = "directQueue1")
    public Queue directQueue1() {
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", RabbitMqDeadLetterExchangeConfig.DEAD_LETTER_EXCHANGE);
        params.put("x-dead-letter-routing-key", RabbitMqDeadLetterExchangeConfig.DEAD_LETTER_QUEUE_1_ROUTING_KEY);
        return QueueBuilder.durable(DIRECT_QUEUE_1).withArguments(params).build();
    }

    @Bean(name = "directQueue2")
    public Queue directQueue2() {
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", RabbitMqDeadLetterExchangeConfig.DEAD_LETTER_EXCHANGE);
        params.put("x-dead-letter-routing-key", RabbitMqDeadLetterExchangeConfig.DEAD_LETTER_QUEUE_2_ROUTING_KEY);
        params.put("x-message-ttl", 3000L); // 消息在队列中的存活时间
        return QueueBuilder.durable(DIRECT_QUEUE_2).withArguments(params).build();
    }

    /**
     * 注册DirectExchange交换机
     *
     * @return
     */
    @Bean(name = "directExchange")
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE, true, false);
    }

    /**
     * 把队列1绑定到Direct Exchange，routingKey为blue
     *
     * @param directExchange
     * @param queue
     * @return
     */
    @Bean
    public Binding directBinding1a(@Qualifier("directExchange") DirectExchange directExchange,
                                   @Qualifier("directQueue1") Queue queue) {
        return BindingBuilder.bind(queue).to(directExchange).with(DIRECT_QUEUE_1_ROUTING_KEY_A);
    }

    /**
     * 把队列1绑定到Direct Exchange，routingKey为pink
     *
     * @param directExchange
     * @param queue
     * @return
     */
    @Bean
    public Binding directBinding1b(@Qualifier("directExchange") DirectExchange directExchange,
                                   @Qualifier("directQueue1") Queue queue) {
        return BindingBuilder.bind(queue).to(directExchange).with(DIRECT_QUEUE_1_ROUTING_KEY_B);
    }

    /**
     * 把队列2绑定到Direct Exchange，routingKey为pink
     *
     * @param directExchange
     * @param queue
     * @return
     */
    @Bean
    public Binding directBinding2a(@Qualifier("directExchange") DirectExchange directExchange,
                                   @Qualifier("directQueue2") Queue queue) {
        return BindingBuilder.bind(queue).to(directExchange).with(DIRECT_QUEUE_2_ROUTING_KEY_A);
    }

    /**
     * 把队列2绑定到Direct Exchange，routingKey为green
     *
     * @param directExchange
     * @param queue
     * @return
     */
    @Bean
    public Binding directBinding2b(@Qualifier("directExchange") DirectExchange directExchange,
                                   @Qualifier("directQueue2") Queue queue) {
        return BindingBuilder.bind(queue).to(directExchange).with(DIRECT_QUEUE_2_ROUTING_KEY_B);
    }
}
