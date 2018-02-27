package com.supdo.sb.demo.controller.admin;

import com.supdo.sb.demo.entity.SysUser;
import com.supdo.sb.demo.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
