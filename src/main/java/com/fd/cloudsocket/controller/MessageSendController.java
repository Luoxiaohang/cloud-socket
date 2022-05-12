package com.fd.cloudsocket.controller;

import com.fd.cloudsocket.message.WebSocketMessageStream;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/message")
public class MessageSendController {

    @Resource
    private WebSocketMessageStream webSocketMessageStream;

    @GetMapping("/send")
    public Map sendMessage(@RequestParam(required = false) String targetUserId){
        Map<String,Object> map = new HashMap<>();
        map.put("shopId","123");
        map.put("targetUserId",targetUserId);
        Message message = MessageBuilder.withPayload(map).build();
        webSocketMessageStream.outPut().send(message);
        Map<String,Object> result = new HashMap<>();
        result.put("code",0);
        result.put("success",true);
        return result;
    }

}
