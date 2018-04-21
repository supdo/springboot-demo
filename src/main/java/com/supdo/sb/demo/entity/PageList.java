package com.supdo.sb.demo.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="page_list")
public class PageList extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable=false, length=100)
    private String title;

    @Column(nullable=false, length=100)
    private String url;

    @Column(nullable=true, length=100)
    private String pUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getpUrl() {
        return pUrl;
    }

    public void setpUrl(String pUrl) {
        this.pUrl = pUrl;
    }
}
