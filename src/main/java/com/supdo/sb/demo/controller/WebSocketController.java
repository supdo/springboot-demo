package com.supdo.sb.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supdo.sb.demo.entity.SysUser;
import com.supdo.sb.demo.plugin.websocket.SocketSessionRegistry;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RequiresAuthentication
@Controller
@RequestMapping("/websocket")
public class WebSocketController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private SimpUserRegistry userRegistry;
    @Autowired
    SocketSessionRegistry webAgentSessionRegistry;

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }


    @RequiresAuthentication
    @GetMapping("/hello")
    public String helloView(Principal principal, Map<String, Object> map){
        String username = principal.getName();
        Subject subject = SecurityUtils.getSubject();
        Object o = subject.getPrincipal();
        map.put("currentUserName",username);
        return render("/websocket/hello");
    }

    @MessageMapping("/hello")
    @SendTo("/topic")
    public Result hello(Principal principal, String message){
        String username = principal.getName();
        JSONObject info = JSON.parseObject(message);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        result.simple(true, String.format("%s：%s。@%s", username, info.get("msg").toString(), format.format(now.getTime())));
        return result;
    }

    @SubscribeMapping("/user/oto/notifications")
    public Result otoNotifications(Principal principal){
        String username = principal.getName();
        result.simple(true,String.format("%s订阅了我。",username));
        return result;
    }

    @MessageMapping("sayToUser")
    public void sayToUser(Principal principal, String message){
        JSONObject info = JSON.parseObject(message);
        String username = principal.getName();

        //这里没做校验
        String sessionId=webAgentSessionRegistry.getSessionIds(info.get("toUser").toString()).stream().findFirst().get();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        result.simple(true,String.format("%s：%s", username, info.get("msg").toString(), format.format(now.getTime())));

        simpMessagingTemplate.convertAndSendToUser(
                info.get("toUser").toString(),
                "/oto/notifications",
                result,
                createHeaders(sessionId)
        );
    }
}
