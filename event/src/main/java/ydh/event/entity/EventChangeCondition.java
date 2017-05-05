package ydh.event.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;
import ydh.cicada.dict.YesNo;
import ydh.event.dict.ActionType;
import ydh.event.dict.EventState;

import java.util.Date;

/**
 * 事件改变条件
 * @author Administrator
 *
 */
@Entity(name = "EVENT_CHANGE_CONDITION")
public class EventChangeCondition {
	
	@Id
	@Column(name = "EVENT_CHANGE_CONDITION_ID")
	private String eventChangeConditionId;
	
	@Column(name = "EVENT_ID")
	private String eventId;//被改变事件ID
	
	@Column(name = "EVENT_STATE")
	private EventState eventState; //事件状态 0未开始 1 执行中 2待验收  3成功 4 失败
	
	@Column(name = "CONDITION_EVENT_ID")
	private String conditionEventId;//条件事件ID
	
	@Column(name = "CONDITION_EVENT_STATE")
	private EventState conditionEventState;//条件事件状态
	
	@Column(name = "CONDITION_AND")
	private YesNo conditionAnd=YesNo.NO;//是否全部满足 0 否 1 是
	
	@Column(name = "CONDITION_CREATE_DATE")
	private Date conditionCreateDate;//创建时间
	/**
	 * 操作类型
	 */
	private ActionType actionType=ActionType.UPDATE;
	/**
	 * 条件是否已满足0否，1是
	 */
	@Column(name="IS_FULFIL")
	private YesNo isFulfil=YesNo.NO;
	
	private String eventName;//关联的事件名称
	
	private String conditionEventName;//被关联的条件节点名称
	
	public String getConditionEventName() {
		return conditionEventName;
	}



	public void setConditionEventName(String conditionEventName) {
		this.conditionEventName = conditionEventName;
	}



	public String getEventName() {
		return eventName;
	}



	public ActionType getActionType() {
		return actionType;
	}



	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}



	public void setEventName(String eventName) {
		this.eventName = eventName;
	}



	public YesNo getIsFulfil() {
		return isFulfil;
	}



	public void setIsFulfil(YesNo isFulfil) {
		this.isFulfil = isFulfil;
	}



	public String getEventChangeConditionId() {
		return eventChangeConditionId;
	}

	public void setEventChangeConditionId(String eventChangeConditionId) {
		this.eventChangeConditionId = eventChangeConditionId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public EventState getEventState() {
		return eventState;
	}

	public void setEventState(EventState eventState) {
		this.eventState = eventState;
	}

	public String getConditionEventId() {
		return conditionEventId;
	}

	public void setConditionEventId(String conditionEventId) {
		this.conditionEventId = conditionEventId;
	}

	public EventState getConditionEventState() {
		return conditionEventState;
	}

	public void setConditionEventState(EventState conditionEventState) {
		this.conditionEventState = conditionEventState;
	}

	public Date getConditionCreateDate() {
		return conditionCreateDate;
	}

	public void setConditionCreateDate(Date conditionCreateDate) {
		this.conditionCreateDate = conditionCreateDate;
	}


	public YesNo getConditionAnd() {
		return conditionAnd;
	}



	public void setConditionAnd(YesNo conditionAnd) {
		this.conditionAnd = conditionAnd;
	}


	
}
