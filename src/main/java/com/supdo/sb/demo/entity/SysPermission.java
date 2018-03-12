package com.supdo.sb.demo.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.supdo.sb.demo.entity.FormMeta.FormType;

@Entity
@Table(name="sys_permission")
public class SysPermission extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -5248610757800891674L;

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	
	@Size(min=5, max=30)
	@FormMeta(label="权限编码", required=true, placeholder="权限编码", groups= {IPermission.class})
	@Column(nullable=false, length=30)
	private String code;
	
	@Size(min=5, max=30)
	@FormMeta(label="权限名", required=true, placeholder="权限名", groups= {IPermission.class})
	@Column(nullable=false, length=50)
	private String name;
	
	@Size(min=1, max=1)
	@FormMeta(type=FormType.select, label="权限类型", required=true, placeholder="权限类型", options= {"1:功能"}, groups= {IPermission.class})
	@Column(nullable=false, length=30)
	private String type;
	
	@Size(min=5, max=100)
	@FormMeta(type=FormType.textarea, label="权限描述", required=true, placeholder="权限描述", groups= {IPermission.class})
	@Column(nullable=true, length=300)
	private String description;
	
	@Column(nullable=false)
	private String status = "1";
	
	@ManyToMany(fetch= FetchType.LAZY, mappedBy = "permissionSet")
	private Set<SysRole> roleSet;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SysPermission that = (SysPermission) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString(){
		return "";
	}

	public interface IPermission {}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<SysRole> getRoleSet() {
		return roleSet;
	}

	public void setRoleSet(Set<SysRole> roleSet) {
		this.roleSet = roleSet;
	}
	
}
