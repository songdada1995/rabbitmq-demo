package com.example.provider.config.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 多个消费者监听队列，消费者依次获取消息，每个消息只能被一个消费者消息
 * @author songbo
 *
 */
@Configuration
public class RabbitMqQueueConfig {

    public static final String HELLO_QUEUE = "hello.queue";

	/**
	 * 配置Queue
	 * @return
	 */
    @Bean(name="helloQueue")
    public Queue helloQueue() {
        return new Queue(HELLO_QUEUE);
    }

}