package com.nh.lunch.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nh.lunch.chatmessage.BroadSocket;
import com.nh.lunch.menu.MenuDto;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

@RestController
public class ApiController {
	@Autowired
	ApiService aSvc;
	
	@PostMapping("/ai/recommend")
	public void recommend(@RequestBody AiMenuRequestDto request) throws Exception {
		String roomKey = request.getRoomKey();

	    // 이미 같은 방에서 AI 추천 중이면 거절
	    if (!BroadSocket.aiLock.add(roomKey)) {
	        throw new IllegalStateException("이미 AI 추천을 진행 중입니다.");
	    }
	    
	    try {
	        AiMenuDto recommendation = aSvc.recommendMenu(request);
	        BroadSocket.broadcastAiRecommend(roomKey, recommendation);
	    } finally {
	        // 성공하든 실패하든 반드시 잠금 해제
	        BroadSocket.aiLock.remove(roomKey);
	    }
	}
}
