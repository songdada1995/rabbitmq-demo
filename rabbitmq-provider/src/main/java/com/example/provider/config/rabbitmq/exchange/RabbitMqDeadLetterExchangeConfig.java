package com.example.provider.config.rabbitmq.exchange;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 死信队列
 */
@Configuration
public class RabbitMqDeadLetterExchangeConfig {

    /* 队列 */
    public static final String DEAD_LETTER_QUEUE_1 = "dlx.queue1";
    public static final String DEAD_LETTER_QUEUE_2 = "dlx.queue2";
    /* 交换机 */
    public static final String DEAD_LETTER_EXCHANGE = "dlx-exchange";
    /* 路由键 */
    public static final String DEAD_LETTER_QUEUE_1_ROUTING_KEY = DEAD_LETTER_QUEUE_1 + ".routingKey";
    public static final String DEAD_LETTER_QUEUE_2_ROUTING_KEY = DEAD_LETTER_QUEUE_2 + ".routingKey";

    /**
     * 注册queue
     *
     * @return Queue
     */
    @Bean(name = "dlxQueue1")
    public Queue dlxQueue1() {
        return new Queue(DEAD_LETTER_QUEUE_1, true);
    }

    /**
     * 注册queue
     *
     * @return Queue
     */
    @Bean(name = "dlxQueue2")
    public Queue dlxQueue2() {
        return new Queue(DEAD_LETTER_QUEUE_2, true);
    }

    /**
     * 注册DeadLetterExchange死信交换机
     *
     * @return
     */
    @Bean(name = "dlxExchange")
    public DirectExchange dlxExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }


    /**
     * 死信队列绑定死信交换机
     *
     * @param dlxExchange
     * @param queue
     * @return
     */
    @Bean
    public Binding dlxBinding1(@Qualifier("dlxExchange") DirectExchange dlxExchange,
                              @Qualifier("dlxQueue1") Queue queue) {
        return BindingBuilder.bind(queue).to(dlxExchange).with(DEAD_LETTER_QUEUE_1_ROUTING_KEY);
    }

    /**
     * 死信队列绑定死信交换机
     *
     * @param dlxExchange
     * @param queue
     * @return
     */
    @Bean
    public Binding dlxBinding2(@Qualifier("dlxExchange") DirectExchange dlxExchange,
                              @Qualifier("dlxQueue2") Queue queue) {
        return BindingBuilder.bind(queue).to(dlxExchange).with(DEAD_LETTER_QUEUE_2_ROUTING_KEY);
    }


}
