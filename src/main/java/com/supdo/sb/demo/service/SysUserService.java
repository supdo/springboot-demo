package com.supdo.sb.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.supdo.sb.demo.dao.SysUserRepository;
import com.supdo.sb.demo.entity.SysUser;

@Service
@CacheConfig(cacheNames="sysuser")
public class SysUserService {
	
	@Autowired
	private SysUserRepository sysUserRepository;

	//@Cacheable
	public List<SysUser> getListByUsername(String username){
		return sysUserRepository.findByUsername(username);
	}
	
	public SysUser findOne(Long id) {
		return sysUserRepository.findOne(id);
	}
	
	public List<SysUser> findAll(){
		return sysUserRepository.findAll();
	}
	
	public SysUser save(SysUser user) {
		return sysUserRepository.save(user);
	}
	
	public void delete(Long id) {
		sysUserRepository.delete(id);
	}
}
