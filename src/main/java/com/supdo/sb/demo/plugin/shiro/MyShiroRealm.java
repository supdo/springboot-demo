package com.supdo.sb.demo.plugin.shiro;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.supdo.sb.demo.entity.SysUser;
import com.supdo.sb.demo.service.SysUserService;

public class MyShiroRealm extends AuthorizingRealm {
	
	@Autowired
	private SysUserService sysUserService;
	/**
	 * 鉴权
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String username = token.getUsername();
		//String password = String.valueOf(token.getPassword());
		SysUser user;
		//String encryptPassword;
		List<SysUser> users = sysUserService.getListByUsername(username);
		if(users.size() == 0) {
			throw new AccountException("无此用户名！");
		}else {
			user = users.get(0);
//			encryptPassword = new SimpleHash("SHA-1", password, username).toString();
//			if(!user.getPassword().equals(encryptPassword)) {
//				throw new AccountException("密码有误！");
//			}
		}
		setName("test_321");
		//由shrio根据AuthenticationToken中的信息与SysUser的信息进行密码加密后的比对
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
				//user,	//此处放user实例在controller中无法获得，因此放username
				username,
				user.getPassword(),
				ByteSource.Util.bytes(user.getUsername()),
				getName()
		);
		return authenticationInfo;
	}
	
	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		SysUser user = (SysUser)SecurityUtils.getSubject().getPrincipal();
		Long userId = user.getId();
		SimpleAuthorizationInfo info =  new SimpleAuthorizationInfo();
		
		Set<String> roleSet = new HashSet<String>();
		roleSet.add("100002");
		info.setRoles(roleSet);
		
		Set<String> permissionSet = new HashSet<String>();
		permissionSet.add("权限添加");
		permissionSet.add("add");
		
		info.setStringPermissions(permissionSet);
		
		return info;
	}

}
