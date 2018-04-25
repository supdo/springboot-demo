package com.supdo.sb.demo.dao;

import com.supdo.sb.demo.entity.SpiderRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpiderRuleRepository extends JpaRepository<SpiderRule, Long> {

    List<SpiderRule> findAllByValid(boolean valid);

}
