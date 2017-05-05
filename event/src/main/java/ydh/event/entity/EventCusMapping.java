package ydh.event.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;
import ydh.cicada.dict.YesNo;
import ydh.event.dict.EventRole;

import java.util.Date;

@Entity(name = "EVENT_CUS_MAPPING")
public class EventCusMapping {
	
	@Id
	@Column(name = "EVENT_CUS_MAPPING_ID")
	private String eventCusMappingId;

	@Column(name = "CUS_ID")
	private Integer cusId;
	
	@Column(name = "EVENT_ROLE")
	private EventRole eventRole;
	
	@Column(name = "EVENT_ID")
	private String eventId;
	
	@Column(name = "CHANGE_FLAG")
	private YesNo changeFlag=YesNo.NO;//是否已变更
	
	@Column(name = "COMMENT_FLAG")
	private YesNo commentFlag=YesNo.NO;//是否已评价
	
	@Column(name = "CHANGE_DATE")
	private Date changeDate;//变更时间
	private String cusLogName;
	private String cusHeadImg;
	private String cusPhone;
	public EventCusMapping() {
	}

	public String getEventCusMappingId() {
		return eventCusMappingId;
	}

	public void setEventCusMappingId(String eventCusMappingId) {
		this.eventCusMappingId = eventCusMappingId;
	}

	public Integer getCusId() {
		return cusId;
	}

	public void setCusId(Integer cusId) {
		this.cusId = cusId;
	}

	public EventRole getEventRole() {
		return eventRole;
	}

	public void setEventRole(EventRole eventRole) {
		this.eventRole = eventRole;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}


	public String getCusLogName() {
		return cusLogName;
	}

	public void setCusLogName(String cusLogName) {
		this.cusLogName = cusLogName;
	}

	public String getCusHeadImg() {
		return cusHeadImg;
	}

	public void setCusHeadImg(String cusHeadImg) {
		this.cusHeadImg = cusHeadImg;
	}

	public String getCusPhone() {
		return cusPhone;
	}

	public void setCusPhone(String cusPhone) {
		this.cusPhone = cusPhone;
	}

	public YesNo getChangeFlag() {
		return changeFlag;
	}

	public void setChangeFlag(YesNo changeFlag) {
		this.changeFlag = changeFlag;
	}

	public YesNo getCommentFlag() {
		return commentFlag;
	}

	public void setCommentFlag(YesNo commentFlag) {
		this.commentFlag = commentFlag;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

}
