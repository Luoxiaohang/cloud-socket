package com.fd.cloudsocket;

import com.alibaba.fastjson.JSONObject;
import com.fd.cloudsocket.client.MyWebSocketClient;
import org.java_websocket.WebSocket;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class CloudSocketApplicationTests {

    @Test
    void contextLoads() {

    }

    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        List<MyWebSocketClient> clientList = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(100);
        int clientNum = 50;
        int messageNum = 50;
        for (int clientId = 0; clientId < clientNum; clientId++) {
            int finalClientId = clientId;
            service.submit(() -> {
                // 建立socket连接
                MyWebSocketClient client = null;
                try {
                    client = new MyWebSocketClient(new URI("ws://192.168.0.47:8081/zfb/cloud-socket/websocket"),finalClientId);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                client.connect();
                //等待服务端响应
                while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                    System.out.println("连接中···请稍后");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //向WebSocket服务端发送数据
                Map<String, Object> map = new HashMap<>();
                Random random = new Random();
                for (int i = 0; i < messageNum; i++) {
                    map.put("message", finalClientId +" 客户端 发送消息id:" + i);
                    client.send(JSONObject.toJSONString(map));
                }
                //等待WebSocket服务端响应
                String message = null;
                while ((message = client.getExcptMessage()) == null) {
                    System.out.println("服务忙等待...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //打印服务端返回的数据
                System.out.println("成功发送数据：" + message);
                clientList.add(client);
                //关闭连接
                // client.close();
            });
        }
    }

}
