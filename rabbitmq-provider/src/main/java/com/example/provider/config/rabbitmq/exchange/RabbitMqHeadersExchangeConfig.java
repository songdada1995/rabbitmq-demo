package com.example.provider.config.rabbitmq.exchange;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * HeadersExchange
 *
 * @author songbo
 */
@Configuration
public class RabbitMqHeadersExchangeConfig {

    /* 队列名 */
    public static final String HEADERS_QUEUE_1 = "headers.queue-1";
    public static final String HEADERS_QUEUE_2 = "headers.queue-2";
    /* 交换机名 */
    public static final String HEADERS_EXCHANGE = "headers-exchange";

    /**
     * 注册queue
     *
     * @return Queue
     */
    @Bean(name = "headersQueue1")
    public Queue headersQueue1() {
        return new Queue(HEADERS_QUEUE_1, true);
    }

    @Bean(name = "headersQueue2")
    public Queue headersQueue2() {
        return new Queue(HEADERS_QUEUE_2, true);
    }

    /**
     * 注册HeadersExchange
     *
     * @return
     */
    @Bean(name = "headersExchange")
    public HeadersExchange headersExchange() {
        return new HeadersExchange(HEADERS_EXCHANGE);
    }

    /**
     * 把queue1绑定到HeadersExchange
     *
     * @param headersExchange
     * @param queue
     * @return
     */
    @Bean
    public Binding headersBinding1(@Qualifier("headersExchange") HeadersExchange headersExchange,
                                   @Qualifier("headersQueue1") Queue queue) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("color", "blue");
        map.put("shape", "circle");

        // whereAny表示部分匹配
        return BindingBuilder.bind(queue).to(headersExchange).whereAny(map).match();
    }

    /**
     * 把queue2绑定到HeadersExchange
     *
     * @param headersExchange
     * @param queue
     * @return
     */
    @Bean
    public Binding headersBinding2(@Qualifier("headersExchange") HeadersExchange headersExchange,
                                   @Qualifier("headersQueue2") Queue queue) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("food", "meat");
        map.put("drinks", "cola");

        // whereAll表示全部匹配
        return BindingBuilder.bind(queue).to(headersExchange).whereAll(map).match();
    }


}
