package com.example.provider.service.primary;

import com.example.provider.model.primary.MqMessage;
import com.example.provider.utils.Responses;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface IProviderService {

    Responses helloSendMsg(MqMessage message);

    Responses topicExchange1SendMsg(MqMessage message) throws JsonProcessingException;

    Responses topicExchange2SendMsg(MqMessage message) throws JsonProcessingException;

    Responses fanoutSendMsg(MqMessage message);

    Responses directQueue1SendMsg(MqMessage message) throws JsonProcessingException;

    Responses directQueue2SendMsg(MqMessage message) throws JsonProcessingException;

    Responses headersSendMsg(MqMessage message);

    Responses topicExchange1SendErrorMsg(MqMessage message) throws JsonProcessingException;
}
