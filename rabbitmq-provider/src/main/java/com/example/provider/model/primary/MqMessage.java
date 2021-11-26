package com.example.provider.model.primary;

import lombok.Data;

import java.util.Date;

@Data
public class MqMessage {

    private String message;

    private String senderName;

    private Date sendDate;

    private String sendIp;

}
