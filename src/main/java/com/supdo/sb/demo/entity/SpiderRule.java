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

    @Size(min=1, max=200, groups={ISpiderRule.class})
    @FormMeta(label="规则编码", required=true, placeholder="规则编码", groups= {ISpiderRule.class})
    @Column(nullable=false, length=200)
    private String code;

    @Size(min=1, max=200, groups={ISpiderRule.class})
    @FormMeta(label="规则名称", required=true, placeholder="规则名称", groups= {ISpiderRule.class})
    @Column(nullable=false, length=200)
    private String name;

    @Size(max=200, groups={ISpiderRule.class})
    @FormMeta(label="分页", placeholder="分页", groups= {ISpiderRule.class})
    @Column(nullable=false, length=200)
    private String pages;

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
    private String tag = " ";

    @FormMeta(type= FormMeta.FormType.redio, label="生效", required=true, placeholder="生效", groups= {ISpiderRule.class})
    @Column(nullable=false)
    private boolean valid = true;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public interface ISpiderRule {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
