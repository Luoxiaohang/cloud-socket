package com.fd.cloudsocket.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface WebSocketMessageStream {

    public final String WEBSOCKET_MESSAGE_IN= "webSocketMessageIn";
    public final String WEBSOCKET_MESSAGE_OUT= "webSocketMessageOut";

    @Input(WEBSOCKET_MESSAGE_IN)
    SubscribableChannel input();

    @Output(WEBSOCKET_MESSAGE_OUT)
    SubscribableChannel outPut();

}
