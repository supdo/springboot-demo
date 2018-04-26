package com.supdo.sb.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supdo.sb.demo.entity.SysUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
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

    @GetMapping("/hello")
    public String helloView(Principal principal, Map<String, Object> map){
        //Class aaa = SecurityUtils.getSubject().getPrincipal().getClass();
        //SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        return render("/websocket/hello");
    }

    @MessageMapping("/hello")
    @SendTo("/topic")
    public Result hello(Principal principal, String message){
        result.simple(true, message);
        JSONObject info = JSON.parseObject(message);
        //SysUser user = (SysUser)SecurityUtils.getSubject().getPrincipal();
        String name = principal.getName();
        simpMessagingTemplate.convertAndSendToUser(info.get("toUser").toString(),
                "/oto/notifications",
                String.format("%s：%s", principal.getName(), info.get("msg").toString()));
        return result;
    }
}
