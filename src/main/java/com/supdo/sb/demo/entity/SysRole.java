package com.supdo.sb.demo.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supdo.sb.demo.entity.FormMeta.FormType;

@Entity
@Table(name="sys_role")
public class SysRole extends BaseEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8687320792443285947L;

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	
	@Size(min=5, max=30, groups= {IRole.class})
	@FormMeta(label="角色名", required=true, placeholder="角色名", groups= {IRole.class})
	@Column(nullable=false, length=30)
	private String name;
	
	@Size(min=5, max=30, groups= {IRole.class})
	@FormMeta(label="角色编码", required=true, placeholder="角色编码", groups= {IRole.class})
	@Column(nullable=false, length=30)
	private String code;
	
	@Size(min=5, max=100, groups= {IRole.class})
	@FormMeta(type=FormType.textarea, label="角色描述", required=true, placeholder="角色描述", groups= {IRole.class})
	private String description;
	
	@Column(nullable=false)
	private String status  = "1";

	@JsonIgnore
	@ManyToMany(fetch= FetchType.LAZY, mappedBy = "roleSet")
	private Set<SysUser> userSet;

	@ManyToMany(fetch= FetchType.LAZY)
    @JoinTable(name = "sys_role_permission", 
    	joinColumns = {@JoinColumn(name = "role_id", referencedColumnName ="id") }, 
    	inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName ="id") }
    )
    private Set<SysPermission> permissionSet;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SysRole sysRole = (SysRole) o;
		return Objects.equals(id, sysRole.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString(){
		return "";
	}

	public interface IRole {}
	
	public SysRole() {
		super();
	}
	
	public SysRole(String name, String code, String description) {
		super();
		this.name = name;
		this.code = code;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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
	
	public Set<SysUser> getUserSet() {
		return userSet;
	}

	public void setUserSet(Set<SysUser> userSet) {
		this.userSet = userSet;
	}

	public Set<SysPermission> getPermissionSet() {
		return permissionSet;
	}

	public void setPermissionSet(Set<SysPermission> permissionSet) {
		this.permissionSet = permissionSet;
	}
	
}
