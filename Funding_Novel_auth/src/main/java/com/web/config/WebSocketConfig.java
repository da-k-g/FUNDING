package com.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker  //WebSocket 메시지 브로커를 활성화 // STOMP 프로토콜을 지원하도록 Spring WebSocket 설정을 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	//클라이언트가 WebSocket에 연결할 엔드포인트를 등록합니다				//SockJS 폴리필을 활성화
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();  // 모든 출처 cors 허용
        //WebSocket 연결의 엔드포인트를 /ws로 정의    //클라이언트는 /ws를 통해 WebSocket 연결을 시도
    }

    @Override
    public void configureMessageBroker(org.springframework.messaging.simp.config.MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // 클라이언트에게 보낼 경로
        registry.setApplicationDestinationPrefixes("/app"); // 클라이언트가 메시지를 보낼 경로
    }
}
