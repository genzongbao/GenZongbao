package ydh.event.query;

import ydh.cicada.query.PagableQueryCmd;
import ydh.cicada.query.api.Query;
import ydh.cicada.query.api.QueryParam;
import ydh.event.dict.EventRole;
import ydh.event.dict.EventState;
import ydh.event.dict.InspectedState;
import ydh.event.dict.SortCondition;

import java.util.Date;

//@Query(	from = "EVENT E LEFT JOIN EVENT_CUS_MAPPING M ON E.EVENT_ID=M.EVENT_ID"
//		+ " LEFT JOIN EVENT_TAG_MAPPING TM ON E.EVENT_ID=TM.EVENT_ID"
//		+ " LEFT JOIN EVENT_TAG T ON TM.EVENT_TAG_ID=T.EVENT_TAG_ID",
//		distince=true,
//		select="E.EVENT_ID,E.EVENT_NAME,E.EVENT_CREATE_DATE,"
//				+ " E.EVENT_START_DATE,E.EVENT_FINISH_DATE,"
//				+ " E.OVER_TIME_FLAG,E.OVER_TIME_STOP,E.EVENT_LAST_CHANGE_DATE,"
//				+ " E.EVENT_STATE,E.INSPECTED_STATE",
//				where=" (SELECT COUNT(1) FROM EVENT EE "
//						+ " LEFT JOIN EVENT_CUS_MAPPING MM ON MM.EVENT_ID=EE.EVENT_ID"
//						+ " WHERE EE.HIGHER_EVENT_ID=E.EVENT_ID"
//						+ " AND {cusId} {_eventRole}"
//						+ ")>0 OR(M.CUS_ID={_cusId} AND ISNULL(E.HIGHER_EVENT_ID))")
@Query(	from = "EVENT E LEFT JOIN EVENT_CUS_MAPPING M ON E.EVENT_ID=M.EVENT_ID"
		+ " LEFT JOIN EVENT_TAG_MAPPING TM ON E.EVENT_ID=TM.EVENT_ID"
		+ " LEFT JOIN EVENT_TAG T ON TM.EVENT_TAG_ID=T.EVENT_TAG_ID",
		distince=true,select="E.EVENT_ID,E.EVENT_NAME,E.EVENT_CREATE_DATE,"
				+ "E.EVENT_START_DATE,E.EVENT_FINISH_DATE,E.OVER_TIME_FLAG,"
				+ "E.OVER_TIME_STOP,E.EVENT_LAST_CHANGE_DATE,E.EVENT_STATE,"
				+ "E.INSPECTED_STATE")
public class EventQuery extends PagableQueryCmd{
	

	@QueryParam(fieldName = "M.CUS_ID",embed=true)
	private Integer cusId;
	@QueryParam(fieldName = "MM.CUS_ID",embed=true)
	private Integer _cusId;
	
	@QueryParam(fieldName = "M.EVENT_ROLE",embed=true)
	private EventRole eventRole;//角色
	
	@QueryParam(fieldName = "E.EVENT_STATE")
	private EventState eventState;//进程
	
	@QueryParam(fieldName = "T.EVENT_TAG_NAME")
	private String eventTagName;//分类
	
	@QueryParam(fieldName = "E.HIGHER_EVENT_ID")
	private String higherEventId;
	
	/**
	 * 是否是事件，
	 */
	@QueryParam(stmt = "ISNULL(E.HIGHER_EVENT_ID)")
	private Boolean top;
	
	@QueryParam(fieldName = "E.EVENT_ID")
	private String eventId;
	
	@QueryParam(fieldName = "E.EVENT_CREATE_DATE")
	private Date createDate;
	//	@QueryParam(fieldName = "M.MAIN_EVENT_IDS", op = QueryOperator.LEFTLIKE)
	@QueryParam(fieldName = "E.EVENT_LAST_CHANGE_DATE")
	private Date eventLastChangeDate;
	/**
	 * 验收状态
	 */
	@QueryParam(fieldName="E.INSPECTED_STATE")
	private InspectedState inspectedState;
	/**
	 * 排序条件
	 */
	private SortCondition sortCondition;
	
	@QueryParam(fieldName=" AND 1",embed=true)
	private EventRole eventRoleSql;
	@QueryParam(fieldName=" AND 1",embed=true)
	private EventRole nodeRoleSql;//
	
	@QueryParam(stmt="{cusId} {eventRoleSql}"
			+ " OR "
			+ "(SELECT COUNT(1) FROM EVENT EE"
			+ " LEFT JOIN EVENT_CUS_MAPPING MM ON MM.EVENT_ID=EE.EVENT_ID"
			+ " WHERE EE.HIGHER_EVENT_ID=E.EVENT_ID "
			+ " AND {_cusId} {nodeRoleSql}"
			+ ")>0",fieldName="sql")
	private String sql="1";
	


	

	public EventRole getEventRoleSql() {
		return eventRoleSql;
	}


	public void setEventRoleSql(EventRole eventRoleSql) {
		this.eventRoleSql = eventRoleSql;
	}


	public EventRole getNodeRoleSql() {
		return nodeRoleSql;
	}


	public void setNodeRoleSql(EventRole nodeRoleSql) {
		this.nodeRoleSql = nodeRoleSql;
	}


	public String getSql() {
		return sql;
	}


	public void setSql(String sql) {
		this.sql = sql;
	}


	public SortCondition getSortCondition() {
		return sortCondition;
	}


	public InspectedState getInspectedState() {
		return inspectedState;
	}


	public void setInspectedState(InspectedState inspectedState) {
		this.inspectedState = inspectedState;
	}


	public void setSortCondition(SortCondition sortCondition) {
		if(sortCondition.equals(SortCondition.LONGTIME)){
			//时长排序
			super.setO("TIMESTAMPDIFF(SECOND,E.EVENT_CREATE_DATE,NOW())","E.EVENT_LAST_CHANGE_DATE");
		}else{
			//最近更新排序
			super.setO("!E.EVENT_LAST_CHANGE_DATE");
		}
		this.sortCondition = sortCondition;
	}


	public Integer getCusId() {
		return cusId;
	}


	public void setCusId(Integer cusId) {
		this._cusId= cusId;
		this.cusId = cusId;
	}


	public Integer get_cusId() {
		return _cusId;
	}


	public void set_cusId(Integer _cusId) {
		this._cusId = _cusId;
	}


	public EventRole getEventRole() {
		return eventRole;
	}


	public void setEventRole(EventRole eventRole) {
//		eventRoleSql="1 AND M.EVENT_ROLE="+eventRole;
//		nodeRoleSql="1 AND MM.EVENT_ROLE="+eventRole;
		eventRoleSql=eventRole;
		nodeRoleSql=eventRole;
		this.eventRole = eventRole;
	}


	public EventState getEventState() {
		return eventState;
	}


	public void setEventState(EventState eventState) {
		this.eventState = eventState;
	}


	public String getEventTagName() {
		return eventTagName;
	}


	public void setEventTagName(String eventTagName) {
		this.eventTagName = eventTagName;
	}


	public String getHigherEventId() {
		return higherEventId;
	}


	public void setHigherEventId(String higherEventId) {
		this.higherEventId = higherEventId;
	}


	public Boolean getTop() {
		return top;
	}


	public void setTop(Boolean top) {
		this.top = top;
	}


	public String getEventId() {
		return eventId;
	}


	public void setEventId(String eventId) {
		this.eventId = eventId;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public Date getEventLastChangeDate() {
		return eventLastChangeDate;
	}


	public void setEventLastChangeDate(Date eventLastChangeDate) {
		this.eventLastChangeDate = eventLastChangeDate;
	}

	
	
}
