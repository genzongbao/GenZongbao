package ydh.customer.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;

import java.math.BigDecimal;

@Entity(name = "ACCOUNT")
public class Account {
	
	/** 账号ID*/
	public static final String ACCOUNT_ID = "ACCOUNT_ID";

	@Id(autoincrement=true)
	@Column(name = "ACCOUNT_ID")
	private Integer accountId;

	/** 客户ID*/
	public static final String CUS_ID = "CUS_ID";
	@Column(name = "CUS_ID")
	private Integer cusId;
	
	public static final String CUS_LOG_NAME = "CUS_LOG_NAME";
	@Column(name = "CUS_LOG_NAME")
	private String cusLogName;

	/** 可用余额*/
	public static final String BALANCE_MONEY = "BALANCE_MONEY";
	@Column(name = "BALANCE_MONEY")
	private BigDecimal balanceMoney;

	/** 冻结金额*/
	public static final String FREEZE_MONEY = "FREEZE_MONEY";
	@Column(name = "FREEZE_MONEY")
	private BigDecimal freezeMoney;

	/** 账户总额*/
	public static final String TOTAL_MONEY = "TOTAL_MONEY";
	@Column(name = "TOTAL_MONEY")
	private BigDecimal totalMoney;
	
	/** 获取总金额*/
	public static final String EARNING_MONEY = "EARNING_MONEY";
	@Column(name = "EARNING_MONEY")
	private BigDecimal earningMoney;
	
	/** 获取总金额*/
	public static final String PRASE_MONEY = "PRASE_MONEY";
	@Column(name = "PRASE_MONEY")
	private BigDecimal praseMoney;
	
	@SuppressWarnings("unused")
	private BigDecimal money;

	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public Integer getCusId() {
		return cusId;
	}
	public void setCusId(Integer cusId) {
		this.cusId = cusId;
	}
	public BigDecimal getBalanceMoney() {
		return balanceMoney;
	}
	public void setBalanceMoney(BigDecimal balanceMoney) {
		this.balanceMoney = balanceMoney;
	}
	public BigDecimal getFreezeMoney() {
		return freezeMoney;
	}
	public void setFreezeMoney(BigDecimal freezeMoney) {
		this.freezeMoney = freezeMoney;
	}
	public BigDecimal getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}
	public BigDecimal getEarningMoney() {
		return earningMoney;
	}
	public void setEarningMoney(BigDecimal earningMoney) {
		this.earningMoney = earningMoney;
	}
	public BigDecimal getMoney() {
		return this.balanceMoney.divide(new BigDecimal(10000)).setScale(2,BigDecimal.ROUND_DOWN);
	}
	public BigDecimal getPraseMoney() {
		return praseMoney;
	}
	public void setPraseMoney(BigDecimal praseMoney) {
		this.praseMoney = praseMoney;
	}
	public String getCusLogName() {
		return cusLogName;
	}
	public void setCusLogName(String cusLogName) {
		this.cusLogName = cusLogName;
	}
}
