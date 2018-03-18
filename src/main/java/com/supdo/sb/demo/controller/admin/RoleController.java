package com.supdo.sb.demo.controller.admin;

import com.supdo.sb.demo.common.StringUtility;
import com.supdo.sb.demo.entity.SysPermission;
import com.supdo.sb.demo.entity.SysRole;
import com.supdo.sb.demo.service.SysPermissionService;
import com.supdo.sb.demo.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    SysPermissionService sysPermissionService;

    @GetMapping("/list")
    @Transactional
    public String listView(Map<String, Object> map) {
        List<SysRole> roles = sysRoleService.findAll();
        map.put("roles", roles);

        List<Long> ids = new ArrayList<>();
        for(SysRole role : roles){
            ids.add(role.getId());
        }
        Map<String, List<Long>> myPermissions = sysRoleService.getPermissionsByRoles(ids);
        map.put("myPermissions", myPermissions);

        SysRole role = new SysRole();
        role.initForm(SysRole.IRole.class);
        map.put("role", role);

        List<SysPermission> permissionList = sysPermissionService.findAll();
        map.put("permissionList", permissionList);

        return render("admin/roleList");
    }

    @PostMapping("/save")
    @ResponseBody
    public Result save(@Validated(SysRole.IRole.class) SysRole roleForm, BindingResult bindingResult){
        roleForm.initForm(SysRole.IRole.class);
        if(bindingResult.hasErrors()){
            this.result.simple(false, "字段校验失败！");
            this.result.putItems("role", roleForm.initFieldErrors(bindingResult));
        }else{
            SysRole newObj;
            if(roleForm.getId() == null){
                newObj = sysRoleService.save(roleForm);
                this.result.simple(true, "保存成功！");
            }else{
                SysRole role = sysRoleService.findOne(roleForm.getId());
                role.merge(roleForm);
                newObj = sysRoleService.save(role);
                this.result.simple(true, "保存成功！");
            }
            //处理前台权限列表
            if(newObj != null) {
                newObj.initMap("permissionSet,userSet");
                List<String> myPermissions = new ArrayList();
                if(newObj.getPermissionSet() != null) {
                    for (SysPermission permission : newObj.getPermissionSet()) {
                        myPermissions.add(permission.getId().toString());
                    }
                }
                newObj.getMap().put("myPermissions", myPermissions);
                this.result.putItems("newObj", newObj.getMap());
            }
        }
        return result;
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public Result delete(@PathVariable Long id){
        if(sysRoleService.countUserList(id)>0){
            this.result.simple(false, "此角色有用户在使用，不能删除！");
        }else {
            sysRoleService.delete(id);
            this.result.simple(true, "删除成功！");
        }
        return result;
    }

    @PostMapping("/setPermission/{id}")
    @ResponseBody
    @Transactional
    public Result setPermission(@PathVariable Long id, @RequestParam Map<String, String> param){
        List<Long> ids = StringUtility.toList(param.get("permissions"));
        List<SysPermission> newPermissions = sysPermissionService.findAll(ids);
        SysRole role = sysRoleService.findOne(id);
        Set<SysPermission> nowPermissions = role.getPermissionSet();
        nowPermissions.clear();
        nowPermissions.addAll(newPermissions);
        sysRoleService.save(role);
        result.simple(true, "设置成功！");
        return result;
    }
}
