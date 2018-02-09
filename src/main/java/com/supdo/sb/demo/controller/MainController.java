package com.supdo.sb.demo.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supdo.sb.demo.entity.SysUser;
import com.supdo.sb.demo.service.SysUserService;

@RequiresAuthentication
@Controller
public class MainController extends BaseController {

	@Autowired
	SysUserService sysUserService;

	@RequestMapping("test")
	public String testView(Map<String, Object> map) {
		return render("test");
	}
	
	//@RequiresAuthentication
	@RequestMapping({"/", "/default"})
	public String defaultView(Map<String, Object> map) {
		map.put("hello", "Hello World!");
		//map.put("test", "test");
		return render("default");
	}
	
	@PostMapping("/login")
	public String signin(@Validated(SysUser.IUserLogin.class) SysUser userForm, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse reponse, Map<String, Object> map) {
		userForm.initForm(SysUser.IUserLogin.class);
		if (bindingResult.hasErrors()) {
			map.put("user", userForm.initFieldErrors(bindingResult));
            return "login";
        }
		String verifycode=request.getParameter("verifycode");
		if(!verifycode.equals(request.getSession().getAttribute("VerifyCode"))) {
			map.put("verifycodeError", "验证码不正确!");
			map.put("user", userForm);
			return "login";
		}
		try {
			UsernamePasswordToken token = new UsernamePasswordToken(userForm.getUsername(), userForm.getPassword());
			SecurityUtils.getSubject().login(token);
			return "redirect:index";
		} catch (Exception e) {
			System.out.println(e.toString());
			userForm.getFields().get("password").setError(e.getMessage());
			map.put("user", userForm);
			return "login";
		}
	}
	
	@GetMapping("/login")
	public String signinView(Map<String, Object> map) {
		SysUser user = new SysUser();
		user.initForm(SysUser.IUserLogin.class);
		map.put("user", user);
		return render("login");
	}
	
	@PostMapping(path="/logon")
	@ResponseBody
	public Result register (@Validated(SysUser.IUser.class) SysUser userForm, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse reponse) {
		userForm.initForm(SysUser.IUser.class);
		if (bindingResult.hasErrors()) {
			this.result.putItems("user", userForm.initFieldErrors(bindingResult));
			this.result.simple(false, "校验失败");
        }else {
        	String verifycode=request.getParameter("verifycode");
    		if(!verifycode.equals(request.getSession().getAttribute("VerifyCode"))) {
    			this.result.putItems("verifycodeError", "验证码不正确!");
    			this.result.putItems("user", userForm);
    			this.result.simple(false, "验证码不正确!");
    		}else {
    			String encryptPassword = new SimpleHash("SHA-1", userForm.getPassword(), userForm.getUsername()).toString();
    			userForm.setPassword(encryptPassword);
	        	userForm.setStatus("1");
	        	userForm.setCreateTime(new Timestamp(new Date().getTime()));
	        	sysUserService.save(userForm);
	        	this.result.simple(true, "保存成功!");
	        	this.result.putItems("user", userForm);
    		}
        }
		return result;
	}
	
	@GetMapping("/logon")
	public String registerView(Map<String, Object> map) throws Exception {
		SysUser user = new SysUser();
		user.initForm(SysUser.IUser.class);
		map.put("user", user);
		return render("logon");
	}

	@GetMapping("/logon_json")
	@ResponseBody
	public Result logonView(Map<String, Object> map) throws Exception {
		SysUser user = new SysUser();
		user.initForm(SysUser.IUser.class);
		this.result.simple(true, "");
		this.result.putItems("user", user);
		return result;
	}

	@RequestMapping(path="/logout")
	@ResponseBody
	public Result logout() {
		SecurityUtils.getSubject().logout();
		this.result = new Result(true, "退出成功！");
		return this.result;
	}
}
