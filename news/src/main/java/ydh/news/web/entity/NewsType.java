package ydh.news.web.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;
import ydh.customer.dict.CustomerMessageType;

@Entity(name = "NEWS_TYPE")
public class NewsType {
	
	@Id
	@Column(name = "NEWS_TYPE_ID")
	private Integer newsTypeId;
	
	@Column(name = "NEWS_TYPE_NAME")
	private String newsTypeName;
	
	@Column(name = "CUSTOMER_MESSAGE_TYPE")
	private CustomerMessageType messageType;

	
	public Integer getNewsTypeId() {
		return newsTypeId;
	}

	public void setNewsTypeId(Integer newsTypeId) {
		this.newsTypeId = newsTypeId;
	}

	public String getNewsTypeName() {
		return newsTypeName;
	}

	public void setNewsTypeName(String newsTypeName) {
		this.newsTypeName = newsTypeName;
	}

	public CustomerMessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(CustomerMessageType messageType) {
		this.messageType = messageType;
	}
}
