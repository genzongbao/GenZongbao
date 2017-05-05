package ydh.message.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;
import ydh.cicada.dict.YesNo;

/**
 * 用户与消息关系体
 * @author tearslee
 *
 */
@Entity(name="CUS_MESSAGE_MAPPING")
public class CusMessageMapping {
	
	@Id
	@Column(name="CUS_MESSAGE_MAPPING_ID")
	private String cusMessageMappingId;
	/**
	 * 消息id
	 */
	@Column(name="MESSAGE_ID")
	private String messageId;
	/**
	 * 消息分类
	 */
	@Column(name="CLASSIFICATION")
	private Classification classification=Classification.PRIVATE_NOTICE;
	/**
	 * 用户id
	 */
	@Column(name="CUS_ID")
	private int cus_Id;
	/**
	 * 是否已读标识(用户可以禁用消息通知，这样的情况下，存在事件已更新然而用户没有收到消息的情况。用户与事件是否已读标识就需要额外的字段)
	 */
	@Column(name="READ_FLAG")
	private YesNo readFlag=YesNo.NO;

	public String getCusMessageMappingId() {
		return cusMessageMappingId;
	}

	public void setCusMessageMappingId(String cusMessageMappingId) {
		this.cusMessageMappingId = cusMessageMappingId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public int getCus_Id() {
		return cus_Id;
	}

	public void setCus_Id(int cus_Id) {
		this.cus_Id = cus_Id;
	}

	public YesNo getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(YesNo readFlag) {
		this.readFlag = readFlag;
	}

	public Classification getClassification() {
		return classification;
	}

	public void setClassification(Classification classification) {
		this.classification = classification;
	}
	
	
}
