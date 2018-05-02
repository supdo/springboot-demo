package com.supdo.sb.demo.config;

import com.supdo.sb.demo.plugin.websocket.SocketSessionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler, "/endpointSpider");
    }

    private WebSocketHandler myWebSocketHandler = new WebSocketHandler() {

        @Autowired
        SocketSessionRegistry webAgentSessionRegistry;

        @Autowired
        private SimpMessagingTemplate simpMessagingTemplate;

        @Override
        public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
            webAgentSessionRegistry.registerSessionId(webSocketSession.getPrincipal().getName(), webSocketSession.getId());
        }

        @Override
        public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
            webSocketMessage.getPayload();
        }

        @Override
        public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

        }

        @Override
        public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
            webAgentSessionRegistry.unregisterSessionId(webSocketSession.getPrincipal().getName(), webSocketSession.getId());
        }

        @Override
        public boolean supportsPartialMessages() {
            return false;
        }
    };
}
