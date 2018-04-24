package com.supdo.sb.demo.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name="spider_rule")
public class SpiderRule  extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -3862217391060387164L;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Size(min=5, max=200, groups={ISpiderRule.class})
    @FormMeta(label="地址", required=true, placeholder="地址", groups= {ISpiderRule.class})
    @Column(nullable=false, length=200)
    private String pUrl;

    @Size(min=1, max=200, groups={ISpiderRule.class})
    @FormMeta(label="列表", required=true, placeholder="列表", groups= {ISpiderRule.class})
    @Column(nullable=false, length=200)
    private String list;

    @Size(min=1, max=200, groups={ISpiderRule.class})
    @FormMeta(label="标题", required=true, placeholder="标题", groups= {ISpiderRule.class})
    @Column(nullable=false, length=200)
    private String title;

    @Size(min=1, max=200, groups={ISpiderRule.class})
    @FormMeta(label="内容地址", required=true, placeholder="内容地址", groups= {ISpiderRule.class})
    @Column(nullable=false, length=200)
    private String url;

    @Size(min=1, groups={ISpiderRule.class})
    @FormMeta(label="内容", required=true, placeholder="内容", groups= {ISpiderRule.class})
    @Column(nullable=false)
    private String content;

    @Size(min=1, max=200, groups={ISpiderRule.class})
    @FormMeta(label="标签", required=true, placeholder="标签", groups= {ISpiderRule.class})
    @Column(nullable=false, length=200)
    private String Tag = " ";

    public interface ISpiderRule {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getpUrl() {
        return pUrl;
    }

    public void setpUrl(String pUrl) {
        this.pUrl = pUrl;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
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

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }
}
