package ydh.event.query;

import ydh.cicada.query.PagableQueryCmd;
import ydh.cicada.query.api.Query;
import ydh.cicada.query.api.QueryParam;
import ydh.event.dict.CommentType;

@Query(	from = "EVENT_COMMENT E LEFT JOIN CUSTOMER C ON E.CUS_ID=C.CUS_ID",orderBy = "E.COMMENT_DATE DESC")
public class EventCommenterQuery extends PagableQueryCmd{

	@QueryParam(fieldName = "E.CUS_ID")
	private Integer cusId;
	
	@QueryParam(fieldName = "E.EVENT_ID")
	private String eventId;
	
	@QueryParam(fieldName = "E.COMMENT_TYPE")
	private CommentType CommentType;

	public EventCommenterQuery() {
	}

	public Integer getCusId() {
		return cusId;
	}

	public void setCusId(Integer cusId) {
		this.cusId = cusId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public CommentType getCommentType() {
		return CommentType;
	}

	public void setCommentType(CommentType commentType) {
		CommentType = commentType;
	}
	
	
}
