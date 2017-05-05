package ydh.user.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import ydh.auth.LoginZone;
import ydh.auth.UserZoneRealm;
import ydh.auth.UserZoneToken;
import ydh.cicada.utils.RequestUtil;
import ydh.user.dict.UserState;
import ydh.user.entity.User;
import ydh.user.service.UserService;
import ydh.utils.ConfigTool;
import ydh.utils.EnvConfig;

public class UserToken extends UsernamePasswordToken implements UserZoneToken{
	private static final long serialVersionUID = -2722101402337235432L;

	
	private String validcode;
	private UserLoginZone userLoginZone;
	

	public UserToken(final String username, final String password,String validcode, UserLoginZone userLoginZone) {
		super(username, password);
		this.setRememberMe(false);
		this.userLoginZone = userLoginZone;
		this.validcode = validcode;
	}
	
	@Override
	public AuthenticationInfo doGetAuthenticationInfo(UserZoneRealm realm)
			throws AuthenticationException {
		String loginName = this.getUsername();
		UserService userService = userLoginZone.getUserService();
		User user = userService.queryUserByLoginName(loginName);
		if (user == null) {
			throw new AuthenticationException("无效的用户名");
		}
		if (user.getUserState() != UserState.VALID) {
			throw new AuthenticationException(user.getUserState().message());
		}
		String passwordMd5 = RequestUtil.MD5(new String(this.getPassword()));
		if ( ! passwordMd5.equals(user.getPassword())) {
			throw new AuthenticationException("密码错误");
		}
		if ( ! ConfigTool.getBoolean(EnvConfig.test, false)) {
			if(!userService.checkValidcodeByPhone(user.getMobile(),validcode)){
				throw new AuthenticationException("验证码输入错误");
			}
		}
		Subject subject = SecurityUtils.getSubject();
		userService.updateLastLogTime(user.getUserId());
		subject.getSession().setAttribute(UserLoginZone.SESSION_KEY, user);
		return new SimpleAuthenticationInfo(user.getUserId(), this.getPassword(), realm.getName()); 
	}

	public String getValidcode() {
		return validcode;
	}

	public void setValidcode(String validcode) {
		this.validcode = validcode;
	}

	@Override
	public LoginZone getUserZone() {
		return this.userLoginZone;
	}
}
