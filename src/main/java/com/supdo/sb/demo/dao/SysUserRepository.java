package com.supdo.sb.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.supdo.sb.demo.entity.SysUser;


public interface SysUserRepository extends JpaRepository<SysUser, Long>{

	
	List<SysUser> findByUsername(String username);
	
	List<SysUser> findAll();

}
