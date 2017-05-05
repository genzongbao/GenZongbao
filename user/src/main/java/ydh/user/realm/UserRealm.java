package ydh.user.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import ydh.cicada.utils.RequestUtil;
import ydh.user.entity.User;
import ydh.user.service.UserService;

public class UserRealm extends AuthorizingRealm {
	
    private UserService authService;
    
    @Override
    public boolean supports(AuthenticationToken token) {
        return super.supports(token) && (token instanceof UserToken);
    }
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) 
            throws AuthenticationException {
    	UserToken token = (UserToken) authcToken;
    	String loginName = token.getUsername();
    	User user = authService.queryUserByLoginName(loginName);
    	if (user == null) {
    		throw new AuthenticationException("无效的用户名");
    	}
    	String passwordMd5 = RequestUtil.MD5(new String(token.getPassword()));
    	if ( ! passwordMd5.equals(user.getPassword())) {
    		throw new AuthenticationException("密码错误");
    	}
        return new SimpleAuthenticationInfo(user.getUserId(), token.getPassword(), getName());  
    }

	public UserService getAuthService() {
		return authService;
	}

	public void setAuthService(UserService authService) {
		this.authService = authService;
	}
	
}