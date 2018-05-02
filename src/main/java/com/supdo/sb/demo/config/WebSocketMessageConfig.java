package com.supdo.sb.demo.config;

import com.supdo.sb.demo.plugin.websocket.STOMPConnectEventListener;
import com.supdo.sb.demo.plugin.websocket.SocketSessionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageConfig extends AbstractWebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/endpointSpider")
                //.setHandshakeHandler(myHandshakeHandler)
                .withSockJS()
                .setInterceptors(myHandshakeInterceptor);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {//配置消息代理(Message Broker)
        //广播式应配置一个/topic消息代理, /oto点对点的消息代理
        registry.enableSimpleBroker("/topic", "/oto");
    }

    @Bean
    public SocketSessionRegistry SocketSessionRegistry(){
        return new SocketSessionRegistry();
    }
    @Bean
    public STOMPConnectEventListener STOMPConnectEventListener(){
        return new STOMPConnectEventListener();
    }

//    private HandshakeHandler myHandshakeHandler = new HandshakeHandler() {
//        @Override
//        public boolean doHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> map) throws HandshakeFailureException {
//            if(request.getPrincipal() == null) {
//                response.setStatusCode(HttpStatus.UNAUTHORIZED);
//                return false;
//            } else {
//                return true;
//            }
//        }
//    };

    private HttpSessionHandshakeInterceptor myHandshakeInterceptor = new HttpSessionHandshakeInterceptor() {

        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
            super.beforeHandshake(request, response, wsHandler, attributes);
            //wsHandler = myWebSocketHandler;
            //如果用户没登录就返回HttpStatus.UNAUTHORIZED
            if(request.getPrincipal() == null) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                //response.getBody().write("ssssssssssss".getBytes());
                return false;
            } else {
                return true;
            }
        }
    };
}

