package com.supdo.sb.demo.controller.admin;

import com.supdo.sb.demo.entity.SysRole;
import com.supdo.sb.demo.entity.SysUser;
import com.supdo.sb.demo.service.SysRoleService;
import com.supdo.sb.demo.service.SysUserService;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysRoleService sysRoleService;

    @GetMapping("/list")
    public String listView(Map<String, Object> map) {
        List<SysUser> users = sysUserService.findAll();
        map.put("users", users);

        SysUser user = new SysUser();
        user.initForm(SysUser.IUser.class);
        map.put("user", user);

        List<SysRole> roleList = sysRoleService.findAll();
        map.put("roleList", roleList);

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
            String encryptPassword = new SimpleHash("SHA-1", userForm.getPassword(), userForm.getUsername()).toString();
            userForm.setPassword(encryptPassword);
            if(userForm.getId() == null){   //新增
                if(sysUserService.getListByUsername(userForm.getUsername()).size() > 0){
                    this.result.simple(false, "用户名已存在！");
                    userForm.getFields().get("username").setError("用户名已存在！");
                }else {
                    SysUser newObj = sysUserService.save(userForm);
                    this.result.simple(true, "保存成功！");
                    this.result.putItems("newObj", newObj);
                }
            }else { //更新
                SysUser user = sysUserService.findOne(userForm.getId());
                user.merge(userForm);
                SysUser newObj = sysUserService.save(user);
                this.result.simple(true, "保存成功！");
                this.result.putItems("newObj", newObj);
            }
            this.result.putItems("user", userForm);
        }
        return result;
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public Result delete(@PathVariable Long id) {
        sysUserService.delete(id);
        this.result.simple(true, "删除成功！");
        return result;
    }

    @GetMapping("/roleList/{id}")
    @ResponseBody
    public Result roleList(@PathVariable Long id){
        SysUser user = sysUserService.findOne(id);
        Set<SysRole> myRoles = user.getRoleList();
        result.simple(true, "成功！");
        result.putItems("myRoles", myRoles);
        return result;
    }
}
