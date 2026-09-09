package com.nh.lunch.chatmessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint("/broadcasting")
public class BroadSocket {
	// 방번호로 채팅 나누기
	public static Map<String, Set<Session>> roomClients = new HashMap<>();
	
	// 세션 별 사용자 정보 (memberId, nickName)
	private static final Map<Session, ChatUser> users = new HashMap<>();
	
	private final ObjectMapper mapper = new ObjectMapper();
    
	// 파라미터로 넘어오는 해당 roomKey 얻는 함수. 
	private String getRoomKey(Session session) {
        Map<String, List<String>> params = session.getRequestParameterMap();
        if (params.containsKey("roomKey") && !params.get("roomKey").isEmpty()) {
            return params.get("roomKey").get(0);
        }
        return null; // 방 키가 없을 경우 null 리턴.
    }
	
	// 파라미터 받는 함수
	private String getParameter(Session session, String name) {
	    if (session.getRequestParameterMap().containsKey(name)) {
	        return session.getRequestParameterMap().get(name).get(0);
	    }
	    return null;
	}
	
    // 새로 접속했을 떄.
	@OnOpen
    public void onOpen(Session session) throws Exception {
        String roomKey = getRoomKey(session);
        // roomKey가 없는 경우
        if (roomKey == null || roomKey.trim().isEmpty()) {
            session.close();
            return;
        }
        // 사용자 정보 가져오기
        String memberIdParam = getParameter(session, "memberId");
        String nickName = getParameter(session, "nickName");
        Integer memberId = null;
        if (memberIdParam != null) {
            memberId = Integer.valueOf(memberIdParam);
        }

        // 사용자 저장
        users.put(session, new ChatUser(memberId, nickName));
        
        // 방에 추가
        roomClients.computeIfAbsent(roomKey, key -> new HashSet<>()).add(session);
        System.out.println("[" + roomKey + "] 클라이언트 IN : 현재 " + roomClients.get(roomKey).size() + "명.");
    }

    // 메시지를 받았을 때.
    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        String roomKey = getRoomKey(session);
        Set<Session> roomMembers = roomClients.get(roomKey);
        if (roomMembers == null) { return; }
        Map<String, Object> received = mapper.readValue(message, Map.class);
        
        ChatUser user = users.get(session);

        Map<String, Object> chatMessage = new HashMap<>();

        chatMessage.put("type", "CHAT");
        chatMessage.put("senderId", user != null ? user.getMemberId() : null);
        chatMessage.put("senderNick", user != null ? user.getNickName(): "알 수 없음");
        chatMessage.put("message",received.get("message"));
        String jsonPayload = mapper.writeValueAsString(chatMessage);
        
        for (Session client : roomMembers) {
            if (client.isOpen()) {
                client.getBasicRemote().sendText(jsonPayload);
            }
        }
    }

    // 접속이 끊어졌을 때. 
    @OnClose
    public void onClose(Session session) throws Exception {
        String roomKey = getRoomKey(session);
        Set<Session> roomMembers = roomClients.get(roomKey);

        ChatUser user = users.get(session);
        String nickName = "누군가";
        if (user != null && user.getNickName() != null) { nickName = user.getNickName(); }

        /*
         * 사용자 정보 삭제
         */
        users.remove(session);

        if (roomMembers != null) {

            //방에서 해당 사용자 제거
            roomMembers.remove(session);

            //방에 아무도 없으면 방 자체 삭제
            if (roomMembers.isEmpty()) {
                roomClients.remove(roomKey);
                System.out.println("[" + roomKey + "] 방의 모든 인원이 퇴장하여 방이 삭제되었습니다.");
            } else {
                System.out.println("[" + roomKey + "] " + nickName + " OUT : 현재 " + roomMembers.size() + "명.");
                
                //퇴장 메시지
                Map<String, Object> leaveMessage = new HashMap<>();
                leaveMessage.put("type", "LEAVE");
                leaveMessage.put("senderId", user != null ? user.getMemberId() : null);
                leaveMessage.put("senderNick", nickName);
                leaveMessage.put("message", nickName + "님이 나갔습니다.");


                String jsonPayload = mapper.writeValueAsString(leaveMessage);

                //남아있는 사람들에게만 전달
                for (Session client : roomMembers) {
                    if (client.isOpen()) {
                        client.getBasicRemote().sendText(jsonPayload);
                    }
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