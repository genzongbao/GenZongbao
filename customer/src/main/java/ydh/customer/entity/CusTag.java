package ydh.customer.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;

import java.util.Date;

@Entity(name="CUS_TAG")
public class CusTag {
	

	@Id
	@Column(name="CUS_TAG_ID")
	private String cusTagId;
	
	@Column(name="CUS_TAG_NAME")
	private String cusTagName;
	
	@Column(name="CUS_ID")
	private int cusId;
	
	@Column(name="CREATE_DATE")
	private Date createDate;
	/**
	 * 标签下的用户数量
	 */
	@Column(name="COUNT_CUS")
	private int countCus;

	
	public int getCountCus() {
		return countCus;
	}

	public void setCountCus(int countCus) {
		this.countCus = countCus;
	}

	public String getCusTagId() {
		return cusTagId;
	}

	public void setCusTagId(String cusTagId) {
		this.cusTagId = cusTagId;
	}
	
	

	public String getCusTagName() {
		return cusTagName;
	}

	public void setCusTagName(String cusTagName) {
		this.cusTagName = cusTagName;
	}

	public int getCusId() {
		return cusId;
	}

	public void setCusId(int cusId) {
		this.cusId = cusId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	
}
