package com.example.consumer.handler.rabbitmq;

import com.example.consumer.constants.RabbitMqConstant;
import com.example.consumer.exception.BasicException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author songbo
 * @Date 2020/8/12 18:39
 * @Version 1.0
 */
@Slf4j
@Component
public class RabbitMqDeadExchangeMessageHandler {

    /**
     * 监听dead.lt.queue队列
     *
     * @param message
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMqConstant.DEAD_LETTER_QUEUE_1),
            exchange = @Exchange(
                    value = RabbitMqConstant.DEAD_LETTER_EXCHANGE,
                    ignoreDeclarationExceptions = "true"),
            key = {RabbitMqConstant.DEAD_LETTER_QUEUE_1_ROUTING_KEY}),
            containerFactory = "autoAckRabbitListenerContainerFactory")
    public void processDeadQueue1Message(Message message) throws Exception {
        log.info("======" + Thread.currentThread().getName() + "开始执行死信队列DeadQueue1======");

        try {
            //将JSON格式数据转换为Map对象
            ObjectMapper mapper = new ObjectMapper();
            JavaType javaType = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
            Map<String, Object> resultMap = mapper.readValue(message.getBody(), javaType);
            log.info("接收者收到Map格式消息：");
            log.info("msg：" + resultMap.get("msg"));
            log.info("port：" + resultMap.get("port"));
            log.info("count：" + resultMap.get("count"));
            log.info("success：" + resultMap.get("success"));
            log.info("======" + Thread.currentThread().getName() + "结束执行死信队列DeadQueue1======");
            log.info(RabbitMqConstant.DEAD_LETTER_QUEUE_1 + "====确认消息：" + message);

        } catch (Exception e) {
            log.error(BasicException.exceptionTrace(e));
            throw e;
        }
    }


}
