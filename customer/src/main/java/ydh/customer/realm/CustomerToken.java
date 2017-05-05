package ydh.customer.realm;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import ydh.auth.LoginZone;
import ydh.auth.UserZoneRealm;
import ydh.auth.UserZoneToken;
import ydh.customer.dict.CustomerState;
import ydh.customer.entity.Customer;
import ydh.customer.form.LoginForm;
import ydh.customer.service.CustomerService;
import ydh.mvc.ErrorCode;
import ydh.utils.EncryptUtil;
import ydh.utils.JPushTool;
import ydh.utils.UUIDTool;

public class CustomerToken extends UsernamePasswordToken implements UserZoneToken {
	private static final long serialVersionUID = -2722101402337235432L;

	public static final String SESSION_KEY = "loginCustomer";

	private String validcode;
	private CustomerService customerService;

	public CustomerToken(final String cusPhone, final String cusPassword, String validcode,
						 CustomerService customerService, final HttpServletRequest request) {
		super(cusPhone, cusPassword, false, request.getRemoteHost());
		this.validcode = validcode;
		this.customerService = customerService;
	}
	/**
	 *
	 * @param loginForm
	 * @param customerService
	 * @param request
	 */
	public CustomerToken(final LoginForm loginForm, CustomerService customerService, final HttpServletRequest request) {
		/**
		 * remeberMe：默认记住我
		 */


		super(loginForm.getCusPhone(), loginForm.getCusPassword(), true, request.getRemoteHost());
		this.validcode = loginForm.getValidcode();
		this.customerService = customerService;
	}

	public String getValidcode() {
		return validcode;
	}

	public void setValidcode(String validcode) {
		this.validcode = validcode;
	}

	public static Customer loginCustomer() {
		Subject subject = SecurityUtils.getSubject();
		return (Customer)subject.getSession().getAttribute(SESSION_KEY);
	}

	public static void updateLoginCustomer(Customer customer) {
		Subject subject = SecurityUtils.getSubject();
		subject.getSession().setAttribute(SESSION_KEY, customer);
	}

	@Override
	public AuthenticationInfo doGetAuthenticationInfo(UserZoneRealm realm)
			throws AuthenticationException {
		String cusPhone = this.getUsername();
		Subject subject = SecurityUtils.getSubject();
		Customer customer = customerService.loginCheckByNameOrPhone(cusPhone);
		if (customer == null) {
			throw new AuthenticationException("该账号或手机号用户不存在");
		}
		String passwordMd5 = EncryptUtil.md5(new String(this.getPassword()));
		if ( ! passwordMd5.equals(customer.getCusPassword())) {
			throw new AuthenticationException("密码错误");
		}
		if (customer.getCusState() == CustomerState.LOCKED) {
			throw new AuthenticationException("您的账号已被冻结，如需解冻，请与系统管理员联系");
		} else if (customer.getCusState() == CustomerState.REMOVED) {
			throw new AuthenticationException("您的账号已被注销");
		}
		customer.setCusLastLogDatetime(new Date());
		customerService.updateLastLogTime(customer.getCusId());
		Session oldSession=getCurrSession(cusPhone);
		if(oldSession!=null && !oldSession.getId().equals(subject.getSession().getId())
				&& StringUtils.isNotBlank(customer.getPushAlias())){
			Map<String,String> baseResult=new HashMap<>();
			baseResult.put("msg", "您的账号在其它地方登录，您被迫下线！");
			baseResult.put("errorCode", ErrorCode.NO_LOGIN.toString());
			baseResult.put("error", "true");
			List<String> alias=new ArrayList<String>();
			alias.add(customer.getPushAlias());
			try {
				JPushTool.pushAliasMessageGzb("警告", "账号强制下线", baseResult, "", alias);
			} catch (Exception e) {
				throw new AuthenticationException("踢出用户失败！");
			}
		}
		String alias= UUIDTool.getUUID();
		customer.setPushAlias(alias);
		customerService.updateAlias(customer.getCusId(), alias);
		subject.getSession().setAttribute(SESSION_KEY, customer);
		return new SimpleAuthenticationInfo(customer.getCusPhone(), this.getPassword(), realm.getName());
	}

	@Override
	public LoginZone getUserZone() {
		// TODO Auto-generated method stub
		return null;
	}

	private Session getCurrSession(String userName){
		DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
		DefaultWebSessionManager sessionManager = (DefaultWebSessionManager)securityManager.getSessionManager();
		Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();//获取当前已登录的用户session列表
		for(Session session:sessions){
			//清除该用户以前登录时保存的session
			if(session!=null && userName.equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
				if(session.getAttribute(SESSION_KEY)!=null){
					session.removeAttribute(SESSION_KEY);
					session.setTimeout(0);
				}
				return session;
			}
		}
		return null;
	}

}
