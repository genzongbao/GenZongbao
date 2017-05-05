package ydh.auth;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import ydh.utils.ServiceProxy;

public class UserZoneRealm extends AuthorizingRealm {
	public static final String AUTHORIZATION_INFO = "authorizationInfo";
	
	LoginZone userZoneProxy = ServiceProxy.proxy(LoginZone.class);
	
    @Override
    public boolean supports(AuthenticationToken token) {
        return super.supports(token) && (token instanceof UserZoneToken);
    }
    
    @Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    	Subject subject = SecurityUtils.getSubject();
    	SimpleAuthorizationInfo info = (SimpleAuthorizationInfo)subject.getSession().getAttribute(AUTHORIZATION_INFO);
    	if (info == null) {
	    	info = new SimpleAuthorizationInfo();
	    	userZoneProxy.doGetAuthorizationInfo(principals, info);
    	}
		return info;
	}

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) 
            throws AuthenticationException {
    	UserZoneToken token = (UserZoneToken) authcToken;
    	return token.doGetAuthenticationInfo(this);
    }

}
