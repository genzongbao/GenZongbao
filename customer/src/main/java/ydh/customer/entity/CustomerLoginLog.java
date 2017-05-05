package ydh.customer.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;

import java.io.Serializable;
import java.util.Date;

@Entity(name = "CUSTOMER_LOGIN_LOG")
public class CustomerLoginLog  implements Serializable {
	private static final long serialVersionUID = -8061552728800871094L;
	
	/** 日志编号*/
	public static final String LOG_ID = "LOG_ID";
	@Id
	@Column(name = "LOG_ID")
	private String logId;

	/** 用户ID*/
	public static final String CUS_ID = "CUS_ID";
	@Column(name = "CUS_ID")
	private Integer cusId;

	/** 用户名（冗余）*/
	public static final String CUS_LOGIN_NAME = "CUS_LOGIN_NAME";
	@Column(name = "CUS_LOGIN_NAME")
	private String cusLoginName;

	/** 登录时间*/
	public static final String LOGIN_DATET_IME = "LOGIN_DATET_IME";
	@Column(name = "LOGIN_DATET_IME")
	private Date loginDatetIme;

	/** 最近一次登录时间*/
	public static final String LAST_LOGIN_TIME = "LAST_LOGIN_TIME";
	@Column(name = "LAST_LOGIN_TIME")
	private Date lastLoginTime;

	/** 登录终端*/
	public static final String LOGIN_TERMINAL = "LOGIN_TERMINAL";
	@Column(name = "LOGIN_TERMINAL")
	private String loginTerminal;

	/** IP地址*/
	public static final String IP_ADDRESS = "IP_ADDRESS";
	@Column(name = "IP_ADDRESS")
	private String ipAddress;
	
	
	
	

	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}
	public Integer getCusId() {
		return cusId;
	}
	public void setCusId(Integer cusId) {
		this.cusId = cusId;
	}
	public String getCusLoginName() {
		return cusLoginName;
	}
	public void setCusLoginName(String cusLoginName) {
		this.cusLoginName = cusLoginName;
	}
	public Date getLoginDatetIme() {
		return loginDatetIme;
	}
	public void setLoginDatetIme(Date loginDatetIme) {
		this.loginDatetIme = loginDatetIme;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getLoginTerminal() {
		return loginTerminal;
	}
	public void setLoginTerminal(String loginTerminal) {
		this.loginTerminal = loginTerminal;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}
