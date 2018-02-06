package com.supdo.sb.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supdo.sb.demo.dao.SysRoleRepository;
import com.supdo.sb.demo.entity.SysRole;

@Service
public class SysRoleService {
	
	@Autowired
	private SysRoleRepository sysRoleRepository;
	
	public List<SysRole> findAll(){
		return sysRoleRepository.findAll();
	}
	
	public SysRole findOne(Long id){
		return sysRoleRepository.findOne(id);
	}
	
	public List<SysRole> findAll(List<Long> ids){
		return sysRoleRepository.findAll(ids);
	}

	public SysRole save(SysRole role) {
		return sysRoleRepository.save(role);
	}
	
	public void delete(Long id) {
		sysRoleRepository.delete(id);
	}
	
	public Long countUserList(Long id) {
		return sysRoleRepository.countUserList(id);
	}
}
