package com.supdo.sb.demo.dao;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class SysDaoImpl implements SysDao {

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List getRolesByUsers(List<Long> ids) {
        String sql = "select ur.* from sys_user_role ur where ur.user_id in (:ids)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("ids", ids);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List rows = query.getResultList();
        return rows;
    }

    @Override
    public List getPermissionsByRoles(List<Long> ids) {
        String sql = "select rp.* from sys_role_permission rp where rp.role_id in (:ids)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("ids", ids);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List rows = query.getResultList();
        return rows;
    }
}
