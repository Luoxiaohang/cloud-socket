package com.fd.cloudsocket.service;

import com.alibaba.fastjson.JSONObject;
import com.fd.cloudsocket.constant.SysConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;

@Service
public class SocketMessageSendService {

    private Logger log = LoggerFactory.getLogger(SocketMessageSendService.class);

    public void sendTextMessage(String content) {
        JSONObject messageJson = JSONObject.parseObject(content);
        String targerUserId = messageJson.getString("targetUserId");
        TextMessage socketMessage = new TextMessage(content);
        for (
                Map.Entry<String, WebSocketSession> entry : SysConstant.sessionMap.entrySet()) {
            try {
                if (ObjectUtils.isEmpty(targerUserId) || entry.getKey().equals(targerUserId)) {
                    log.info("开始发送socket消息，to sessionId:{},消息内容：{}",entry.getKey(),content);
                    entry.getValue().sendMessage(socketMessage);
                }
            } catch (IOException e) {
                log.error("转发消息至webSocket失败");
                e.printStackTrace();
            }
        }
    }
}
