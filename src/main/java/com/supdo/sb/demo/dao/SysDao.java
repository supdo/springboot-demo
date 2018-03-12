package com.supdo.sb.demo.dao;

import com.supdo.sb.demo.entity.SysRole;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@NoRepositoryBean
public interface SysDao{

    List getRolesByUsers(List<Long> ids);


    List getPermissionsByRoles(List<Long> ids);


}
