package ydh.event.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;
import ydh.event.dict.CommentType;
import ydh.upload.entity.QuoteRelation;
import ydh.utils.GsonUtil;

import java.util.Date;
import java.util.List;

@Entity(name = "EVENT_COMMENT")
public class EventComment {
	
	@Id
	@Column(name = "EVENT_COMMENT_ID")
	private String eventCommentId;
	//评论用户
	@Column(name = "CUS_ID")
	private Integer cusId;
	//评论内容
	@Column(name = "COMMENT_CONTENT")
	private String commentContent;
	/**
	 * 评论时间
	 */
	@Column(name = "COMMENT_DATE")
	private Date commentDate;
	//	事件/节点 id
	@Column(name = "EVENT_ID")
	private String eventId;
	//位置  json数据，包含坐标
	@Column(name = "LOCATION_AND_CODE")
	private String locationAndCode;
	//评论类型
	@Column(name = "COMMENT_TYPE")
	private CommentType commentType=CommentType.NORMAL;
	
	private Location location;
	private String cusHeadImg;
	private String cusLogName;
	private List<QuoteRelation> quoteRelations;	//引用关系
	public EventComment() {
	}
	
	

	public String getEventCommentId() {
		return eventCommentId;
	}



	public void setEventCommentId(String eventCommentId) {
		this.eventCommentId = eventCommentId;
	}

	

	public Location getLocation() {
		return location;
	}



	public void setLocation(Location location) {
		this.locationAndCode=GsonUtil.toJSONString(location);
		this.location = location;
	}



	public Integer getCusId() {
		return cusId;
	}

	public void setCusId(Integer cusId) {
		this.cusId = cusId;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public Date getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getCusHeadImg() {
		return cusHeadImg;
	}

	public void setCusHeadImg(String cusHeadImg) {
		this.cusHeadImg = cusHeadImg;
	}

	public String getCusLogName() {
		return cusLogName;
	}

	public void setCusLogName(String cusLogName) {
		this.cusLogName = cusLogName;
	}




	public String getLocationAndCode() {
		return locationAndCode;
	}



	public void setLocationAndCode(String locationAndCode) {
		this.location=GsonUtil.toObject(locationAndCode, Location.class);
		this.locationAndCode = locationAndCode;
	}



	public CommentType getCommentType() {
		return commentType;
	}

	public void setCommentType(CommentType commentType) {
		this.commentType = commentType;
	}



	public List<QuoteRelation> getQuoteRelations() {
		return quoteRelations;
	}



	public void setQuoteRelations(List<QuoteRelation> quoteRelations) {
		this.quoteRelations = quoteRelations;
	}



	
	
}
