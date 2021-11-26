package com.example.consumer.handler.rabbitmq;

import com.example.consumer.constants.RabbitMqConstant;
import com.example.consumer.exception.BasicException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class RabbitMqDirectQueue1MessageHandler {

    /**
     * 申明direct.queue-1队列，绑定死信队列，监听消息
     *
     * @param message
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMqConstant.DIRECT_QUEUE_NAME_1,
                    arguments = {
                            @Argument(name = "x-dead-letter-exchange", value = RabbitMqConstant.DEAD_LETTER_EXCHANGE),
                            @Argument(name = "x-dead-letter-routing-key", value = RabbitMqConstant.DEAD_LETTER_QUEUE_1_ROUTING_KEY)
                    }),
            exchange = @Exchange(
                    value = RabbitMqConstant.DIRECT_EXCHANGE_NAME,
                    ignoreDeclarationExceptions = "true"),
            key = {RabbitMqConstant.DIRECT_QUEUE_1_ROUTING_KEY_B}),
            containerFactory = "autoAckRabbitListenerContainerFactory")
    public void processDirectMessage1(Message message) throws IOException {
        log.info(Thread.currentThread().getName() + "进入processDirectMessage1......");

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

            if (resultMap.containsKey("success") && !(boolean) resultMap.get("success")) {
                log.info(RabbitMqConstant.DIRECT_QUEUE_NAME_1 + "====错误消息：" + message);
                throw BasicException.newInstance().error("测试重试，抛出异常", 500);

            } else {
                log.info(RabbitMqConstant.DIRECT_QUEUE_NAME_1 + "====确认消息：" + message);
            }

        } catch (Exception e) {
            log.error(BasicException.exceptionTrace(e));
            throw e;
        }
    }

}
