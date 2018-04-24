package com.supdo.sb.demo.dao;

import com.supdo.sb.demo.entity.PageContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PageContentRepository extends JpaRepository<PageContent, Long> {

    List<PageContent> findByUrl(String url);
}
