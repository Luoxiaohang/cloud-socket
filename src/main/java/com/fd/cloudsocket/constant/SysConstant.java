package com.fd.cloudsocket.constant;

import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

public class SysConstant {
    public static final ConcurrentHashMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();



}
