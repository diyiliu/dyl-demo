package com.diyilu.ws.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Description: WebSocket
 * Author: DIYILIU
 * Update: 2020-11-18 15:38
 */

@Slf4j
@Component
@ServerEndpoint("/binding-ws")
public class WebSocket {

    private Session session;

    private static Set<WebSocket> webSockets = new CopyOnWriteArraySet<>();

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }


    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSockets.add(this);
        log.info("【新建连接】，连接总数:{}", webSockets.size());
    }

    /**
     * 断开连接
     */
    @OnClose
    public void onClose() {
        webSockets.remove(this);
        log.info("【断开连接】，连接总数:{}", webSockets.size());
    }

    /**
     * 接收到信息
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("【收到】，客户端的信息:{}，连接总数:{}", message, webSockets.size());
    }

    /**
     * 发送消息
     *
     * @param message
     */
    public void sendMessage(String message) {
        log.info("【广播发送】，信息:{}，总连接数:{}", message, webSockets.size());
        for (WebSocket webSocket : webSockets) {
            try {
                webSocket.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.info("【广播发送】，信息异常:{}", e.fillInStackTrace());
            }
        }
    }
}