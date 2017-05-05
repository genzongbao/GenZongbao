package ydh.user.query;

import java.util.Date;

import ydh.cicada.query.PagableQueryCmd;
import ydh.cicada.query.api.Query;
import ydh.cicada.query.api.QueryOperator;
import ydh.cicada.query.api.QueryParam;
import ydh.user.dict.UserState;

@Query( select = "U.*,R.ROLE_ID,GROUP_CONCAT(R.ROLE_NAME) ROLE_NAME,GROUP_CONCAT(R.ROLE_ID) ROLES",
		from = "USERS U LEFT JOIN USER_ROLE UR ON U.USER_ID=UR.USER_ID LEFT JOIN ROLES R ON UR.ROLE_ID=R.ROLE_ID", 
		groupBy = "U.USER_ID",
		orderBy = "U.USER_ID DESC")
public class UserQuery extends PagableQueryCmd{
	
	/** 用户ID*/
	@QueryParam(fieldName = "U.USER_ID")
	private Integer userId;

	/** 登录账号*/
	@QueryParam(fieldName = "U.LOGIN_NAME", op = QueryOperator.LIKE)
	private String loginName;

	/** 真实姓名*/
	@QueryParam(fieldName = "U.REAL_NAME", op = QueryOperator.LIKE)
	private String realName;

	/** 身份证号码*/
	@QueryParam(fieldName = "U.ID_CARD_NO", op = QueryOperator.LIKE)
	private String idCardNo;

	/** 手机号码*/
	@QueryParam(fieldName = "U.MOBILE", op = QueryOperator.LIKE)
	private String mobile;
	
	/** 注册时间（大于）*/
	@QueryParam(fieldName = "U.REGISTER_TIME", op = QueryOperator.GT_EQU)
	private Date minRegisterTime;
	
	/** 注册时间（小于）*/
	@QueryParam(fieldName = "U.REGISTER_TIME", op = QueryOperator.LESS_EQU)
	private Date maxRegisterTime;

	/** 最后登录时间（大于）*/
	@QueryParam(fieldName = "U.LAST_LOGIN_TIME", op = QueryOperator.GT_EQU)
	private Date minLastLoginTime;
	
	/** 最后登录时间（小于）*/
	@QueryParam(fieldName = "U.LAST_LOGIN_TIME", op = QueryOperator.LESS_EQU)
	private Date maxLastLoginTime;

	/** 状态:0=有效 1=冻结 2=注销*/
	@QueryParam(fieldName = "U.USER_STATE")
	private UserState userState;
	
	/** 关联的角色id */
	@QueryParam(fieldName = "R.ROLE_ID")
	private Integer roleId;
	
	/** 关联的角色名 */
	@QueryParam(fieldName = "R.ROLE_NAME", op = QueryOperator.LIKE)
	private String roleName;
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
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

	public Date getMinRegisterTime() {
		return minRegisterTime;
	}

	public void setMinRegisterTime(Date minRegisterTime) {
		this.minRegisterTime = minRegisterTime;
	}

	public Date getMaxRegisterTime() {
		return maxRegisterTime;
	}

	public void setMaxRegisterTime(Date maxRegisterTime) {
		this.maxRegisterTime = maxRegisterTime;
	}

	public Date getMinLastLoginTime() {
		return minLastLoginTime;
	}

	public void setMinLastLoginTime(Date minLastLoginTime) {
		this.minLastLoginTime = minLastLoginTime;
	}

	public Date getMaxLastLoginTime() {
		return maxLastLoginTime;
	}

	public void setMaxLastLoginTime(Date maxLastLoginTime) {
		this.maxLastLoginTime = maxLastLoginTime;
	}

	public UserState getUserState() {
		return userState;
	}

	public void setUserState(UserState userState) {
		this.userState = userState;
	}

}
