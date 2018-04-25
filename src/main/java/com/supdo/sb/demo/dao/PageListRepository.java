package com.supdo.sb.demo.dao;

import com.supdo.sb.demo.entity.PageList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PageListRepository extends JpaRepository<PageList, Long> {

    List<PageList> findByUrl(String url);

    List<PageList> findBySite(Long site);

}
