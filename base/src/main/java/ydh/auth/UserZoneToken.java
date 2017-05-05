package ydh.auth;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;

public interface UserZoneToken {
	
	public LoginZone getUserZone();

	/**
	 * 验证
	 * @param realm
	 * @return
	 * @throws AuthenticationException
	 */
	public AuthenticationInfo doGetAuthenticationInfo(UserZoneRealm realm) 
			throws AuthenticationException;
}
