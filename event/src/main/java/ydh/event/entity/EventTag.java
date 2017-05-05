package ydh.event.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;
import ydh.event.dict.TagStatus;
import ydh.event.dict.TagType;

import java.util.Date;

@Entity(name = "EVENT_TAG")
public class EventTag {
	
	@Id
	@Column(name = "EVENT_TAG_ID")
	private String eventTagId;

	@Column(name = "EVENT_TAG_NAME")
	private String eventTagName;
	
	@Column(name = "CUS_ID")
	private int cusId;
	
	@Column(name = "EVENT_TAG_CREATE_DATE")
	private Date eventTagCreateDate;
	/**
	 * 是否有效
	 */
	@Column(name = "TAG_STATUS")
	private TagStatus tagStatus;
	/**
	 * 标签顺序
	 */
	@Column(name="SORT_INDEX")
	private int sortIndex;
	/**
	 * 标签类型
	 */
	@Column(name="TAG_TYPE")
	private TagType tagType=TagType.PRIVATE;
	
	public EventTag() {
	}
	
	
	
	public TagType getTagType() {
		return tagType;
	}



	public void setTagType(TagType tagType) {
		this.tagType = tagType;
	}



	public int getSortIndex() {
		return sortIndex;
	}


	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}


	public int getCusId() {
		return cusId;
	}

	public void setCusId(int cusId) {
		this.cusId = cusId;
	}



	public Date getEventTagCreateDate() {
		return eventTagCreateDate;
	}

	public void setEventTagCreateDate(Date eventTagCreateDate) {
		this.eventTagCreateDate = eventTagCreateDate;
	}

	public TagStatus getTagStatus() {
		return tagStatus;
	}

	public void setTagStatus(TagStatus tagStatus) {
		this.tagStatus = tagStatus;
	}

	public String getEventTagId() {
		return eventTagId;
	}

	public void setEventTagId(String eventTagId) {
		this.eventTagId = eventTagId;
	}

	public String getEventTagName() {
		return eventTagName;
	}

	public void setEventTagName(String eventTagName) {
		this.eventTagName = eventTagName;
	}

}
