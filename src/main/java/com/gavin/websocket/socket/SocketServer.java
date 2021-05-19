package com.gavin.websocket.socket;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;

/**
 * @Author jiwen.cao
 * @Date 2021/5/19
 * @Description
 */
@Slf4j
@ServerEndpoint("/socket/{param}")
@Component
public class SocketServer {

    @OnOpen
    public void onOpen(@PathParam("param") String param, Session session) {
        if (StringUtils.isBlank(param)) {
            return;
        }
        log.info("websocket服务连接开启", param);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        if ("ping".equals(message)) {
            // 心跳
            session.getBasicRemote().sendText("pong");
        } else {
            //进行消息分发
            session.getBasicRemote().sendText(message);
        }
    }

    @OnClose
    public void onClose(Session session) {
        log.info("关闭socket连接");

    }

    @OnError
    public void onError(Session session, Throwable error) {
        Map<String, String> pathParameters = session.getPathParameters();
        String userId = pathParameters.get("userId");
        log.info("用户：{}的webSocket 连接异常", userId);
    }
}
