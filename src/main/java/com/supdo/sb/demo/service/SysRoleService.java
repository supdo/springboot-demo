package com.supdo.sb.demo.service;

import java.util.*;

import com.supdo.sb.demo.dao.SysDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supdo.sb.demo.dao.SysRoleRepository;
import com.supdo.sb.demo.entity.SysRole;

@Service
public class SysRoleService {
	
	@Autowired
	private SysRoleRepository sysRoleRepository;

	@Autowired
	private SysDao sysDao;

	//使用使用EntityManager实现了，具体在SysDaoImpl类中实现
//	@Qualifier("defaultNamedParameterJdbcTemplate")
//	@Autowired
//	private JdbcTemplate defaultJdbc;
	
	public List<SysRole> findAll(){
		return sysRoleRepository.findAll();
	}

	public Map<String, List<Long>> getPermissionsByRoles(List<Long> ids){
		Map<String, List<Long>> result = new HashMap<>();
//		List<Object[]> queryList1 = sysRoleRepository.getPermissionsByRoles(ids);
//		String sql = "select rp.* from sys_role_permission rp where rp.role_id in (:ids)";
//		MapSqlParameterSource parameters = new MapSqlParameterSource();
//		parameters.addValue("ids", ids);
//		List<Map<String,Object>> queryList = defaultJdbc.queryForList(sql, parameters);
		List<Map<String,Object>> rows = sysDao.getPermissionsByRoles(ids);
		for (Map<String, Object> map : rows) {
			String rold_id = map.get("role_id").toString();
			if(!result.containsKey(rold_id)){
				result.put(rold_id, new ArrayList<>());
			}
			result.get(rold_id).add(Long.parseLong(map.get("permission_id").toString()));
		}
		return result;
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
