package com.supdo.sb.demo.controller.admin;

import com.supdo.sb.demo.entity.SysPermission;
import com.supdo.sb.demo.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/save")
    @ResponseBody
    public Result save(@Validated(SysPermission.IPermission.class) SysPermission permissionForm, BindingResult bindingResult){
        permissionForm.initForm(SysPermission.IPermission.class);
        if(bindingResult.hasErrors()){
            this.result.simple(false, "字段校验失败！");
            this.result.putItems("role", permissionForm.initFieldErrors(bindingResult));
        }else{
            if(permissionForm.getId() == null){
                SysPermission newObj = sysPermissionService.save(permissionForm);
                this.result.simple(true, "保存成功！");
                this.result.putItems("newObj", newObj);
            }else{
                SysPermission role = sysPermissionService.findOne(permissionForm.getId());
                role.merge(permissionForm);
                SysPermission newObj = sysPermissionService.save(role);
                this.result.simple(true, "保存成功！");
                this.result.putItems("newObj", newObj);
            }
            this.result.putItems("permission", permissionForm);
        }
        return result;
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public Result delete(@PathVariable Long id){
        if(sysPermissionService.countRoleList(id)>0){
            this.result.simple(false, "此权限有角色在使用，不能删除！");
        }else {
            sysPermissionService.delete(id);
            this.result.simple(true, "删除成功！");
        }
        return result;
    }
}
