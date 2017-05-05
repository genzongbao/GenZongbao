package ydh.customer.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;
import ydh.customer.dict.AccountType;
import ydh.customer.dict.CustomerState;

import java.io.Serializable;
import java.util.Date;

@Entity(name = "CUSTOMER")
public class Customer implements Serializable {
	private static final long serialVersionUID = -5574353777547962191L;

	/** 客户ID*/
	@Id
	@Column(name = "CUS_ID")
	private Integer cusId;

	/** 登录名*/
	@Column(name = "CUS_LOG_NAME")
	private String cusLogName;

	/** 密码(MD5)*/
	@Column(name = "CUS_PASSWORD")
	private String cusPassword;

	/** 电话号码*/
	@Column(name = "CUS_PHONE")
	private String cusPhone;

	/** 注册日期*/
	@Column(name = "CUS_REG_DATETIME")
	private Date cusRegDatetime;

	/** 最近登录时间*/
	@Column(name = "CUS_LAST_LOG_DATETIME")
	private Date cusLastLogDatetime;

	/** 状态||0:正常||1:临时||2:冻结||3:注销*/
	@Column(name = "CUS_STATE")
	private CustomerState cusState;
	
	/** 账户类型 	 ||0：个人||1：企业*/
	@Column(name = "ACCOUNT_TYPE")
	private AccountType accountType;
	
	@Column(name = "CUS_HEAD_IMG")
	private String cusHeadImg;
	//电子邮件地址
	@Column(name= "EMAIL_ADDRESS")
	private String emailAddress;
	//微信id
	@Column(name="WX_OPEN_ID")
	private String wxOpenId;
	
	@Column(name="PUSH_ALIAS")
	private String pushAlias;
	
	private Boolean friend;
	
	private Boolean checked;
	public Customer() {
	}
	
	



	public String getPushAlias() {
		return pushAlias;
	}

	public void setPushAlias(String pushAlias) {
		this.pushAlias = pushAlias;
	}





	public String getEmailAddress() {
		return emailAddress;
	}





	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}





	public String getWxOpenId() {
		return wxOpenId;
	}





	public void setWxOpenId(String wxOpenId) {
		this.wxOpenId = wxOpenId;
	}





	public Boolean getFriend() {
		return friend;
	}





	public void setFriend(Boolean friend) {
		this.friend = friend;
	}





	public Integer getCusId() {
		return cusId;
	}
	public void setCusId(Integer cusId) {
		this.cusId = cusId;
	}
	public String getCusLogName() {
		return cusLogName;
	}
	public void setCusLogName(String cusLogName) {
		this.cusLogName = cusLogName;
	}
	public String getCusPassword() {
		return cusPassword;
	}
	public void setCusPassword(String cusPassword) {
		this.cusPassword = cusPassword;
	}
	public String getCusPhone() {
		return cusPhone;
	}
	public void setCusPhone(String cusPhone) {
		this.cusPhone = cusPhone;
	}
	public Date getCusRegDatetime() {
		return cusRegDatetime;
	}
	public void setCusRegDatetime(Date cusRegDatetime) {
		this.cusRegDatetime = cusRegDatetime;
	}
	public Date getCusLastLogDatetime() {
		return cusLastLogDatetime;
	}
	public void setCusLastLogDatetime(Date cusLastLogDatetime) {
		this.cusLastLogDatetime = cusLastLogDatetime;
	}
	public CustomerState getCusState() {
		return cusState;
	}
	public void setCusState(CustomerState state) {
		this.cusState = state;
	}
	public String getCusHeadImg() {
		return cusHeadImg;
	}
	public void setCusHeadImg(String cusHeadImg) {
		this.cusHeadImg = cusHeadImg;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public AccountType getAccountType() {
		return accountType;
	}
	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}
	
}
