package com.vein.realm;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vein.bean.Manager;
import com.vein.service.LoginService;

@Repository
public class VeinRealm extends AuthorizingRealm {

	@Autowired
	LoginService loginService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// 授权方法，不需要验证密码

		Map<String, String> parameter = new HashMap<String, String>();

		// 获取当前登录的用户名
		String userName = (String) super.getAvailablePrincipal(principals);
		Manager manager = null;
		// 根据用户名从数据库中获取用户信息
		try {
			manager = (Manager) loginService.getManagerById(userName);
			if (manager != null) {
				// 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
				SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
				return info;
			} else {
				throw new AuthorizationException();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		// 登入认证，需要验证密码
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
		String userName = token.getUsername();
		Manager manager = null;
		try {
			manager = (Manager) loginService.getManagerById(userName);
			if (manager == null) {
				throw new UnknownAccountException("用户名不存在!");
			}
			String password = new String((char[]) token.getCredentials());
			if (manager.getPassword().equals(password)) {
				AuthenticationInfo auth = new SimpleAuthenticationInfo(userName, password, "VeinAuthorizingRealm");
				return auth;
			} else {
				throw new IncorrectCredentialsException("密码错误!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public void removeUserAuthorizationInfoCache(String username) {
		SimplePrincipalCollection pc = new SimplePrincipalCollection();
		pc.add(username, super.getName());
		super.clearCachedAuthorizationInfo(pc);
	}
	
}
