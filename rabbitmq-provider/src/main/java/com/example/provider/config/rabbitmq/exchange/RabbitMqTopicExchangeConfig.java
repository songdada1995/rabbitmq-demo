package com.example.provider.config.rabbitmq.exchange;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Topic转发模式：队列按规则匹配到交换机，消息可被多个消费者消息
 *
 * @author songbo
 */
@Configuration
public class RabbitMqTopicExchangeConfig {

    /* 队列名 */
    public static final String TOPIC_QUEUE_NAME_1 = "topic.queue-1";
    public static final String TOPIC_QUEUE_NAME_2 = "topic.queue-2";
    /* 交换机名 */
    public static final String TOPIC_EXCHANGE_NAME_1 = "topic-exchange1";
    public static final String TOPIC_EXCHANGE_NAME_2 = "topic-exchange2";
    /* 路由键名 */
    public static final String TOPIC_QUEUE_1_ROUTING_KEY = TOPIC_QUEUE_NAME_1 + ".routingKey-1";
    public static final String TOPIC_QUEUE_2_ROUTING_KEY = TOPIC_QUEUE_NAME_2 + ".#";

    /**
     * 注册Queue1，绑定死信队列
     *
     * @return
     */
    @Bean(name = "topicQueue1")
    public Queue topicQueue1() {
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", RabbitMqDeadLetterExchangeConfig.DEAD_LETTER_EXCHANGE); //声明当前队列绑定的死信交换机
        params.put("x-dead-letter-routing-key", RabbitMqDeadLetterExchangeConfig.DEAD_LETTER_QUEUE_1_ROUTING_KEY); //声明当前队列的死信路由键
        params.put("x-message-ttl", 3000L); // 消息在队列中的存活时间
        return QueueBuilder.durable(TOPIC_QUEUE_NAME_1).withArguments(params).build();
    }

    /**
     * 注册Queue2，绑定死信队列
     *
     * @return
     */
    @Bean(name = "topicQueue2")
    public Queue topicQueue2() {
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", RabbitMqDeadLetterExchangeConfig.DEAD_LETTER_EXCHANGE);
        params.put("x-dead-letter-routing-key", RabbitMqDeadLetterExchangeConfig.DEAD_LETTER_QUEUE_2_ROUTING_KEY);
        return QueueBuilder.durable(TOPIC_QUEUE_NAME_2).withArguments(params).build();
    }

    /**
     * 注册交换机
     *
     * @return
     */
    @Bean(name = "topicExchange1")
    public TopicExchange topicExchange1() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME_1);
    }

    @Bean(name = "topicExchange2")
    public TopicExchange topicExchange2() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME_2);
    }

    /**
     * 把队列topicQueue1绑定到交换机，当且仅当发送端routingKey为topic.queue-1时才匹配
     *
     * @param queue1
     * @param topicExchange1
     * @return
     */
    @Bean
    Binding topicBinding1(@Qualifier("topicQueue1") Queue queue1,
                          @Qualifier("topicExchange1") TopicExchange topicExchange1) {
        return BindingBuilder.bind(queue1).to(topicExchange1).with(TOPIC_QUEUE_1_ROUTING_KEY);
    }

    /**
     * 把队列topicQueue2绑定到交换机，当发送端routingKey为topicMatch.开头时都会匹配
     *
     * @param queue2
     * @param topicExchange2
     * @return
     */
    @Bean
    Binding topicBinding2(@Qualifier("topicQueue2") Queue queue2,
                          @Qualifier("topicExchange2") TopicExchange topicExchange2) {
        return BindingBuilder.bind(queue2).to(topicExchange2).with(TOPIC_QUEUE_2_ROUTING_KEY); //*表示一个词,#表示零个或多个词
    }
}
