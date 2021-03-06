package com.diyilu.ws.controller;

import com.diyilu.ws.modules.MessageBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * Description: MessageController
 * Author: diyiliu
 * Update: 2020-11-17 17:27
 */

@Controller
public class MessageController {

    /** 消息发送工具对象 */
    @Resource
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Resource
    private WebSocket webSocket;


    /** 广播发送消息，将消息发送到指定的目标地址 */
    @MessageMapping("/test")
    public void sendTopicMessage(MessageBody messageBody) {
        // 将消息发送到 WebSocket 配置类中配置的代理中（/topic）进行消息转发
        simpMessageSendingOperations.convertAndSend(messageBody.getDestination(), messageBody);

        webSocket.sendMessage(messageBody.getContent());
    }
}