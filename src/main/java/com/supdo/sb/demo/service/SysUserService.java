package com.supdo.sb.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supdo.sb.demo.dao.SysDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.supdo.sb.demo.dao.SysUserRepository;
import com.supdo.sb.demo.entity.SysUser;

@Service
@CacheConfig(cacheNames = "sysuser")
public class SysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private SysDao sysDao;

    //@Cacheable
    public List<SysUser> getListByUsername(String username) {
        return sysUserRepository.findByUsername(username);
    }

    public SysUser findOne(Long id) {
        return sysUserRepository.findOne(id);
    }

    public List<SysUser> findAll() {
        return sysUserRepository.findAll();
    }

    public SysUser save(SysUser user) {
        return sysUserRepository.save(user);
    }

    public void delete(Long id) {
        sysUserRepository.delete(id);
    }

    public Map<String, List<Long>> getRolesByUsers(List<Long> ids){
        Map<String, List<Long>> result = new HashMap<>();
        List<Map<String,Object>> rows = sysDao.getRolesByUsers(ids);
        for (Map<String, Object> map : rows) {
            String rold_id = map.get("user_id").toString();
            if(!result.containsKey(rold_id)){
                result.put(rold_id, new ArrayList<>());
            }
            result.get(rold_id).add(Long.parseLong(map.get("role_id").toString()));
        }
        return result;
    }
}
