package com.supdo.sb.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supdo.sb.demo.dao.SysPermissionRepository;
import com.supdo.sb.demo.entity.SysPermission;

@Service
public class SysPermissionService {
	@Autowired
	SysPermissionRepository sysPermissionRepository;
	
	public List<SysPermission> findAll(){
		return sysPermissionRepository.findAll();
	}
	
	public List<SysPermission> findAll(List<Long> ids){
		return sysPermissionRepository.findAll(ids);
	}
	
	public SysPermission findOne(Long id) {
		return sysPermissionRepository.findOne(id);
	}
	
	public SysPermission save(SysPermission permission) {
		return sysPermissionRepository.save(permission);
	}
	
	public void delete(Long id) {
		sysPermissionRepository.delete(id);
	}
	
	public Long countRoleList(Long id) {
		return sysPermissionRepository.countRoleList(id);
	}
}
