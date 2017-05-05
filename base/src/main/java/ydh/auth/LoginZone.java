package ydh.auth;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;

public interface LoginZone {

	/**
	 * 授权
	 * @return
	 */
	public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals, SimpleAuthorizationInfo info);
	
	
}
