package com.example.provider.config.rabbitmq.exchange;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 广播路由交换器:绑定到该路由器的每一个Queue都接收到消息，消息可被多个消费者消息，无视routing_key
 *
 * @author songbo
 */
@Configuration
public class RabbitMqFanoutExchangeConfig {

    /* 队列名 */
    public static final String FANOUT_QUEUE_1 = "fanout.queue-1";
    public static final String FANOUT_QUEUE_2 = "fanout.queue-2";
    /* 交换机名 */
    public static final String FANOUT_EXCHANGE = "fanout-exchange";

    /**
     * 注册queue
     *
     * @return Queue
     */
    @Bean(name = "fanoutQueue1")
    public Queue fanoutQueue1() {
        return new Queue(FANOUT_QUEUE_1, true);
    }

    @Bean(name = "fanoutQueue2")
    public Queue fanoutQueue2() {
        return new Queue(FANOUT_QUEUE_2, true);
    }

    /**
     * 配置广播路由交换器
     *
     * @return
     */
    @Bean(name = "fanoutExchange")
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    /**
     * 把queue1绑定到广播路由交换器
     *
     * @param fanoutExchange
     * @param queue
     * @return
     */
    @Bean
    public Binding fanoutBinding1(@Qualifier("fanoutExchange") FanoutExchange fanoutExchange,
                                  @Qualifier("fanoutQueue1") Queue queue) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    /**
     * 把queue2绑定到广播路由交换器
     *
     * @param fanoutExchange
     * @param queue
     * @return
     */
    @Bean
    public Binding fanoutBinding2(@Qualifier("fanoutExchange") FanoutExchange fanoutExchange,
                                  @Qualifier("fanoutQueue2") Queue queue) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }
}
