package com.supdo.sb.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.supdo.sb.demo.entity.SysRole;

import java.util.List;

public interface SysRoleRepository extends JpaRepository<SysRole, Long> {
	
	@Query(value="select count(1) from sys_user_role ur where ur.role_id = ?1", nativeQuery = true)
	Long countUserList(Long id);

	//在这儿实现查询不返回字段名，已经移到自定义的SysDao中了
//	@Query(value="select rp.* from sys_role_permission rp where rp.role_id in (?1)", nativeQuery = true)
//    List<Object[]> getPermissionsByRoles(List<Long> ids);
}
