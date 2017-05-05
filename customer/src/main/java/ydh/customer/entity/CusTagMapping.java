package ydh.customer.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;

import java.util.Date;

@Entity(name="CUS_TAG_MAPPING")
public class CusTagMapping {

	@Id
	@Column(name="CUS_TAG_MAPPING_ID")
	private String cusTagMappingId;
	
	@Column(name="CUS_TAG_ID")
	private String cusTagId;
	
	/**
	 * 所关联用户id
	 */
	@Column(name="CUS_ID")
	private int cusId;
	
	@Column(name="CHANGE_DATE")
	private Date changeDate;

	public String getCusTagMappingId() {
		return cusTagMappingId;
	}

	public void setCusTagMappingId(String cusTagMappingId) {
		this.cusTagMappingId = cusTagMappingId;
	}

	public String getCusTagId() {
		return cusTagId;
	}

	public void setCusTagId(String cusTagId) {
		this.cusTagId = cusTagId;
	}

	public int getCusId() {
		return cusId;
	}

	public void setCusId(int cusId) {
		this.cusId = cusId;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	
	
}
