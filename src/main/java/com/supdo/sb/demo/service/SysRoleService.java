package com.supdo.sb.demo.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.supdo.sb.demo.dao.SysRoleRepository;
import com.supdo.sb.demo.entity.SysRole;

@Service
public class SysRoleService {
	
	@Autowired
	private SysRoleRepository sysRoleRepository;

	@Autowired
	@Qualifier("defaultNamedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate defaultJdbc;
	
	public List<SysRole> findAll(){
		return sysRoleRepository.findAll();
	}

	public Map<Long, List<Long>> getPermissionsByRoles(List<Long> ids){
		Map<Long, List<Long>> result = new HashMap<>();
		String sql = "select rp.* from sys_role_permission rp where rp.role_id in (:ids)";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ids", ids);
		List<Map<String,Object>> queryList = defaultJdbc.queryForList(sql, parameters);
		for (Map<String, Object> map : queryList) {
			Long rold_id = Long.parseLong(map.get("role_id").toString());
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
