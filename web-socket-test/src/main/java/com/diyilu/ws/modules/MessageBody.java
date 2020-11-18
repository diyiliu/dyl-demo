package com.diyilu.ws.modules;

import lombok.Data;

/**
 * Description: MessageBody
 * Author: diyiliu
 * Update: 2020-11-17 17:25
 */

@Data
public class MessageBody {
    /** 发送消息的用户 */
    private String from;
    /** 消息内容 */
    private String content;
    /** 目标用户（告知 STOMP 代理转发到哪个用户） */
    private String targetUser;
    /** 广播转发的目标地址（告知 STOMP 代理转发到哪个地方） */
    private String destination;
}