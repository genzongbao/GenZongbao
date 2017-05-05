package ydh.event.query;

import ydh.cicada.query.PagableQueryCmd;
import ydh.cicada.query.api.Query;
import ydh.cicada.query.api.QueryParam;

import java.util.Date;

@Query(	from = "EVENT_TAG E",
	orderBy = "E.EVENT_TYPE_CREATE_DATE DESC")
public class EventTagQuery extends PagableQueryCmd {

	@QueryParam(fieldName = "E.EVENT_TAG_ID")
	private Integer eventTagId;
	
	@QueryParam(fieldName = "E.EVENT_TAG_NAME")
	private String eventTagName;
	
	@QueryParam(fieldName = "E.CUS_ID")
	private Integer cusId;
	
	@QueryParam(fieldName = "E.EVENT_TYPE_CREATE_DATE")
	private Date eventTypeCreateDate;

	public Integer getEventTagId() {
		return eventTagId;
	}

	public void setEventTagId(Integer eventTagId) {
		this.eventTagId = eventTagId;
	}

	public String getEventTagName() {
		return eventTagName;
	}

	public void setEventTagName(String eventTagName) {
		this.eventTagName = eventTagName;
	}

	public Integer getCusId() {
		return cusId;
	}

	public void setCusId(Integer cusId) {
		this.cusId = cusId;
	}

	public Date getEventTypeCreateDate() {
		return eventTypeCreateDate;
	}

	public void setEventTypeCreateDate(Date eventTypeCreateDate) {
		this.eventTypeCreateDate = eventTypeCreateDate;
	}
}
