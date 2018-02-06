package com.supdo.sb.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import freemarker.template.Configuration;
import freemarker.template.Template;

@ControllerAdvice
public class ExceptionController {
	
	@Autowired  
	Configuration configuration;
	
	/**
	 * 用户未授权，即用户权限不足
	 * @param ex
	 * @return
	 * @throws Exception
	 */
	@ExceptionHandler(UnauthorizedException.class)
	@ResponseBody
	public String unauthorized(UnauthorizedException ex)  throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("error", ex.getMessage());
		Template t = configuration.getTemplate("403.ftl");
		String content = FreeMarkerTemplateUtils.processTemplateIntoString(t, resultMap);
		return content;
	}

	//
	/**
	 * 用户未鉴权,即用户没有登录。对于有角色和权限验证的函数，也得先登录。
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(UnauthenticatedException.class)
	public String unauthenticated(UnauthenticatedException ex) {
		return "redirect:login";
	}
}
