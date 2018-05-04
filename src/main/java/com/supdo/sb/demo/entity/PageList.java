package com.supdo.sb.demo.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;

@Entity
@Table(name="page_list")
public class PageList extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3023193796058955329L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable=false, length=20)
    private Long site;

    @Column(nullable=false, length=200)
    private String title;

    @Column(length=100)
    private String author;

    @Column(nullable=false, length=200)
    private String url;

    @Column(nullable=false)
    private String content = "尚未抓取";

    @Column(nullable=false, length=100)
    private String tags = " ";

    @Column()
    private Time date;

    @Column(nullable=false)
    private boolean isGrab = false;

    @Column(nullable=false)
    private boolean isPost = false;

    @Column(nullable=false)
    private String postId = "";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSite() {
        return site;
    }

    public void setSite(Long site) {
        this.site = site;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Time getDate() {
        return date;
    }

    public void setDate(Time date) {
        this.date = date;
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


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
