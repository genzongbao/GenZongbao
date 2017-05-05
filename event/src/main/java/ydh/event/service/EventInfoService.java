package ydh.event.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ydh.cicada.dao.JdbcDao;
import ydh.cicada.dict.YesNo;
import ydh.cicada.query.QueryObject;
import ydh.customer.entity.Customer;
import ydh.event.dict.EventRole;
import ydh.event.dict.EventState;
import ydh.event.dict.EventType;
import ydh.event.dict.TagType;
import ydh.event.entity.Event;
import ydh.event.entity.EventChangeCondition;
import ydh.event.entity.EventCusMapping;
import ydh.event.entity.EventTag;
import ydh.message.entity.MessageType;
import ydh.upload.entity.QuoteRelation;
import ydh.utils.CheckTool;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件详情处理
 * @author tearslee
 *
 */
@Service
@Transactional(rollbackFor={Exception.class})
public class EventInfoService {
	@Autowired
	private JdbcDao dao;
	
	/**
	 * 根据事件id查询事件下的节点
	 * @param eventId
	 * @return
	 */
	public List<Event> getEventNodes(String eventId, int cusId, int progressType, int fallowId){
		List<Event> nodes=new ArrayList<Event>();
		try {			
			EventCusMapping mapping=QueryObject.select(EventCusMapping.class)
					.cond("CUS_ID").equ(cusId)
					.cond("EVENT_ID").equ(eventId)
					.firstResult(dao);
			if(mapping!=null && fallowId==0){
				QueryObject<Event> query=QueryObject.select(Event.class).distinct(true)
						.cond("HIGHER_EVENT_ID").equ(eventId)
						.asc("EVENT_CREATE_DATE");
				if(progressType==1){//只看过程
					query.cond("EVENT_TYPE").equ(EventType.RECORD);
				}else if(progressType==2){//不看进程
					query.cond("EVENT_TYPE").notEqu(EventType.RECORD);
				}
				List<Event> list=query.asc("EVENT_CREATE_DATE").list(dao);
				if(list!=null){
					for (Event event : list) {
						nodes.add(getEventDetail(event.getEventId(), 0));
					}
					Event mainEventFinishNode=getEventDetail(eventId,0);
					mainEventFinishNode.setEventName("事件完结");
					nodes.add(mainEventFinishNode);
				}
			}else{
				//TODO 筛选问题
				QueryObject<EventCusMapping> query=QueryObject.select("M.EVENT_ID",EventCusMapping.class)
						.from("EVENT_CUS_MAPPING M "
								+" LEFT JOIN EVENT E on E.EVENT_ID=M.EVENT_ID")
						.distinct(true)
						
						.cond("E.HIGHER_EVENT_ID").equ(eventId);
				if(fallowId!=0){
					query.cond("M.CUS_ID").equ(fallowId);
					query.cond("M.EVENT_ROLE").equ(EventRole.MANAGER);
				}else{
					query.cond("M.CUS_ID").equ(cusId);
				}
				if(progressType==1){//只看过程
					query.cond("EVENT_TYPE").equ(EventType.RECORD);
				}else if(progressType==2){//不看进程
					query.cond("EVENT_TYPE").notEqu(EventType.RECORD);
				}
				List<EventCusMapping> mappings=query.list(dao);
				if(CheckTool.checkListIsNotNull(mappings)){
					for (EventCusMapping eventCusMapping : mappings) {
						nodes.add(getEventDetail(eventCusMapping.getEventId(),0));
					}
				}
			}	
		} catch (Exception e) {
			nodes=null;
			e.printStackTrace();
		}
		return nodes;
	}
	/**
	 * 查询事件相关人员详情
	 * @param mapping
	 * @return
	 */
	public EventCusMapping getMappingDetail(EventCusMapping mapping){
		Customer cus=dao.load(Customer.class, mapping.getCusId());
		mapping.setCusLogName(cus.getCusLogName());
		mapping.setCusHeadImg(cus.getCusHeadImg());
		mapping.setCusPhone(cus.getCusPhone());
		return mapping;
	}
	
	/**
	 * 根据事件id查询事件详情
	 * @param eventId
	 * @return
	 */
	public Event getEventDetail(String eventId,int cusId){
		Event event=dao.load(Event.class, eventId);
		List<EventCusMapping> mappings=QueryObject.select(EventCusMapping.class)
				.cond("EVENT_ID").equ(eventId).list(dao);
		if(CheckTool.checkListIsNotNull(mappings)){
			for (EventCusMapping eventCusMapping : mappings) {
				getMappingDetail(eventCusMapping);
			}
		}
		event.setQuoteRelations(QueryObject.select(QuoteRelation.class)
				.cond("SOURCE_ID").equ(eventId).asc("QUOTE_SOURCE_TYPE").asc("QUOTE_SOURCE_SUFFIX").list(dao));
		event.setEventCusMappings(mappings);
		if(cusId!=0){
			//更新相关人员为已读评论
			String eventReadFlagRefushSql="update event_cus_mapping m"
					+ " left join event e on e.event_id=m.event_id"
					+ " set m.change_flag=?"
					+ " where (e.event_id=? or e.HIGHER_EVENT_ID=?) and m.cus_id=?";
			String MessageReadFlagRefushSql="update cus_message_mapping m"
					+ " left join push_message p on m.MESSAGE_ID=p.PUSH_MESSAGE_ID"
					+ " set m.READ_FLAG=?"
					+ " where m.cus_id=?"
					+ " and p.CONDITION_ID=? and p.MESSAGE_TYPE=?";
			try {			
				this.dao.getJdbcTemplate().update(eventReadFlagRefushSql,new Object[]{YesNo.NO.ordinal(),eventId,eventId,cusId});
				//消息更新
				this.dao.getJdbcTemplate().update(MessageReadFlagRefushSql,new Object[]{YesNo.NO.ordinal(),cusId,eventId, MessageType.EVENT_MESSAGE.ordinal()});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return event;
	}
	/**
	 * 根据事件id查询事件详情,评论，附件，标签
	 * @param eventId
	 * @return
	 */
	public Event getEventDetailInfo(String eventId,int cusId){
		Event event=dao.load(Event.class, eventId);
		List<EventCusMapping> mappings=QueryObject.select(EventCusMapping.class)
				.cond("EVENT_ID").equ(eventId)
				.asc("EVENT_ROLE")
				.list(dao);
		if(CheckTool.checkListIsNotNull(mappings)){
			for (EventCusMapping eventCusMapping : mappings) {
				getMappingDetail(eventCusMapping);
			}
		}
		event.setEventChangeConditions(QueryObject.select("d.*,e.event_name as eventName,c.event_name as conditionEventName",EventChangeCondition.class)
				.from(" EVENT_CHANGE_CONDITION d "
						+ " left join EVENT e on d.event_id=e.event_id"
						+ " left join EVENT c on d.CONDITION_EVENT_ID=c.event_id")
				.cond("d.EVENT_ID").equ(eventId)
				.list(dao));
		event.setQuoteRelations(QueryObject.select(QuoteRelation.class)
				.cond("SOURCE_ID").equ(eventId).asc("QUOTE_SOURCE_TYPE").asc("QUOTE_SOURCE_SUFFIX").list(dao));
		event.setEventCusMappings(mappings);
		event.setEventTags(QueryObject.select("T.*",EventTag.class)
				.from("EVENT_TAG T LEFT JOIN EVENT_TAG_MAPPING P ON T.EVENT_TAG_ID=P.EVENT_TAG_ID")
				.cond("P.EVENT_ID").equ(eventId)
				.cond("TAG_TYPE").equ(TagType.PRIVATE)
				.cond("T.CUS_ID").equ(cusId)
				.list(dao));
		
		return event;
	}
	/**
	 * 查询事件详情--反馈进程所需数据：用户负责的执行中事件列表
	 * @param cusId
	 * @return
	 */
	public List<Event> getEventListForProgress(int cusId){
		List<Event> events=QueryObject.select("E.*",Event.class).distinct(true)
				.from("EVENT E LEFT JOIN EVENT_CUS_MAPPING M ON M.EVENT_ID=E.EVENT_ID")
				.distinct(true)
				.cond("E.EVENT_STATE").notEqu(EventState.FINISH)
				.cond("E.EVENT_STATE").notEqu(EventState.SUBMITEND)//2017年4月25日15:53:52  增加提交完结后不可见
				.cond("E.HIGHER_EVENT_ID").isNull()
				.condition("(M.CUS_ID=? "
						+ "AND M.EVENT_ROLE=?) OR "
						+ "(SELECT COUNT(1) FROM EVENT EE"
						+ " LEFT JOIN EVENT_CUS_MAPPING MM ON MM.EVENT_ID=EE.EVENT_ID"
						+ " WHERE EE.HIGHER_EVENT_ID=E.EVENT_ID "
						+ " AND MM.CUS_ID=? "
						+ " AND MM.EVENT_ROLE=?"
						+ " AND EE.EVENT_STATE!=?"
						+ " AND EE.EVENT_STATE!=?)>0", 
						new Object[]{cusId,EventRole.MANAGER,
								cusId,EventRole.MANAGER,
								EventState.FINISH,
								EventState.SUBMITEND})
				.asc("E.EVENT_LAST_CHANGE_DATE")
				.list(dao);
		return events;
	}

	
	/**
	 * 查询事件详情--反馈进程所需数据：用户负责的执行中节点
	 * @param cusId
	 * @return
	 */
	public List<Event> getNodeListForProgress(int cusId,String eventId){
		List<Event> nodes=null;
		if(StringUtils.isNotBlank(eventId)){
			EventCusMapping mapping=QueryObject.select(EventCusMapping.class)
					.cond("EVENT_ID").equ(eventId)
					.cond("CUS_ID").equ(cusId)
					.cond("EVENT_ROLE").equ(EventRole.MANAGER).firstResult(dao);
			if(mapping!=null){//判断是事件验收人还是节点验收人
				nodes=QueryObject.select(Event.class)
						.cond("HIGHER_EVENT_ID").equ(eventId)
						.cond("EVENT_TYPE").notEqu(EventType.CHECK)
						.cond("EVENT_TYPE").notEqu(EventType.RECORD)
						.cond("EVENT_STATE").notEqu(EventState.FINISH)
						.cond("EVENT_STATE").notEqu(EventState.SUBMITEND)
						.asc("EVENT_CREATE_DATE")
						.list(dao);
				//当前事件的完结节点就是当前事件
				Event thisEventFinishNode=QueryObject.select(Event.class)
						.cond("EVENT_ID").equ(eventId).firstResult(dao);
				thisEventFinishNode.setEventName("事件完结");
				if(!thisEventFinishNode.getEventState().equals(EventState.SUBMITEND)){
					nodes.add(thisEventFinishNode);
				}
				
			}else{
				nodes=QueryObject.select("E.*,E.EVENT_CREATE_DATE",Event.class)
						.from("EVENT E LEFT JOIN EVENT_CUS_MAPPING M ON M.EVENT_ID=E.EVENT_ID")
						.cond("E.HIGHER_EVENT_ID").equ(eventId)
						.cond("M.CUS_ID").equ(cusId)
						.cond("M.EVENT_ROLE").equ(EventRole.MANAGER)
						.cond("E.EVENT_TYPE").equ(EventType.NORMAL)
						.cond("EVENT_STATE").notEqu(EventState.SUBMITEND)
						.cond("E.EVENT_STATE").notEqu(EventState.FINISH)
						.asc("E.EVENT_CREATE_DATE")
						.list(dao);
			}
		}
		if(CheckTool.checkListIsNotNull(nodes)){
			for (Event event : nodes) {
				event.setEventRole(EventRole.MANAGER);
			}
		}
		return nodes;
	}
	/**
	 * 查询事件所有成员
	 * @param eventId  事件id
	 * @return
	 */
	public List<Customer> getAllMember(String eventId){
		return QueryObject.select("C.*",Customer.class).distinct(true)
				.from("CUSTOMER C LEFT JOIN EVENT_CUS_MAPPING M ON M.CUS_ID=C.CUS_ID"
						+ " LEFT JOIN EVENT E ON E.EVENT_ID=M.EVENT_ID")
					.cond("E.EVENT_ID").equ(eventId).or(true)
					.cond("E.HIGHER_EVENT_ID").equ(eventId)
					.list(dao);
	}
}
