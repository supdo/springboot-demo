package com.supdo.sb.demo.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import com.supdo.sb.demo.entity.FormMeta.FormType;

@Entity
@Table(name="sys_user")
public class SysUser extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6336110168482446523L;

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	
	@Size(min=5, max=30, groups={IUser.class, IUserLogin.class})
	@FormMeta(label="用户名", required=true, placeholder="用户名", groups= {IUser.class, IUserLogin.class})
	@Column(unique=true, nullable=false, length=30)
	private String username;
	
	@Size(min=5, max=30, groups={IUser.class, IUserLogin.class})
	@FormMeta(type=FormType.password, label="密码", required=true, placeholder="密码", groups= {IUser.class, IUserLogin.class})
	@Column(nullable=false)
	private String password;
	
    @Size(min=5, max=30, groups={IUser.class})
    @Transient
    @FormMeta(type=FormType.password, label="确认密码", required=true, placeholder="确认密码", groups= {IUser.class})
    private String rePassword;
	
	@Size(min=5, max=30, groups={IUser.class, IUserLogin.class})
	@FormMeta(label="昵称", required=true, placeholder="昵称", groups= {IUser.class})
	@Column(length=30)
	private String nickname;
	
	@Column(nullable=false)
	private String status = "1";

	@Column(name="last_login_time")
	private Timestamp lastLoginTime;
	
	@Column(name="create_user")
	private String createUser;
	
	@Column(name="create_time")
	private Timestamp createTime;
	
	@Column(name="last_update_user")
	private String lastUpdateUser;
	
	@Column(name="last_update_time")
	private Timestamp lastUpdateTime;
	
	@ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name = "sys_user_role", 
    	joinColumns = {@JoinColumn(name = "user_id", referencedColumnName ="id") }, 
    	inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName ="id") }
    )
    private Set<SysRole> roleSet;

	public interface IUser {}
    public interface IUserLogin {}
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Set<SysRole> getRoleSet() {
		return roleSet;
	}

	public void setRoleSet(Set<SysRole> roleSet) {
		this.roleSet = roleSet;
	}
}
