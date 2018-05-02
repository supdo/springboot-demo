package com.supdo.sb.demo.plugin.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;

public class STOMPConnectEventListener implements ApplicationListener<SessionConnectEvent> {
    @Autowired
    SocketSessionRegistry webAgentSessionRegistry;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        //String agentId = sha.getNativeHeader("login").get(0);
        String sctionName = "";
        String username = event.getUser().getName();
        String sessionId = sha.getSessionId();
        if(sha.getCommand().equals(StompCommand.CONNECTED)) {
            sctionName = "连接了系统";
            webAgentSessionRegistry.registerSessionId(username, sessionId);
        } else if(sha.getCommand().equals(StompCommand.DISCONNECT)) {
            sctionName = "退出了系统";
            webAgentSessionRegistry.unregisterSessionId(username, sessionId);
        }
        simpMessagingTemplate.convertAndSendToUser("kukei","/topic", String.format("%s%s。", username, sctionName));
    }
}
