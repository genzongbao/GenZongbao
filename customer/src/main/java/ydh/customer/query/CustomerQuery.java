package ydh.customer.query;

import ydh.cicada.query.PagableQueryCmd;
import ydh.cicada.query.api.Query;
import ydh.cicada.query.api.QueryOperator;
import ydh.cicada.query.api.QueryParam;
import ydh.customer.dict.CustomerState;

import java.util.Date;

@Query(	from = "CUSTOMER",orderBy = "CUS_REG_DATETIME DESC")
public class CustomerQuery extends PagableQueryCmd{
	/** 客户ID*/
	@QueryParam(fieldName = "CUS_ID")
	private Integer cusId;

	/** 登录名*/
	@QueryParam(fieldName = "CUS_LOG_NAME", op = QueryOperator.LIKE)
	private String cusLogName;
	
	/** 昵称，用于验证手机接口昵称是否重复 */
	@QueryParam(fieldName = "CUS_LOG_NAME")
	private String nickname;
	
	/** 手机号码，用于验证是否重复 */
	@QueryParam(fieldName = "CUS_LOG_NAME")
	private String cusPhoneEQ;

	/** 真实姓名*/
	@QueryParam(fieldName = "CUS_NAME", op = QueryOperator.LIKE)
	private String cusName;

	/** 密码(MD5)*/
	@QueryParam(fieldName = "CUS_PASSWORD")
	private String cusPassWd;

	/** 邮箱*/
	@QueryParam(fieldName = "CUS_EMAIL", op = QueryOperator.LIKE)
	private String cusEmail;

	/** 地址*/
	@QueryParam(fieldName = "CUS_ADDRESS", op = QueryOperator.LIKE)
	private String cusAddress;

	/** 身份证号*/
	@QueryParam(fieldName = "CUS_IDCARD", op = QueryOperator.LIKE)
	private String cusIdCard;

	/** 电话号码*/
	@QueryParam(fieldName = "CUS_PHONE", op = QueryOperator.LIKE)
	private String cusPhone;

	/** 注册日期（大于） */
	@QueryParam(fieldName = "CUS_REG_DATETIME", op = QueryOperator.GT_EQU)
	private Date minCusRegTime;

	/** 注册日期（小于） */
	@QueryParam(fieldName = "CUS_REG_DATETIME", op = QueryOperator.LESS_EQU)
	private Date maxCusRegTime;
	
	/** 最后登录日期（大于） */
	@QueryParam(fieldName = "CUS_LAST_LOG_DATETIME", op = QueryOperator.GT_EQU)
	private Date minCusLastLogTime;
	
	/** 最后登录日期（小于） */
	@QueryParam(fieldName = "CUS_LAST_LOG_DATETIME", op = QueryOperator.LESS_EQU)
	private Date maxCusLastLogTime;

	/** 状态||0:未激活||1:正常||2:已锁定||3:注销*/
	@QueryParam(fieldName = "CUS_STATE")
	private CustomerState cusState;

	/**
	 * 无参构造
	 */
	public CustomerQuery() {
	}
	
	/**
	 * 构造:
	 * 主键ID
	 * @param cusId
	 */
	public CustomerQuery(Integer cusId) {
		this.cusId = cusId;
	}

	/**
	 * 构造:
	 * ID和手机号 
	 * @param cusId
	 * @param cusPhone
	 */
	public CustomerQuery(Integer cusId, String cusPhone) {
		this.cusId = cusId;
		this.cusPhone = cusPhone;
	}
	
	/** 
	 * 构造:
	 * 登录名、真实姓名、身份证号、手机号 
	 * @param cusLogName
	 * @param cusName
	 * @param cusIdCard
	 * @param cusPhone
	 */
	public CustomerQuery(String cusLogName, String cusName, String cusIdCard,
			String cusPhone) {
		this.cusLogName = cusLogName;
		this.cusName = cusName;
		this.cusIdCard = cusIdCard;
		this.cusPhone = cusPhone;
	}

	public String getCusPhoneEQ() {
		return cusPhoneEQ;
	}

	public void setCusPhoneEQ(String cusPhoneEQ) {
		this.cusPhoneEQ = cusPhoneEQ;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * @return the cusId
	 */
	public Integer getCusId() {
		return cusId;
	}

	/**
	 * @param cusId the cusId to set
	 */
	public void setCusId(Integer cusId) {
		this.cusId = cusId;
	}

	/**
	 * @return the cusLogName
	 */
	public String getCusLogName() {
		return cusLogName;
	}

	/**
	 * @param cusLogName the cusLogName to set
	 */
	public void setCusLogName(String cusLogName) {
		this.cusLogName = cusLogName;
	}

	/**
	 * @return the cusName
	 */
	public String getCusName() {
		return cusName;
	}

	/**
	 * @param cusName the cusName to set
	 */
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	/**
	 * @return the cusPassWd
	 */
	public String getCusPassWd() {
		return cusPassWd;
	}

	/**
	 * @param cusPassWd the cusPassWd to set
	 */
	public void setCusPassWd(String cusPassWd) {
		this.cusPassWd = cusPassWd;
	}

	/**
	 * @return the cusEmail
	 */
	public String getCusEmail() {
		return cusEmail;
	}

	/**
	 * @param cusEmail the cusEmail to set
	 */
	public void setCusEmail(String cusEmail) {
		this.cusEmail = cusEmail;
	}

	/**
	 * @return the cusAddress
	 */
	public String getCusAddress() {
		return cusAddress;
	}

	/**
	 * @param cusAddress the cusAddress to set
	 */
	public void setCusAddress(String cusAddress) {
		this.cusAddress = cusAddress;
	}

	/**
	 * @return the cusIdCard
	 */
	public String getCusIdCard() {
		return cusIdCard;
	}

	/**
	 * @param cusIdCard the cusIdCard to set
	 */
	public void setCusIdCard(String cusIdCard) {
		this.cusIdCard = cusIdCard;
	}

	/**
	 * @return the cusPhone
	 */
	public String getCusPhone() {
		return cusPhone;
	}

	/**
	 * @param cusPhone the cusPhone to set
	 */
	public void setCusPhone(String cusPhone) {
		this.cusPhone = cusPhone;
	}

	public CustomerState getCusState() {
		return cusState;
	}

	public void setCusState(CustomerState cusState) {
		this.cusState = cusState;
	}

	/**
	 * @return the minCusRegTime
	 */
	public Date getMinCusRegTime() {
		return minCusRegTime;
	}

	/**
	 * @param minCusRegTime the minCusRegTime to set
	 */
	public void setMinCusRegTime(Date minCusRegTime) {
		this.minCusRegTime = minCusRegTime;
	}

	/**
	 * @return the maxCusRegTime
	 */
	public Date getMaxCusRegTime() {
		return maxCusRegTime;
	}

	/**
	 * @param maxCusRegTime the maxCusRegTime to set
	 */
	public void setMaxCusRegTime(Date maxCusRegTime) {
		this.maxCusRegTime = maxCusRegTime;
	}

	/**
	 * @return the minCusLastLogTime
	 */
	public Date getMinCusLastLogTime() {
		return minCusLastLogTime;
	}

	/**
	 * @param minCusLastLogTime the minCusLastLogTime to set
	 */
	public void setMinCusLastLogTime(Date minCusLastLogTime) {
		this.minCusLastLogTime = minCusLastLogTime;
	}

	/**
	 * @return the maxCusLastLogTime
	 */
	public Date getMaxCusLastLogTime() {
		return maxCusLastLogTime;
	}

	/**
	 * @param maxCusLastLogTime the maxCusLastLogTime to set
	 */
	public void setMaxCusLastLogTime(Date maxCusLastLogTime) {
		this.maxCusLastLogTime = maxCusLastLogTime;
	}

}
