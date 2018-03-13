package com.supdo.sb.demo.controller.admin;

import com.supdo.sb.demo.entity.SysPermission;
import com.supdo.sb.demo.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/permission")
public class PermissionController extends BaseController {

    @Autowired
    SysPermissionService sysPermissionService;

    @GetMapping("/list")
    @Transactional
    public String listView(Map<String, Object> map) {
        List<SysPermission> permissions = sysPermissionService.findAll();
        map.put("permissions", permissions);

        SysPermission permission = new SysPermission();
        permission.initForm();
        map.put("permission", permission);

        return render("admin/permissionList");
    }
}
