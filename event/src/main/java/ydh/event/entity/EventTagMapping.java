package ydh.event.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;

import java.util.Date;

/**
 * 事件与标签关系表
 * @author tearslee
 *
 */
@Entity(name = "EVENT_TAG_MAPPING")
public class EventTagMapping {

	@Id
	@Column(name = "EVENT_TAG_MAPPING_ID")
	private String eventTagMappingId;
	/**
	 * 事件标签id
	 */
	@Column(name = "EVENT_TAG_ID")
	private String eventTagId;
	
	/**
	 * 事件id
	 */
	@Column(name = "EVENT_ID")
	private String eventId;
	
	/**
	 * 改变日期
	 */
	@Column(name = "CHANGE_DATE")
	private Date changeDate;

	public String getEventTagMappingId() {
		return eventTagMappingId;
	}

	public void setEventTagMappingId(String eventTagMappingId) {
		this.eventTagMappingId = eventTagMappingId;
	}

	public String getEventTagId() {
		return eventTagId;
	}

	public void setEventTagId(String eventTagId) {
		this.eventTagId = eventTagId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	
	
}
