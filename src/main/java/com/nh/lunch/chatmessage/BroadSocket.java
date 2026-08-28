package com.nh.lunch.chatmessage;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint("/broadcasting")
public class BroadSocket {

    public static Set<Session> clients = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) throws Exception {
        clients.add(session);
        System.out.println("새로운 클라이언트 IN : 현재 " + clients.size() + "명.");
        for (Session client : clients) {
            client.getBasicRemote().sendText("누군가 들어왔습니다!");
        }
    }

    @OnMessage
    public void onMessage(String msg, Session session) throws Exception {
        System.out.println("클라이언트로부터 도착한 메시지 : " + msg);
        for (Session client : clients) {
            if (session != client) {
                client.getBasicRemote().sendText(msg);
            }
        }
    }

    @OnClose
    public void onClose(Session session) throws Exception {
        clients.remove(session); // 💡 기존 코드의 add -> remove 수정
        System.out.println("클라이언트 OUT : 현재 " + clients.size() + "명.");
        for (Session client : clients) {
            client.getBasicRemote().sendText("누군가 나갔습니다!");
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("에러 발생: " + throwable.getMessage());
    }
}