package com.fd.cloudsocket.handler;

import com.alibaba.fastjson.JSONObject;
import com.fd.cloudsocket.constant.SysConstant;
import com.fd.cloudsocket.message.WebSocketMessageStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Component
public class WebSocketMessageHandler extends AbstractWebSocketHandler{

    private Logger log = LoggerFactory.getLogger(WebSocketMessageHandler.class);

    @Autowired
    private WebSocketMessageStream webSocketMessageStream;

    /**
     * websocket连接创建后调用
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("收到连接请求：{}", session.getId());
        SysConstant.sessionMap.put(session.getId(), session);
        TextMessage socketMessage = new TextMessage("连接成功，socketId:" + session.getId());
        session.sendMessage(socketMessage);
        super.afterConnectionEstablished(session);
    }

    /**
     * 接收到websocket消息调用
     * @param currentSession
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession currentSession, TextMessage message) throws Exception {
        log.info("Socket 收到text 消息：{},并开始发送到消息队列",message);
        // 推送到消息队列
        JSONObject param = JSONObject.parseObject(message.getPayload());
        // 添加消息标识符
        param.put("fromSessionId",currentSession.getId());
        Message mqMessage = MessageBuilder.withPayload(param.toJSONString()).build();
        webSocketMessageStream.outPut().send(mqMessage);
    }

    /**
     * 连接关闭调用
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        SysConstant.sessionMap.remove(session.getId());
        super.afterConnectionClosed(session, status);
    }

    /**
     * 连接出错调用
     * @param session
     * @param exception
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        SysConstant.sessionMap.remove(session.getId());
        super.handleTransportError(session, exception);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void sendMessage(){

    }
}
