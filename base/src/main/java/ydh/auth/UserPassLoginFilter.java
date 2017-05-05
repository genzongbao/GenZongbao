package ydh.auth;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.UserFilter;

public class UserPassLoginFilter extends UserFilter {

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		saveRequestAndRedirectToLogin(request, response);
		return false;
	}
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		Subject subject = getSubject(request, response);
		if (isLoginRequest(request, response)) {
			return true;
        } else {
        	if(subject.getPrincipal()!=null && subject.getSession().getTimeout()>0){
        		System.err.println(subject.getSession().getId()+"||"+subject.getSession().getTimeout());
        		return true;
        	}else{
        		return false;
        	}
        }
	}
}
