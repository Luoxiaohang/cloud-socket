package com.fd.cloudsocket.handler;

import com.fd.cloudsocket.message.WebSocketMessageStream;
import com.fd.cloudsocket.service.SocketMessageSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(WebSocketMessageStream.class)
public class MqMessageHandler {

    private Logger log = LoggerFactory.getLogger(WebSocketMessageHandler.class);

    @Autowired
    private SocketMessageSendService socketMessageSendService;

    @StreamListener(WebSocketMessageStream.WEBSOCKET_MESSAGE_IN)
    private void messageReceived(Message<String> message) {
        log.info("收到消息：{}", message.getPayload());
        log.info("开始转发到socket连接");
        socketMessageSendService.sendTextMessage(message.getPayload());

    }
}
