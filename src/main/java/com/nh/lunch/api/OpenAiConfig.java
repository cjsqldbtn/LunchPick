package com.nh.lunch.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;

@PropertySource("classpath:secret.properties")
@Configuration
public class OpenAiConfig {
	@Value("${openai.api.key}")
    private String openaiApiKey;
	
	@Bean
	public OpenAIClient openAiClient() {
		 return OpenAIOkHttpClient.builder()
	                .apiKey(openaiApiKey)
	                .build();
	}
}
