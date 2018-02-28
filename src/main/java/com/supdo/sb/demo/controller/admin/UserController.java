package com.supdo.sb.demo.controller.admin;

import com.supdo.sb.demo.entity.SysUser;
import com.supdo.sb.demo.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    SysUserService sysUserService;

    @GetMapping("/list")
    public String listView(Map<String, Object> map) {
        List<SysUser> users = sysUserService.findAll();
        map.put("users", users);

        SysUser user = new SysUser();
        user.initForm(SysUser.IUser.class);
        map.put("user", user);
        return render("admin/userList");
    }

    @PostMapping("/add")
    @ResponseBody
    public Result save(@Validated(SysUser.IUser.class) SysUser userForm, BindingResult bindingResult,
                       HttpServletRequest request, HttpServletResponse reponse) {
        userForm.initForm(SysUser.IUser.class);
        if (bindingResult.hasErrors()) {
            this.result.simple(false, "校验失败！");
            this.result.putItems("user", userForm.initFieldErrors(bindingResult));
        }else {
            sysUserService.save(userForm);
            this.result.simple(true, "保存成功！");
            this.result.putItems("user", userForm);
        }
        return result;
    }
}