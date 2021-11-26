package com.example.consumer.handler.rabbitmq;

import com.example.consumer.constants.RabbitMqConstant;
import com.example.consumer.constants.RedisKeyPrefixConstant;
import com.example.consumer.exception.BasicException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author songbo
 * @Date 2020/8/12 18:39
 * @Version 1.0
 */
@Slf4j
@Component
public class RabbitMqTopicExchange1MessageHandler {

    @Resource(name = "redissonClient")
    private RedissonClient redissonClient;

    /**
     * 申明topic.queue-1队列，绑定死信队列，监听消息
     *
     * @param message
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMqConstant.TOPIC_QUEUE_NAME_1,
                    arguments = {
                            @Argument(name = "x-dead-letter-exchange", value = RabbitMqConstant.DEAD_LETTER_EXCHANGE),
                            @Argument(name = "x-dead-letter-routing-key", value = RabbitMqConstant.DEAD_LETTER_QUEUE_1_ROUTING_KEY),
                            @Argument(name = "x-message-ttl", value = "3000", type = "java.lang.Long")
                    }),
            exchange = @Exchange(
                    value = RabbitMqConstant.TOPIC_EXCHANGE_NAME_1,
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {RabbitMqConstant.TOPIC_QUEUE_1_ROUTING_KEY}),
            containerFactory = "autoAckRabbitListenerContainerFactory")
    public void processTopicMessage1(Message message) throws Exception {
        log.info("======" + Thread.currentThread().getName() + "开始执行processTopicMessage1======");
        RLock lock = redissonClient.getLock(RedisKeyPrefixConstant.SHIPMENT + "3");

        try {
            lock.tryLock(30, TimeUnit.SECONDS);
            log.info(Thread.currentThread().getName() + "进入processMessage1，开始执行业务逻辑，开启分布式锁......");

            //将JSON格式数据转换为Map对象
            ObjectMapper mapper = new ObjectMapper();
            JavaType javaType = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
            Map<String, Object> resultMap = mapper.readValue(message.getBody(), javaType);
            log.info("接收者收到Map格式消息：");
            log.info("msg：" + resultMap.get("msg"));
            log.info("port：" + resultMap.get("port"));
            log.info("count：" + resultMap.get("count"));
            log.info("success：" + resultMap.get("success"));

            if (resultMap.containsKey("success") && !(boolean) resultMap.get("success")) {
                log.info(RabbitMqConstant.TOPIC_QUEUE_NAME_1 + "====错误消息：" + message);
                throw BasicException.newInstance().error("测试重试，抛出异常", 500);

            } else {
                log.info(RabbitMqConstant.TOPIC_QUEUE_NAME_1 + "====确认消息：" + message);
            }
            log.info("======" + Thread.currentThread().getName() + "结束执行processTopicMessage1======");

        } catch (Exception e) {
            log.error("======" + Thread.currentThread().getName() + "执行processTopicMessage1异常======");
            log.error(BasicException.exceptionTrace(e));
            throw e;
        } finally {
            log.info("======" + Thread.currentThread().getName() + "结束执行processTopicMessage1，释放分布式锁======");
            lock.unlock();
        }
    }


}
