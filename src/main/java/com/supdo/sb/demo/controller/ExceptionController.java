package com.supdo.sb.demo.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.supdo.sb.demo.plugin.Result;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@ControllerAdvice
public class ExceptionController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
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

	@ResponseBody
	@ExceptionHandler(Exception.class)
	public Result handler(HttpServletRequest req, HttpServletResponse res, Exception e) {
		logger.info("Restful Http请求发生异常...");

		Result result = new Result(false, "异常了。");

		if (res.getStatus() == HttpStatus.BAD_REQUEST.value()) {
			logger.info("修改返回状态值为200");
			res.setStatus(HttpStatus.OK.value());
		}

//		if (e instanceof NullPointerException) {
//			logger.error("代码00：" + e.getMessage(), e);
//			return ResultEntity.fail("发生空指针异常");
//		} else if (e instanceof IllegalArgumentException) {
//			logger.error("代码01：" + e.getMessage(), e);
//			return ResultEntity.fail("请求参数类型不匹配");
//		} else if (e instanceof SQLException) {
//			logger.error("代码02：" + e.getMessage(), e);
//			return ResultEntity.fail("数据库访问异常");
//		} else {
//			logger.error("代码99：" + e.getMessage(), e);
//			return ResultEntity.fail("服务器代码发生异常,请联系管理员");
//		}

		String trace = e.getStackTrace().toString();
		return result.simple(false, trace);
	}
}
