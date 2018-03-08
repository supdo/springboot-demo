package com.supdo.sb.demo.controller.admin;

import com.supdo.sb.demo.entity.SysRole;
import com.supdo.sb.demo.service.SysPermissionService;
import com.supdo.sb.demo.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    SysPermissionService sysPermissionService;

    @GetMapping("/list")
    public String listView(Map<String, Object> map) {
        List<SysRole> roles = sysRoleService.findAll();
        map.put("roles", roles);

        SysRole role = new SysRole();
        role.initForm(SysRole.IRole.class);
        map.put("role", role);

        List<SysRole> permissionList = sysRoleService.findAll();
        map.put("permissionList", permissionList);

        return render("admin/roleList");
    }
}
