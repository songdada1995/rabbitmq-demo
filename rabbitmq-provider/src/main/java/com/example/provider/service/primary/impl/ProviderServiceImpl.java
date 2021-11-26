package com.example.provider.service.primary.impl;

import com.example.provider.config.rabbitmq.exchange.RabbitMqDirectExchangeConfig;
import com.example.provider.config.rabbitmq.exchange.RabbitMqTopicExchangeConfig;
import com.example.provider.model.primary.MqMessage;
import com.example.provider.service.primary.IProviderService;
import com.example.provider.utils.Responses;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.amqp.core.MessageProperties.CONTENT_TYPE_JSON;

@Slf4j
@Service
public class ProviderServiceImpl implements IProviderService {

    private static AtomicInteger count = new AtomicInteger(0);

    @Value("${server.port}")
    private String port;

    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource(name = "topicExchange1")
    private TopicExchange topicExchange1;
    @Resource(name = "topicExchange2")
    private TopicExchange topicExchange2;


    @Override
    public Responses helloSendMsg(MqMessage mqMessage) {
        log.info("第" + count.incrementAndGet() + "次调用接口，端口：" + port);

        return Responses.newInstance().succeed("执行成功");
    }

    @Override
    public Responses topicExchange1SendMsg(MqMessage mqMessage) throws JsonProcessingException {
        log.info("第" + count.incrementAndGet() + "次调用接口，端口：" + port);

        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("msg", mqMessage.getMessage());
        msgMap.put("port", port);
        msgMap.put("count", count.get());
        msgMap.put("success", true);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(msgMap);
        MessageProperties messageProperties = new MessageProperties();
        // 这里注意一定要修改contentType为 application/json
        messageProperties.setContentType("application/json");
        Message message = new Message(json.getBytes(), messageProperties);

        rabbitTemplate.convertAndSend(
                RabbitMqTopicExchangeConfig.TOPIC_EXCHANGE_NAME_1,
                RabbitMqTopicExchangeConfig.TOPIC_QUEUE_1_ROUTING_KEY,
                message,
                new CorrelationData(UUID.randomUUID().toString()));

        log.info("发送topicExchange1消息：'" + mqMessage.getMessage() + "'");

        return Responses.newInstance().succeed("执行成功");
    }

    @Override
    public Responses topicExchange1SendErrorMsg(MqMessage mqMessage) throws JsonProcessingException {
        log.info("第" + count.incrementAndGet() + "次调用接口，端口：" + port);

        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("msg", mqMessage.getMessage());
        msgMap.put("port", port);
        msgMap.put("count", count.get());
        msgMap.put("success", false);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(msgMap);
        MessageProperties messageProperties = new MessageProperties();
        // 这里注意一定要修改contentType为 application/json
        messageProperties.setContentType(CONTENT_TYPE_JSON);
        Message message = new Message(json.getBytes(), messageProperties);

        rabbitTemplate.convertAndSend(
                RabbitMqTopicExchangeConfig.TOPIC_EXCHANGE_NAME_1,
                RabbitMqTopicExchangeConfig.TOPIC_QUEUE_1_ROUTING_KEY,
                message,
                new CorrelationData(UUID.randomUUID().toString()));

        log.info("发送topicExchange1消息：'" + mqMessage.getMessage() + "'");

        return Responses.newInstance().succeed("执行成功");
    }

    @Override
    public Responses topicExchange2SendMsg(MqMessage mqMessage) throws JsonProcessingException {
        log.info("第" + count.incrementAndGet() + "次调用接口，端口：" + port);

        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("msg", mqMessage.getMessage());
        msgMap.put("port", port);
        msgMap.put("count", count.get());
        msgMap.put("success", false);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(msgMap);
        MessageProperties messageProperties = new MessageProperties();
        // 这里注意一定要修改contentType为 application/json
        messageProperties.setContentType(CONTENT_TYPE_JSON);
        Message message = new Message(json.getBytes(), messageProperties);

        rabbitTemplate.convertAndSend(
                RabbitMqTopicExchangeConfig.TOPIC_EXCHANGE_NAME_2,
                RabbitMqTopicExchangeConfig.TOPIC_QUEUE_2_ROUTING_KEY,
                message,
                new CorrelationData(UUID.randomUUID().toString()));

        log.info("发送" + RabbitMqTopicExchangeConfig.TOPIC_EXCHANGE_NAME_2 + "消息：'" + mqMessage.getMessage() + "'");

        return Responses.newInstance().succeed("执行成功");
    }

    @Override
    public Responses fanoutSendMsg(MqMessage mqMessage) {
        log.info("第" + count.incrementAndGet() + "次调用接口，端口：" + port);

        return Responses.newInstance().succeed("执行成功");
    }

    @Override
    public Responses directQueue1SendMsg(MqMessage mqMessage) throws JsonProcessingException {
        log.info("第" + count.incrementAndGet() + "次调用接口，端口：" + port);

        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("msg", mqMessage.getMessage());
        msgMap.put("port", port);
        msgMap.put("count", count.get());
        msgMap.put("success", true);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(msgMap);
        MessageProperties messageProperties = new MessageProperties();
        // 这里注意一定要修改contentType为 application/json
        messageProperties.setContentType(CONTENT_TYPE_JSON);
        Message message = new Message(json.getBytes(), messageProperties);

        rabbitTemplate.convertAndSend(
                RabbitMqDirectExchangeConfig.DIRECT_EXCHANGE,
                RabbitMqDirectExchangeConfig.DIRECT_QUEUE_1_ROUTING_KEY_B,
                message,
                new CorrelationData(UUID.randomUUID().toString()));

        log.info("发送" + RabbitMqDirectExchangeConfig.DIRECT_EXCHANGE + "消息：'" + mqMessage.getMessage() + "'");

        return Responses.newInstance().succeed("执行成功");
    }

    @Override
    public Responses directQueue2SendMsg(MqMessage mqMessage) throws JsonProcessingException {
        log.info("第" + count.incrementAndGet() + "次调用接口，端口：" + port);

        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("msg", mqMessage.getMessage());
        msgMap.put("port", port);
        msgMap.put("count", count.get());
        msgMap.put("success", true);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(msgMap);
        MessageProperties messageProperties = new MessageProperties();
        // 这里注意一定要修改contentType为 application/json
        messageProperties.setContentType(CONTENT_TYPE_JSON);
        Message message = new Message(json.getBytes(), messageProperties);

        rabbitTemplate.convertAndSend(
                RabbitMqDirectExchangeConfig.DIRECT_EXCHANGE,
                RabbitMqDirectExchangeConfig.DIRECT_QUEUE_2_ROUTING_KEY_B,
                message,
                new CorrelationData(UUID.randomUUID().toString()));

        log.info("发送" + RabbitMqDirectExchangeConfig.DIRECT_EXCHANGE + "消息：'" + mqMessage.getMessage() + "'");

        return Responses.newInstance().succeed("执行成功");
    }

    @Override
    public Responses headersSendMsg(MqMessage mqMessage) {
        log.info("第" + count.incrementAndGet() + "次调用接口，端口：" + port);

        return Responses.newInstance().succeed("执行成功");
    }

}
