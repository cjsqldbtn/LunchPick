package com.nh.lunch.chatmessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.CloseReason;
import jakarta.websocket.CloseReason.CloseCodes;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint("/broadcasting")
public class BroadSocket {
	
	public static Map<String, Set<Session>> roomClients = new HashMap<>();
    
	// 파라미터로 넘어오는 해당 roomKey 얻는 함수. 
	private String getRoomKey(Session session) {
        Map<String, List<String>> params = session.getRequestParameterMap();
        if (params.containsKey("roomKey") && !params.get("roomKey").isEmpty()) {
            return params.get("roomKey").get(0);
        }
        return null; // 방 키가 없을 경우 null 리턴.
    }
	
    // 새로 접속했을 떄.
    @OnOpen
    public void onOpen(Session session) throws Exception {
    	String roomKey = getRoomKey(session);
    	System.out.println("(onOpen) room key : " + roomKey);
    	// 키 값이 리턴이 안됐으면.
    	if (roomKey == null || roomKey.trim().isEmpty()) { 
            System.out.println(" [입장 거부] 유효하지 않은 방 키 접근");
            session.close(new CloseReason(CloseCodes.CANNOT_ACCEPT, "EMPTY_ROOM_KEY"));
            return;
        }
    	// 그 현재 존재하는 방에 접속.
    	Set<Session> existingSet = roomClients.get(roomKey);
    	existingSet.add(session);
    	
        System.out.println("새로운 클라이언트 IN : 현재 " + existingSet.size() + "명.");
    }

    // 메시지를 받았을 때.
    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
    	String roomKey = getRoomKey(session);
        Set<Session> roomMembers = roomClients.get(roomKey);

        // 방이 존재하고 방 안에 유저들이 있는 경우에만 메시지 전달
        if (roomMembers != null) {
            ObjectMapper mapper = new ObjectMapper();
            
            Map<String, Object> msgMap = mapper.readValue(message, Map.class);
            String jsonPayload = mapper.writeValueAsString(msgMap);
            for (Session client : roomMembers) {
                    client.getBasicRemote().sendText(jsonPayload);
            }
        }
    }

    // 접속이 끊어졌을 때. 
    @OnClose
    public void onClose(Session session) throws Exception {
    	String roomKey = getRoomKey(session);
        Set<Session> roomMembers = roomClients.get(roomKey);

        if (roomMembers != null) {
            roomMembers.remove(session); // 해당 방의 세션 목록에서 나간 사람 제거
            if (roomMembers.isEmpty()) { // 방에 남아있는 사람이 아무도 없으면, Map에서 방 삭제
                roomClients.remove(roomKey);
                System.out.println("[" + roomKey + "] 방의 모든 인원이 퇴장하여 방이 삭제되었습니다.");
            } else { // 방에 남은 사람이 있다면, 남은 사람들에게만 퇴장 알림 전송
                System.out.println("[" + roomKey + "] 클라이언트 OUT : 현재 " + roomMembers.size() + "명.");
                for (Session client : roomMembers) {
                    client.getBasicRemote().sendText("누군가 나갔습니다!");
                }
            }
        }
    }
    
    // 에러 났을 떄. 
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("에러 발생: " + throwable.getMessage());
    }
}