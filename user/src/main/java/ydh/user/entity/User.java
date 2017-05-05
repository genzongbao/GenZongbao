package ydh.user.entity;

import java.util.Date;
import java.util.List;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;
import ydh.user.dict.UserState;

@Entity(name = "USERS")
public class User {
	/** 用户ID*/
	@Id(autoincrement = true)
	@Column(name = "USER_ID")
	private Integer userId;

	/** 登录账号*/
	@Column(name = "LOGIN_NAME")
	private String loginName;

	/** 密码*/
	@Column(name = "PASSWORD")
	private String password;

	/** 真实姓名*/
	@Column(name = "REAL_NAME")
	private String realName;

	/** 身份证号码*/
	@Column(name = "ID_CARD_NO")
	private String idCardNo;

	/** 手机号码*/
	@Column(name = "MOBILE")
	private String mobile;

	/** 注册时间*/
	@Column(name = "REGISTER_TIME")
	private Date registerTime;

	/** 最后登录时间*/
	@Column(name = "LAST_LOGIN_TIME")
	private Date lastLoginTime;

	/** 状态:0=有效 1=冻结 2=注销*/
	@Column(name = "USER_STATE")
	private UserState userState;
	
	/** 关联的角色名，多个由逗号分隔 */
	private String roleName;
	
	private String roles;
	
	private List<Role> roleList;
	

	public String getRoleName() {
		return roleName;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public UserState getUserState() {
		return userState;
	}

	public void setUserState(UserState userState) {
		this.userState = userState;
	}

}
