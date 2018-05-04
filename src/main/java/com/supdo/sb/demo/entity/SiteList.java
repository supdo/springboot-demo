package com.supdo.sb.demo.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name="site_list")
public class SiteList extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 6730548005645993771L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Size(min=1, max=200, groups={ISiteList.class})
    @FormMeta(label="网站编码", required=true, placeholder="网站编码", groups= {ISiteList.class})
    @Column(nullable=false, length=200)
    private String code;

    @Size(min=1, max=200, groups={ISiteList.class})
    @FormMeta(label="网站名称", required=true, placeholder="网站名称", groups= {ISiteList.class})
    @Column(nullable=false, length=200)
    private String name;

    @Size(min=1, max=200, groups={ISiteList.class})
    @FormMeta(label="URL", required=true, placeholder="URL", groups= {ISiteList.class})
    @Column(nullable=false, length=200)
    private String url;

    @FormMeta(label="抓取规则", required=true, type=FormMeta.FormType.select, placeholder="抓取规则", groups= {ISiteList.class})
    @Column(nullable=false, length=200)
    private Long rule;

    @FormMeta(label="标签", required=true, type = FormMeta.FormType.select, placeholder="标签", options={"53:java","57:python", "59:android",  "60:javascript", "72:oracle"}, groups= {ISiteList.class})
    @Column(nullable=true, length=200)
    private String tag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getRule() {
        return rule;
    }

    public void setRule(Long rule) {
        this.rule = rule;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public interface ISiteList {}
}
