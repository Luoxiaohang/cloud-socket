package com.fd.cloudsocket.config;

import com.fd.cloudsocket.handler.WebSocketMessageHandler;
import com.fd.cloudsocket.interceptor.WebSocketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
public class WebSocketAutoConfig implements WebSocketConfigurer {

    @Autowired
    private WebSocketMessageHandler webSocketMessageHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册websocket组件 添加处理器和拦截器
        // websocket是websocket服务器的请求路径可以自己定义
        registry.addHandler(webSocketMessageHandler, "/websocket")
                // 指定自定义拦截器
                .addInterceptors(new WebSocketInterceptor())
                // 允许跨域
                .setAllowedOrigins("*");
        // 在某些低版本的浏览器中不支持websocket可以用sock-js替代
        registry.addHandler(webSocketMessageHandler, "/sock-js")
                .addInterceptors(new WebSocketInterceptor())
                .setAllowedOrigins("*")
                // 开启sockJs支持
                .withSockJS();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
