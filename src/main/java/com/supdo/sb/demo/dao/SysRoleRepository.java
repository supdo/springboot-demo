package com.supdo.sb.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.supdo.sb.demo.entity.SysRole;

public interface SysRoleRepository extends JpaRepository<SysRole, Long> {
	
	@Query(value="select count(1) from sys_user_role ur where ur.role_id = ?1", nativeQuery = true)
	Long countUserList(Long id);
}
