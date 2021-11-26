package com.example.consumer.config.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Author songbo
 * @Date 2020/8/12 18:31
 * @Version 1.0
 */
@Slf4j
@Configuration
public class RabbitMqConsumerConfig {

    @Resource
    private RabbitProperties properties;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(properties.getHost() + ":" + properties.getPort());
        connectionFactory.setUsername(properties.getUsername());
        connectionFactory.setPassword(properties.getPassword());
        return connectionFactory;
    }

    @Bean(name = "manualAckRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory manualAckRabbitListenerContainerFactory(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
        // SimpleRabbitListenerContainerFactory发现消息中有content_type有text就会默认将其转换成string类型的
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConnectionFactory(connectionFactory);
        containerFactory.setConcurrentConsumers(2);
        containerFactory.setMaxConcurrentConsumers(5);
        containerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 手动确认
        containerFactory.setMessageConverter(new Jackson2JsonMessageConverter());
        return containerFactory;
    }


    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        return rabbitAdmin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    /**
     * 简单消息监听容器
     * 功能：
     * 1.监听队列(多个对列),自动启动，自动声明功能
     * 2.设置事务特性，事务管理器，事务属性。事务容量(并发),是否开启事务，回滚消息等
     * 3.设置消费者的数量，最大最小数量，批量消费等
     * 4.设置消息确认和自动确认模式，是否重回队列，异常捕获handler函数
     * 5.设置消费者标签生成策略，是否独占模式，消费者属性
     * 6.设置具体的监听器，消息转换器等等
     * 注：也可使用注解@RabbitListener来消费消息。
     * 自测发现用了Container监听队列之后，@RabbitListener就监听不到了
     *
     * @param connectionFactory
     * @return
     */
//    @Bean
//    public SimpleMessageListenerContainer messageListenerContainer(@Qualifier("manualAckRabbitListenerContainerFactory") SimpleRabbitListenerContainerFactory connectionFactory) {
//
//        SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
//        endpoint.setMessageListener((message) -> {
//            log.info("==== 监听到消息 ==== " + new String(message.getBody()));
//            log.info(message.getMessageProperties().toString());
//        });
//
//        endpoint.setId(String.valueOf(UUID.randomUUID()));
//        SimpleMessageListenerContainer container = connectionFactory.createListenerContainer(endpoint);
//        container.setQueueNames(TOPIC_QUEUE_NAME_2, DIRECT_QUEUE_NAME_2); // 监听的队列
//
////        container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
////            System.out.println("====接收到消息=====");
////            System.out.println(message.getMessageProperties());
////            System.out.println(new String(message.getBody()));
////
////            log.info("==== 监听到消息 ==== :" + new String(message.getBody()));
////            if (message.getMessageProperties().getHeaders().get("error") == null) {
////                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
////                log.info("==== 监听消息已确认 ==== :" + new String(message.getBody()));
////            } else {
////                // 否认消息
////                // deliveryTag:该消息的index
////                // multiple：是否批量，true:将一次性拒绝所有小于deliveryTag的消息
////                // requeue：被拒绝的是否重新入队列
////                // channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
////
////                // 拒绝消息
////                // deliveryTag:该消息的index
////                // requeue：被拒绝的是否重新入队列
////                // channel.basicNack 与 channel.basicReject的区别在于basicNack可以拒绝多条消息，而basicReject一次只能拒绝一条消息
////                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
////                log.info("==== 监听消息已拒绝 ==== :" + new String(message.getBody()));
////            }
////        });
//
//        return container;
//    }


}
