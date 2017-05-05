package ydh.event.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;
import ydh.cicada.dict.YesNo;
import ydh.event.dict.*;
import ydh.upload.entity.QuoteRelation;

import java.util.Date;
import java.util.List;

@Entity(name = "EVENT")
public class Event {
	
	@Id
	@Column(name = "EVENT_ID")
	private String eventId;

	@Column(name = "EVENT_NAME")
	private String eventName;

	@Column(name = "EVENT_CREATE_DATE")
	private Date eventCreateDate;// 创建时间

	@Column(name = "EVENT_START_DATE")
	private Date eventStartDate;// 开始时间

	@Column(name = "EVENT_FINISH_DATE")
	private Date eventFinishDate;// 用户设置的最后完成时间

	@Column(name = "EVENT_REAL_FINISH_DATE")
	private Date eventRealFinishDate;// 真实完成时间

	@Column(name = "OVER_TIME_FLAG")
	private YesNo overTimerFlag = YesNo.NO;// 超时标识

	@Column(name = "OVER_TIME_STOP")
	private YesNo overTimerStop = YesNo.NO;// 超时是否停止标识

	@Column(name = "EVENT_LAST_CHANGE_DATE")
	private Date eventLastChangeDate;// 最后变更时间(由提交开始,包括多次提交,验收)

	@Column(name = "HIGHER_EVENT_ID")
	private String higherEventId;// 上级事件ID

	@Column(name = "EVENT_DETAIL")
	private String eventDetail; // 事件描述

	@Column(name = "EVENT_STATE")
	private EventState eventState;// 事件进程 0进行中1 已完结 2节点未开始

	@Column(name = "INSPECTED_STATE")
	private InspectedState inspectedState=InspectedState.CHECKING;// 验收状态 0未验收 1已验收

	@Column(name = "COMMENT_COUNT")
	private int commentCount;// 评论数量
	@Column(name = "EVENT_TYPE")
	private EventType eventType=EventType.NORMAL;// 节点类型 0记录过程 1节点验收 2事件完结

	@Column(name = "SHARE_FLAG")
	private YesNo shareFlag=YesNo.YES;// 是否可分享 0 不可 1 可以  搜索可見

	@Column(name = "EVENT_CHANGE_TYPE")
	private EventChangeType eventChangeType=EventChangeType.OPEN;// 0开放 1封闭

	@Column(name = "TEMPLATE_DATA")
	private YesNo templateData=YesNo.NO; // 是否是模板数据
	/**
	 * 事件标签列表
	 */
	private List<EventTag> eventTags;

	private YesNo commentFlag;// 是否已评价
	private String managerName;// 责任人姓名
	private Date changeDate;// 变更时间
	private YesNo changeFlag;// 该事件对于当前人是否有更新
	private EventRole eventRole; // 当前用户在当前事件中充当的角色
	private List<EventCusMapping> eventCusMappings;
	private List<QuoteRelation> quoteRelations;
	private List<EventChangeCondition> eventChangeConditions;

	public Event() {
	}

	public Date getEventRealFinishDate() {
		return eventRealFinishDate;
	}

	public void setEventRealFinishDate(Date eventRealFinishDate) {
		this.eventRealFinishDate = eventRealFinishDate;
	}

	public YesNo getChangeFlag() {
		return changeFlag;
	}

	public void setChangeFlag(YesNo changeFlag) {
		this.changeFlag = changeFlag;
	}

	public EventRole getEventRole() {
		return eventRole;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public void setEventRole(EventRole eventRole) {
		this.eventRole = eventRole;
	}

	

	public List<EventTag> getEventTags() {
		return eventTags;
	}

	public void setEventTags(List<EventTag> eventTags) {
		this.eventTags = eventTags;
	}

	public YesNo getTemplateData() {
		return templateData;
	}

	public void setTemplateData(YesNo templateData) {
		this.templateData = templateData;
	}

	public List<QuoteRelation> getQuoteRelations() {
		return quoteRelations;
	}

	public void setQuoteRelations(List<QuoteRelation> quoteRelations) {
		this.quoteRelations = quoteRelations;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Date getEventCreateDate() {
		return eventCreateDate;
	}

	public void setEventCreateDate(Date eventCreateDate) {
		this.eventCreateDate = eventCreateDate;
	}

	public Date getEventFinishDate() {
		return eventFinishDate;
	}

	public void setEventFinishDate(Date eventFinishDate) {
		this.eventFinishDate = eventFinishDate;
	}

	public String getEventDetail() {
		return eventDetail;
	}

	public void setEventDetail(String eventDetail) {
		this.eventDetail = eventDetail;
	}

	public EventState getEventState() {
		return eventState;
	}

	public void setEventState(EventState eventState) {
		this.eventState = eventState;
	}

	public Date getEventStartDate() {
		return eventStartDate;
	}

	public void setEventStartDate(Date eventStartDate) {
		this.eventStartDate = eventStartDate;
	}

	public String getHigherEventId() {
		return higherEventId;
	}

	public void setHigherEventId(String higherEventId) {
		this.higherEventId = higherEventId;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public YesNo getOverTimerFlag() {
		return overTimerFlag;
	}

	public void setOverTimerFlag(YesNo overTimerFlag) {
		this.overTimerFlag = overTimerFlag;
	}

	public YesNo getOverTimerStop() {
		return overTimerStop;
	}

	public void setOverTimerStop(YesNo overTimerStop) {
		this.overTimerStop = overTimerStop;
	}

	public YesNo getShareFlag() {
		return shareFlag;
	}

	public void setShareFlag(YesNo shareFlag) {
		this.shareFlag = shareFlag;
	}

	public Date getEventLastChangeDate() {
		return eventLastChangeDate;
	}

	public void setEventLastChangeDate(Date eventLastChangeDate) {
		this.eventLastChangeDate = eventLastChangeDate;
	}

	public YesNo getCommentFlag() {
		return commentFlag;
	}

	public void setCommentFlag(YesNo commentFlag) {
		this.commentFlag = commentFlag;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	public List<EventCusMapping> getEventCusMappings() {
		return eventCusMappings;
	}

	public void setEventCusMappings(List<EventCusMapping> eventCusMappings) {
		this.eventCusMappings = eventCusMappings;
	}

	public List<EventChangeCondition> getEventChangeConditions() {
		return eventChangeConditions;
	}

	public void setEventChangeConditions(List<EventChangeCondition> eventChangeConditions) {
		this.eventChangeConditions = eventChangeConditions;
	}

	public EventChangeType getEventChangeType() {
		return eventChangeType;
	}

	public void setEventChangeType(EventChangeType eventChangeType) {
		this.eventChangeType = eventChangeType;
	}

	public InspectedState getInspectedState() {
		return inspectedState;
	}

	public void setInspectedState(InspectedState inspectedState) {
		this.inspectedState = inspectedState;
	}
	
}
