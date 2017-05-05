package ydh.website.localization.entity;

import java.util.Date;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;

@Entity(name = "WEBSITE_EXCEPTION")
public class WebSiteException {
	
	/** 系统异常ID*/
	@Id(autoincrement=true)
	@Column(name = "EXCEPTION_ID")
	private Integer exceptionId;

	/** 异常标题-我们自己定义的一段文字*/
	@Column(name = "EXCEPTION_TITLE")
	private String exceptionTitle;

	/** 异常的消息*/
	@Column(name = "EXCEPTION_MESSAGE")
	private String exceptionMessage;

	/** 异常的堆栈信息*/
	@Column(name = "EXCEPTION_CONTENT")
	private String exceptionContent;

	/** 异常发生的时间*/
	@Column(name = "EXCEPTION_TIME")
	private Date exceptionTime;

	public Integer getExceptionId() {
		return exceptionId;
	}

	public void setExceptionId(Integer exceptionId) {
		this.exceptionId = exceptionId;
	}

	public String getExceptionTitle() {
		return exceptionTitle;
	}

	public void setExceptionTitle(String exceptionTitle) {
		this.exceptionTitle = exceptionTitle;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public String getExceptionContent() {
		return exceptionContent;
	}

	public void setExceptionContent(String exceptionContent) {
		this.exceptionContent = exceptionContent;
	}

	public Date getExceptionTime() {
		return exceptionTime;
	}

	public void setExceptionTime(Date exceptionTime) {
		this.exceptionTime = exceptionTime;
	}
	
	
	

}
