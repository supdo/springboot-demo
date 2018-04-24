package com.supdo.sb.demo.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="page_list")
public class PageList extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3023193796058955329L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable=false, length=200)
    private String title;

    @Column(nullable=false, length=200)
    private String url;

    @Column(nullable=false, length=200)
    private String pUrl;

    @Column(nullable=false)
    private String content = "尚未抓取";

    @Column(nullable=false)
    private boolean isGrab = false;

    @Column(nullable=false)
    private boolean isPost = false;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isGrab() {
        return isGrab;
    }

    public void setGrab(boolean grab) {
        isGrab = grab;
    }

    public boolean isPost() {
        return isPost;
    }

    public void setPost(boolean post) {
        isPost = post;
    }
}
