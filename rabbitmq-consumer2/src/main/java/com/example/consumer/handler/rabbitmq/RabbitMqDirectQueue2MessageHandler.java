package com.example.consumer.handler.rabbitmq;

import com.example.consumer.constants.RabbitMqConstant;
import com.example.consumer.exception.BasicException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class RabbitMqDirectQueue2MessageHandler {

    /**
     * 监听direct.queue-2
     *
     * @param message
     * @param channel
     * @param map
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMqConstant.DIRECT_QUEUE_NAME_2,
                    arguments = {
                            @Argument(name = "x-dead-letter-exchange", value = RabbitMqConstant.DEAD_LETTER_EXCHANGE),
                            @Argument(name = "x-dead-letter-routing-key", value = RabbitMqConstant.DEAD_LETTER_QUEUE_2_ROUTING_KEY),
                            @Argument(name = "x-message-ttl", value = "3000", type = "java.lang.Long")
                    }),
            exchange = @Exchange(
                    value = RabbitMqConstant.DIRECT_EXCHANGE_NAME,
                    ignoreDeclarationExceptions = "true"
            ),
            key = {RabbitMqConstant.DIRECT_QUEUE_2_ROUTING_KEY_B}),
            containerFactory = "manualAckRabbitListenerContainerFactory")
    public void processDirectQueue2(Message message, Channel channel, @Headers Map<String, Object> map) {
        log.info("======" + Thread.currentThread().getName() + "开始执行processDirectQueue2======");
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

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
                log.info(RabbitMqConstant.DIRECT_QUEUE_NAME_2 + "====错误消息：" + message);
                throw BasicException.newInstance().error("测试异常进入死信队列！", 500);

            } else {
                log.info(RabbitMqConstant.DIRECT_QUEUE_NAME_2 + "====确认消息：" + message);
                // 确认消息
                // deliveryTag:该消息的index
                // multiple：是否批量，true:将一次性ack所有小于deliveryTag的消息
                channel.basicAck(deliveryTag, false);
            }
            log.info("======" + Thread.currentThread().getName() + "结束执行processDirectQueue2======");

        } catch (Exception e) {
            log.error("======" + Thread.currentThread().getName() + "执行processDirectQueue2异常======");
            try {
                if (message.getMessageProperties().getRedelivered()) {
                    log.info("消息已重复处理失败,拒绝再次接收");
                    // 拒绝消息，requeue=false 表示不再重新入队，如果配置了死信队列则进入死信队列
                    channel.basicReject(deliveryTag, false);
                } else {
                    log.info("消息即将再次返回队列处理");
                    // 否认消息
                    // deliveryTag:该消息的index
                    // multiple：是否批量，true:将一次性拒绝所有小于deliveryTag的消息
                    // requeue：被拒绝的是否重新入队列
                    channel.basicNack(deliveryTag, false, true);
                }
            } catch (IOException ioe) {
                log.error(BasicException.exceptionTrace(ioe));
            }
        }
    }
}
