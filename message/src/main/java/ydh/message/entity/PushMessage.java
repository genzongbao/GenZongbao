package ydh.message.entity;

import java.util.Date;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;
import ydh.cicada.dict.YesNo;

@Entity(name="PUSH_MESSAGE")
public class PushMessage {

	@Id
	@Column(name="PUSH_MESSAGE_ID")
	private String pushMessageId;
	
	@Column(name="TITLE")
	private String title;
	
	@Column(name="CONTENT")
	private String content;
	
	@Column(name="URL")
	private String url;
	
	/**
	 * 关联id/事件、公告、评论
	 */
	@Column(name="CONDITION_ID")
	private String conditionId;
	/**
	 * 消息类型（事件，链接，普通）
	 */
	@Column(name="MESSAGE_TYPE")
	private MessageType messageType=MessageType.NORMAL_MESSAGE;
	/**
	 * 是否已读(in cus_message_mapping)
	 * 消息体内不存是否已读
	 */
	private YesNo readFlag=YesNo.NO;
	
	@Column(name="PUSH_DATE")
	private Date pushDate;

	public String getPushMessageId() {
		return pushMessageId;
	}

	public void setPushMessageId(String pushMessageId) {
		this.pushMessageId = pushMessageId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getConditionId() {
		return conditionId;
	}

	public void setConditionId(String conditionId) {
		this.conditionId = conditionId;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	
	public YesNo getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(YesNo readFlag) {
		this.readFlag = readFlag;
	}

	public Date getPushDate() {
		return pushDate;
	}

	public void setPushDate(Date pushDate) {
		this.pushDate = pushDate;
	}
	
}
