package com.supdo.sb.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.supdo.sb.demo.entity.SysPermission;

public interface SysPermissionRepository extends JpaRepository<SysPermission, Long>{

	@Query(value="select count(1) from sys_role_permission ur where ur.permission_id = ?1", nativeQuery = true)
	Long countRoleList(Long id);
	
}
