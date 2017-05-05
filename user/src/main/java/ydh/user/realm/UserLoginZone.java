package ydh.user.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ydh.auth.LoginZone;
import ydh.user.entity.User;
import ydh.user.service.UserService;

@Component
public class UserLoginZone implements LoginZone {
	public static final String SESSION_KEY = "loginUser";
	
	@Autowired
	private UserService userService;
	
	@Override
	public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals, SimpleAuthorizationInfo info) {
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getSession().getAttribute(SESSION_KEY);
		if (user != null) {
			info.addRoles(userService.queryActiveRoles(user.getUserId()));
//			info.addStringPermissions(userService.queryActivePermissions(user.getUserId()));
		}
		return info;
	}
	
	public UserService getUserService() {
		return this.userService;
	}
	
	public static User loginUser() {
		Subject subject = SecurityUtils.getSubject();
		return (User)subject.getSession().getAttribute(UserLoginZone.SESSION_KEY);
	}

	public static void updateLoginUser(User user) {
		Subject subject = SecurityUtils.getSubject();
		subject.getSession().setAttribute(SESSION_KEY, user);
	}
}
