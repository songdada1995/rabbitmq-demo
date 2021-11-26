package com.example.provider.web;

import com.example.provider.model.primary.MqMessage;
import com.example.provider.service.primary.IProviderService;
import com.example.provider.utils.Responses;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/provider")
public class ProviderController {

    @Resource
    private IProviderService providerService;

    @RequestMapping(value = "/hello/sendMsg", method = RequestMethod.POST)
    public Responses helloSendMsg(@RequestBody MqMessage message) {
        return providerService.helloSendMsg(message);
    }

    @RequestMapping(value = "/topic_exchange1/sendMsg", method = RequestMethod.POST)
    public Responses topicExchange1SendMsg(@RequestBody MqMessage message) throws JsonProcessingException {
        return providerService.topicExchange1SendMsg(message);
    }

    @RequestMapping(value = "/topic_exchange1/sendErrorMsg", method = RequestMethod.POST)
    public Responses topicExchange1SendErrorMsg(@RequestBody MqMessage message) throws JsonProcessingException {
        return providerService.topicExchange1SendErrorMsg(message);
    }

    @RequestMapping(value = "/topic_exchange2/sendMsg", method = RequestMethod.POST)
    public Responses topicExchange2SendMsg(@RequestBody MqMessage message) throws JsonProcessingException {
        return providerService.topicExchange2SendMsg(message);
    }

    @RequestMapping(value = "/fanout/sendMsg", method = RequestMethod.POST)
    public Responses fanoutSendMsg(@RequestBody MqMessage message) {
        return providerService.fanoutSendMsg(message);
    }

    @RequestMapping(value = "/direct/queue1/sendMsg", method = RequestMethod.POST)
    public Responses directQueue1SendMsg(@RequestBody MqMessage message) throws JsonProcessingException {
        return providerService.directQueue1SendMsg(message);
    }

    @RequestMapping(value = "/direct/queue2/sendMsg", method = RequestMethod.POST)
    public Responses directQueue2SendMsg(@RequestBody MqMessage message) throws JsonProcessingException {
        return providerService.directQueue2SendMsg(message);
    }

    @RequestMapping(value = "/headers/sendMsg", method = RequestMethod.POST)
    public Responses headersSendMsg(@RequestBody MqMessage message) {
        return providerService.headersSendMsg(message);
    }


}
