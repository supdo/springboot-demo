package com.supdo.sb.demo.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="page_content")
public class PageContent  extends BaseEntity implements Serializable {


    private static final long serialVersionUID = 7669998332146444598L;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable=false, length=200)
    private String title;

    @Column(nullable=false, length=200)
    private String url;

    @Column(nullable=false)
    private String content;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
