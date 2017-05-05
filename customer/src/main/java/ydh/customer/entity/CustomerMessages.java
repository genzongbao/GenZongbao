package ydh.customer.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;
import ydh.message.dict.MessageState;

import java.util.Date;

@Entity(name="CUSTOMER_MESSAGES")
public class CustomerMessages {

	/** 消息ID*/
	@Id(autoincrement=true)
	@Column(name = "MSG_ID")
	private Integer msgId;

	/** 消息类型*/
	public static final String MESSAGE_TYPE = "MESSAGE_TYPE";
	@Column(name = "MESSAGE_TYPE")
	private Integer messageType;
	
	/** 消息标题*/
	@Column(name = "MSG_TITLE")
	private String msgTitle;

	/** 消息内容*/
	@Column(name = "MSG_CONTENT")
	private String msgContent;

	/** 用户ID*/
	@Column(name = "CUS_ID")
	private Integer cusId;

	/** 接收时间*/
	@Column(name = "RECEIVE_TIME")
	private Date receiveTime;

	/** 是否阅读*/
	@Column(name = "IS_READ")
	private MessageState isRead;
	
	public CustomerMessages(){}
	
	public CustomerMessages(Integer msgId, String msgTitle, String msgContent,
			Integer cusId, Date receiveTime, MessageState isRead) {
		super();
		this.msgId = msgId;
		this.msgTitle = msgTitle;
		this.msgContent = msgContent;
		this.cusId = cusId;
		this.receiveTime = receiveTime;
		this.isRead = isRead;
	}

	public Integer getMsgId() {
		return msgId;
	}

	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}

	public String getMsgTitle() {
		return msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public Integer getCusId() {
		return cusId;
	}

	public void setCusId(Integer cusId) {
		this.cusId = cusId;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public MessageState getIsRead() {
		return isRead;
	}

	public void setIsRead(MessageState isRead) {
		this.isRead = isRead;
	}

	public Integer getMessageType() {
		return messageType;
	}

	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	

}
