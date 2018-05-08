package com.supdo.sb.demo.dao;

import com.supdo.sb.demo.entity.SiteList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SiteListRepository extends JpaRepository<SiteList, Long> {

    List<SiteList> findByValid(boolean valid);
}
