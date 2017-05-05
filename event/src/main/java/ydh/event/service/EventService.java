package ydh.event.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ydh.cicada.dao.JdbcDao;
import ydh.cicada.dao.Page;
import ydh.cicada.dict.YesNo;
import ydh.cicada.query.Deleter;
import ydh.cicada.query.QueryObject;
import ydh.cicada.query.api.Query;
import ydh.customer.dict.CustomerState;
import ydh.customer.entity.Customer;
import ydh.customer.service.CustomerService;
import ydh.event.dict.*;
import ydh.event.entity.*;
import ydh.event.query.EventQuery;
import ydh.message.entity.CusMessageMapping;
import ydh.message.entity.MessageType;
import ydh.message.entity.PushMessage;
import ydh.mvc.BaseResult;
import ydh.mvc.ex.WorkException;
import ydh.push.entity.PushSetting;
import ydh.sms.utils.DateUtil;
import ydh.upload.entity.QuoteRelation;
import ydh.upload.entity.SourceType;
import ydh.upload.service.UploadFileService;
import ydh.utils.*;
import ydh.weixin.entity.WeiXinConstant;
import ydh.weixin.service.LinkServiceImpl;
import ydh.weixin.template.CreatTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class EventService {

	@Autowired
	private JdbcDao dao;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private UploadFileService uploadFileService;
	/**
	 * 通过用户ID和事件ID查询用户事件关系映射
	 * @param cusId
	 * @param eventId
	 * @return
	 */
	@Transactional
	public List<EventCusMapping> getEventCusMappingByCusIdAndEventId(Integer cusId, String eventId){
		return QueryObject.select("M.*",EventCusMapping.class)
							.from("EVENT_CUS_MAPPING M LEFT JOIN EVENT E ON M.EVENT_ID=E.EVENT_ID")
							.cond("M.CUS_ID").equ(cusId)
							.cond("M.MAIN_EVENT_IDS").like(eventId)
							.asc("E.MAIN_EVENT_IDS")
							.list(dao);
	}
	
	/**
	 * 通过用户ID和事件ID查询用户事件标签
	 * @param cusId
	 * @param eventId
	 * @return
	 */
	@Transactional
	public EventCusMapping getEventTagByCusIdAndEventId(Integer cusId, String eventId){
		return QueryObject.select(EventCusMapping.class)
									.cond("CUS_ID").equ(cusId)
									.cond("EVENT_ID").equ(eventId)
									.firstResult(dao);
	}

	/**
	 * 通过用户ID和事件ID查询创始/责任人
	 * @param cusId
	 * @param eventId
	 * @return
	 */
	@Transactional
	public List<EventCusMapping> getEventUpdaterByCusIdAndEventId(Integer cusId, String eventId){
		return QueryObject.select(EventCusMapping.class)
							.cond("CUS_ID").equ(cusId)
							.cond("EVENT_ID").equ(eventId)
							.cond("EVENT_ROLE").in(EventRole.CREATER,EventRole.MANAGER,EventRole.CHECKER)
							.list(dao);
	}
	
	/**
	 * 查询用户参与的执行中事件
	 * @param cusId
	 * @return
	 */
	@Transactional
	public List<Event> getEventsByCusId(Integer cusId){
		return QueryObject.select("*", Event.class)
							.from("EVENT_CUS_MAPPING M LEFT JOIN EVENT E ON M.EVENT_ID=E.EVENT_ID")
							.cond("M.CUS_ID").equ(cusId)
							.cond("E.EVENT_STATE").in(EventState.RUNNING)
							.groupBy("M.EVENT_ID")
							.list(dao);
	}
	
	/**
	 * 通过事件ID获取事件
	 * @param eventId
	 * @return
	 */
	@Transactional
	public Event getEventByEventId(String eventId){
		return QueryObject.select(Event.class)
							.cond("M.EVENT_ID").equ(eventId)
							.firstResult(dao);
	}
	
	/**
	 * 查询用户参与的执行中节点
	 * @param cusId
	 * @param eventId
	 * @return
	 */
	@Transactional
	public List<Event> getNodesByCusIdAndEventId(Integer cusId, String eventId){
		return QueryObject.select("*", Event.class)
				.from("EVENT_CUS_MAPPING M LEFT JOIN EVENT E ON M.EVENT_ID=E.EVENT_ID")
				.cond("M.CUS_ID").equ(cusId)
				.cond("E.HIGHER_EVENT_ID").equ(eventId)
				.cond("E.EVENT_STATE").in(EventState.RUNNING)
				.groupBy("M.EVENT_ID")
				.list(dao);
	}
	
	/**
	 * 通过主事件ID查询最后一个子事件
	 * @param eventId
	 * @return
	 */
	@Transactional
	public Event getLastEvent(String eventId){
		return QueryObject.select(Event.class)
				.cond("HIGHER_EVENT_ID").equ(eventId)
				.desc("EVENT_NUM DESC")
				.firstResult(dao);
	}
	/**
	 * 通过用户ID和事件ID,身份查询人员信息
	 * @param cusId
	 * @param eventId
	 * @return
	 */
	@Transactional
	public EventCusMapping getEventPersonByCusIdAndEventId(Integer cusId, String eventId, EventRole role){
		if(role == null){
			return QueryObject.select(EventCusMapping.class)
					.cond("CUS_ID").equ(cusId)
					.cond("EVENT_ID").equ(eventId)
					.firstResult(dao);
		}
		return QueryObject.select(EventCusMapping.class)
					.cond("CUS_ID").equ(cusId)
					.cond("EVENT_ID").equ(eventId)
					.cond("EVENT_ROLE").equ(role)
					.firstResult(dao);
	}
	
	/**
	 * 选取事件某种身份的全部用户
	 * @param eventId
	 * @param role
	 * @return
	 */
	@Transactional
	public List<EventCusMapping> getEventPersonByEventIdAndRole(String eventId, EventRole role){
			return QueryObject.select(EventCusMapping.class)
					.cond("EVENT_ID").equ(eventId)
					.cond("EVENT_ROLE").equ(role)
					.list(dao);
	}
	/**
	 * 查询事件相关人员列表
	 * @param eventId
	 * @param role
	 * @return
	 */
	@Transactional
	public List<EventCusMapping> getEventPersonListByEventId(String eventId, EventRole role){
		return QueryObject.select("M.*,C.*",EventCusMapping.class)
							.from("EVENT_CUS_MAPPING M LEFT JOIN CUSTOMER C ON C.CUS_ID = M.CUS_ID")
							.cond("M.EVENT_ID").equ(eventId)
							.cond("M.EVENT_ROLE").equ(role)
							.list(dao);
	}
	/**
	 * 查询事件相关人员
	 * @param eventId
	 * @param role
	 * @return
	 */
	@Transactional
	public EventCusMapping getEventPersonByEventId(String eventId, EventRole role){
		return QueryObject.select("M.*,C.*",EventCusMapping.class)
							.from("EVENT_CUS_MAPPING M LEFT JOIN CUSTOMER C ON C.CUS_ID = M.CUS_ID")
							.cond("M.EVENT_ID").equ(eventId)
							.cond("M.EVENT_ROLE").equ(role)
							.firstResult(dao);
	}
	/**
	 * 保存事件明细
	 * @param nodes
	 * @throws Exception
	 */
	@Transactional
	public Event dealSave(List<Event> events, int cusId)throws Exception{
		Event mainEvent=null;
		if(CheckTool.checkListIsNotNull(events)){
			try {
				for (Event event : events) {
					if(StringUtils.isBlank(event.getHigherEventId())){
						mainEvent=event;
					}
					this.addEvent(event,cusId);
				}
				sendInstationMessage(mainEvent, cusId);
			} catch (Exception e) {
				e.printStackTrace();
				mainEvent=null;
			}
		}
		return mainEvent;
	}
	/**
	 * 添加事件
	 * @param event
	 * @param nodes
	 * @param eventCusMappings
	 * @param nodeCusMappings
	 */
	@Transactional
	public Event addEvent(Event event,int cusId){
		try {
			if(event.getEventCreateDate()==null){//新增创建
				Date nowDate=new Date();
				event.setEventCreateDate(nowDate);
				//event.setEventId(TransSerialNumTool.createSerialNum());
				//判断是节点还是事件，事件创建完成状态为进行中，节点创建完成状态为未开始
				if(StringUtils.isNotBlank(event.getHigherEventId())){					
					event.setEventLastChangeDate(nowDate);
					event.setEventState(EventState.NOSTART);
				}else{
					event.setEventLastChangeDate(nowDate);
					createEventStartNode(event,cusId);//2017年4月19日18:09:26  给事件默认责任人与创建人
					event.setEventStartDate(nowDate);
					event.setEventState(EventState.RUNNING);
				}
				//待验收
				event.setEventType(EventType.NORMAL);
				event.setInspectedState(InspectedState.CHECKING);
				event.setCommentCount(0);
				event.setEventLastChangeDate(nowDate);
				
				//保存事件附件
				List<QuoteRelation> relations = event.getQuoteRelations();
				//changeFilePath(files);
				if(CheckTool.checkListIsNotNull(relations)){
					for (QuoteRelation relation : relations) {
						relation.setSourceId(event.getEventId());
						relation.setSourceType(SourceType.EVENT);
						if(StringUtils.isBlank(relation.getQuoteRelationId())){
							relation.setQuoteRelationId(UUIDTool.getUUID());
							this.dao.insert(relation);
						}else{
							this.dao.update(relation);
						}
					}
					uploadFileService.deleteLoseSource();
				}
				//保存事件
				this.dao.insert(event);
				//保存事件标签关系
				List<EventTag> tagList=event.getEventTags();
				if(CheckTool.checkListIsNotNull(tagList)){
					for (EventTag eventTag : tagList) {
						EventTagMapping etm=new EventTagMapping();
						etm.setChangeDate(nowDate);
						etm.setEventId(event.getEventId());
						etm.setEventTagId(eventTag.getEventTagId());
						etm.setEventTagMappingId(UUIDTool.getUUID());
						this.dao.insert(etm);
					}
				}else{
					//添加与“未分类”标签的关联
					EventTagMapping etm2=new EventTagMapping();
					etm2.setChangeDate(nowDate);
					etm2.setEventId(event.getEventId());
					etm2.setEventTagId("1");//TODO 待修改，id=1的标签为“未分类”标签
					etm2.setEventTagMappingId(UUIDTool.getUUID());
					this.dao.insert(etm2);
				}
				//添加与“全部”标签的关联
				EventTagMapping etm1=new EventTagMapping();
				etm1.setChangeDate(nowDate);
				etm1.setEventId(event.getEventId());
				etm1.setEventTagId("0");//TODO 待修改，id=0的标签为“全部”标签
				etm1.setEventTagMappingId(UUIDTool.getUUID());
				this.dao.insert(etm1);
				//保存事件用户关系
				List<EventCusMapping> eventCusMappings = event.getEventCusMappings();
				if(CheckTool.checkListIsNotNull(eventCusMappings)){					
					for (EventCusMapping mapping : eventCusMappings) {
						mapping.setEventCusMappingId(TransSerialNumTool.createSerialNum());
						mapping.setChangeDate(nowDate);
						mapping.setEventId(event.getEventId());
						if(mapping.getCusId()==cusId){
							mapping.setChangeFlag(YesNo.NO);
						}else{
							mapping.setChangeFlag(YesNo.YES);//TODO 在点开事件的时候改变状态
						}
						mapping.setCommentFlag(YesNo.NO);
						this.dao.insert(mapping);
					}
				}
			} else {//修改已保存的事件
				
			}
		} catch (Exception e) {
			throw e;
		}
		return event;
	}
	
//	/**
//	 * TODO 修改事件
//	 * 
//	 * @param event
//	 * @return
//	 */
//	private Event editEvent(Event event){
//		
//		
//		
//		return null;
//	}
	
	
	/**
	 * 删除事件
	 * @param eventId
	 */
	@Transactional
	public void delEvent(String eventId){
		List<Event> events = QueryObject.select(Event.class)
										.cond("HIGHER_EVENT_ID").equ(eventId)
										.list(dao);
		for (Event event : events) {
			delEvent(event.getEventId());
		}
		Deleter.delete(EventCusMapping.class).cond("EVENT_ID").equ(eventId).exec(dao);//删除人员
		Deleter.delete(EventChangeCondition.class).cond("EVENT_ID").equ(eventId).exec(dao);//删除条件
		//删除文件
		this.dao.delete(this.dao.load(Event.class, eventId));					
	}
	
	/**
	 * 用户是否可改事件
	 * @param eventId
	 * @param cusId
	 * @return
	 */
	@Transactional
	public boolean couldManageEvent(String eventId, Integer cusId){
		Event event = this.dao.load(Event.class, eventId);
		if(event.getHigherEventId() == null || event.getHigherEventId().length() == 0){//主事件
			List<EventCusMapping> mappings = this.getEventUpdaterByCusIdAndEventId(cusId, eventId);
			if(mappings==null || mappings.size() == 0){//非管理员
				return false;
			}
		} else {//子事件
			List<EventCusMapping> mappings = this.getEventUpdaterByCusIdAndEventId(cusId, eventId);
			if(mappings==null || mappings.size() == 0){//非管理员
				 return couldManageEvent(event.getHigherEventId(), cusId);
			}
		}
		return true;
	}

	/**
	 * 处理选择人员
	 * @param eventId
	 * @param eventRole
	 * @param cusInfo
	 * @return
	 */
	@Transactional
	public void dealChosePeople(String eventId, EventRole eventRole, String cusInfo){
		List<Customer> customers = new  ArrayList<Customer>();
		List<EventCusMapping> eventCusMappings = new  ArrayList<EventCusMapping>();
		if(cusInfo != null && cusInfo.length() > 0){
			String[] info = cusInfo.split(",");
			for (int i = 0; i < info.length; i++) {
				if(info[i] != null && info[i].length() > 0){
					Customer customer = this.customerService.queryCustomerByPhone(info[i]);
					if(customer != null){
						customers.add(customer);
					} else {
						customer = new Customer();
						customer.setCusState(CustomerState.TEMP);
						customer.setCusPhone(info[i]);
						customer.setCusLogName(info[i+1]);
						customer.setCusHeadImg("0.jpg");
						this.dao.insert(customer);
						customer = this.customerService.queryCustomerByPhone(info[i]);
					}
					EventCusMapping mapping = QueryObject.select(EventCusMapping.class)
														.cond("EVENT_ID").equ(eventId)
														.cond("EVENT_ROLE").equ(eventRole)
														.cond("CUS_ID").equ(info[i])
														.firstResult(dao);
					if(mapping == null){
						mapping = new EventCusMapping();
						mapping.setCusId(customer.getCusId());
						mapping.setEventId(eventId);
						mapping.setEventRole(eventRole);
					}
					eventCusMappings.add(mapping);
				}
				i++;
			}
		}
		if(eventRole != null){
			Deleter.delete(EventCusMapping.class)
					.cond("EVENT_ID").equ(eventId)
					.cond("EVENT_ROLE").equ(eventRole)
					.exec(dao);
		}
		for (EventCusMapping mapping : eventCusMappings) {
			this.dao.insert(mapping);
		}
	}
	/**
	 * 保存评论/验收
	 * @param eventComment
	 */
	@Transactional
	public EventComment saveEventComment(EventComment eventComment,Event event){
		List<QuoteRelation> relations=eventComment.getQuoteRelations();
		eventComment.setEventCommentId(UUIDTool.getUUID());
		if(CheckTool.checkListIsNotNull(relations)){
			for (QuoteRelation quoteRelation : relations) {
				quoteRelation.setSourceId(eventComment.getEventCommentId());
				quoteRelation.setSourceType(SourceType.COMMENT);
				quoteRelation.setQuoteDate(new Date());
				if(StringUtils.isBlank(quoteRelation.getQuoteRelationId())){
					quoteRelation.setQuoteRelationId(UUIDTool.getUUID());
					this.dao.insert(quoteRelation);
				}else{
					this.dao.update(quoteRelation);
				}
			}
			uploadFileService.deleteLoseSource();
		}
		eventComment.setLocationAndCode(GsonUtil.toJSONString(eventComment.getLocation()));
		eventComment.setCommentDate(new Date());
		this.dao.insert(eventComment);
		//评论数修改
		String countSql="select count(1) from event_comment where event_id=?";
		int	count=this.dao.getJdbcTemplate().queryForObject(countSql, new Object[]{eventComment.getEventId()},Integer.class).intValue();
		event.setCommentCount(count);//(nodeOrEvent.getCommentCount()==null?1:nodeOrEvent.getCommentCount()+1);
		this.dao.update(event);
		return eventComment;
	} 

	/**
	 * 获取节点汇报/验收评论
	 * @param eventId
	 * @return
	 * @throws WorkException 
	 */
	@Transactional
	public List<EventComment> getEventComment(int cusId,String eventId) throws WorkException {
		List<EventComment> comments=null;
		if(cusId!=0){
			comments=QueryObject.select(EventComment.class)
					.cond("EVENT_ID").equ(eventId)
					.cond("CUS_ID").equ(cusId)
					.asc("COMMENT_DATE")
					.list(dao);
			
		}else{
			comments=QueryObject.select(EventComment.class)
					.cond("EVENT_ID").equ(eventId)
					.asc("COMMENT_DATE")
					.list(dao);
		}
		if(CheckTool.checkListIsNotNull(comments)){
			for (EventComment eventComment : comments) {
				eventComment.setQuoteRelations(QueryObject.select(QuoteRelation.class)
						.cond("SOURCE_ID").equ(eventComment.getEventCommentId()).list(dao));
				Customer customer=QueryObject.select(Customer.class)
						.cond("CUS_ID").equ(eventComment.getCusId())
						.firstResult(dao);
				eventComment.setCusHeadImg(customer.getCusHeadImg());//放入图片
				eventComment.setCusLogName(customer.getCusLogName());//放入姓名
			}
		}
		
		return comments;
	}

	/**
	 * 改变节点状态
	 * @param eventState
	 * @param eventId
	 */
	@Transactional
	public void updateNodeState(EventState eventState, String eventId){
		Event mainEvent = this.dao.load(Event.class, eventId);
		mainEvent.setEventState(eventState);
		if(eventState == EventState.FINISH){
			mainEvent.setEventFinishDate(new Date());
		}
		mainEvent.setEventLastChangeDate(new Date());
		this.dao.update(mainEvent);
		List<EventChangeCondition> conditions = QueryObject.select(EventChangeCondition.class)
																	.cond("CONDITION_EVENT_STATE").equ(eventState)
																	.cond("CONDITION_EVENT_ID").equ(eventId)
																	.list(dao);
		for (EventChangeCondition condition : conditions) {
			if(condition.getConditionAnd() == YesNo.NO){
				Event event= this.dao.load(Event.class, condition.getEventId());
				if(event.getEventState() != EventState.FINISH){
					event.setEventState(condition.getEventState());
					this.dao.update(event);
				}
				updateNodeState(condition.getEventState(), condition.getEventId());
			} else {
				List<EventChangeCondition> changes = QueryObject.select(EventChangeCondition.class)
																.cond("EVENT_STATE").equ(condition.getEventState())
																.cond("EVENT_ID").equ(condition.getEventId())
																.cond("AND").equ(YesNo.YES)
																.list(dao);
				boolean flag=true;
				for (EventChangeCondition eventChangeCondition : changes) {
					Event event= this.dao.load(Event.class, eventChangeCondition.getConditionEventId());
					if(event.getEventState() == eventChangeCondition.getConditionEventState()){
						continue;
					} else {
						flag = false;
						break;
					}
				}
				if(flag){
					updateNodeState(condition.getEventState(), condition.getEventId());
				}
			}
		}
	}
	/**
	 * 查询可引用事件
	 * @param cusId
	 * @return
	 */
	public List<Event> getQuoteEvent(int cusId,String eventId){
		Event event=QueryObject.select(Event.class)
				.cond("EVENT_ID").equ(eventId).firstResult(dao);
		if(event!=null){
			if(!StringUtils.isBlank(event.getHigherEventId())){
				event=QueryObject.select(Event.class)
						.cond("EVENT_ID").equ(event.getHigherEventId()).firstResult(dao);
			}
			return QueryObject.select("DISTINCT E.*",Event.class)
					.from("EVENT E LEFT JOIN EVENT_CUS_MAPPING C ON E.EVENT_ID=C.EVENT_ID")
					.cond("C.CUS_ID").equ(cusId)
					.cond("E.HIGHER_EVENT_ID").isNull()
					.cond("E.EVENT_ID").notEqu(event.getEventId())
					.cond("E.TEMPLATE_DATA").equ(YesNo.NO).list(dao);
		}
		return QueryObject.select("DISTINCT E.*",Event.class)
				.from("EVENT E LEFT JOIN EVENT_CUS_MAPPING C ON E.EVENT_ID=C.EVENT_ID")
				.cond("C.CUS_ID").equ(cusId)
				.cond("E.HIGHER_EVENT_ID").isNull()
				.cond("E.TEMPLATE_DATA").equ(YesNo.NO).list(dao);
		
	}
	/**
	 * 创建事件开始节点
	 * @param event
	 * @return
	 */
	public Event createEventStartNode(Event event,int cusId){
		Event node=new Event();
		node.setEventName("创建事件");
		node.setEventId(TransSerialNumTool.createSerialNum());
		Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, -30);//取得事件创建时间前30秒
		Date t=calendar.getTime();
		node.setEventRealFinishDate(t);
		node.setEventCreateDate(t);
		node.setEventLastChangeDate(t);
		node.setEventType(EventType.RECORD);
		node.setEventState(EventState.FINISH);
		node.setHigherEventId(event.getEventId());
		this.dao.insert(node);
		addCreaterManagerChecker(node.getEventId(),cusId);
		return node;
	}
	/**
	 * 2017年4月19日18:10:07
	 * 给事件与节点创建默认的创建人，责任人，验收人
	 */
	private void addCreaterManagerChecker(String eventId,int cusId){
		EventCusMapping cus=new EventCusMapping();
		Date createDate=new Date();
		cus.setEventId(eventId);
		cus.setCusId(cusId);
		cus.setEventCusMappingId(UUIDTool.getUUID());
		cus.setChangeDate(createDate);
		cus.setEventRole(EventRole.CHECKER);
		this.dao.insert(cus);
		cus.setEventCusMappingId(UUIDTool.getUUID());
		cus.setEventRole(EventRole.CREATER);
		this.dao.insert(cus);
		cus.setEventCusMappingId(UUIDTool.getUUID());
		cus.setEventRole(EventRole.MANAGER);
		this.dao.insert(cus);
	}
	/**
	 * 创建改变记录开始节点
	 * @param event
	 * @return
	 */
	public Event createChangeRecodingNode(Event event,int cusId){
		Customer cus=this.dao.load(Customer.class,cusId);
		Event node=new Event();
		if(StringUtils.isNotBlank(event.getEventId())){
			node.setEventName(cus.getCusLogName()+"修改了节点["+event.getEventName()+"]");
			node.setHigherEventId(event.getHigherEventId());
		}else{
			node.setEventName(cus.getCusLogName()+"修改了事件["+event.getEventName()+"]");
			node.setHigherEventId(event.getEventId());
		}
		node.setEventId(TransSerialNumTool.createSerialNum());
		Date t=new Date();
		node.setEventRealFinishDate(t);
		node.setEventCreateDate(t);
		node.setEventLastChangeDate(t);
		node.setEventType(EventType.RECORD);
		node.setEventState(EventState.FINISH);
		this.dao.insert(node);
		addCreaterManagerChecker(node.getEventId(),cusId);
		return node;
	}
	/**
	 * 创建节点验收节点
	 */
	public Event createNodeSuccessNode(Event event,int cusId){
		if(StringUtils.isBlank(event.getHigherEventId())){
			return event;
		}
		Customer cus=this.dao.load(Customer.class,cusId);
		Event node=new Event();
		node.setEventName(cus.getCusLogName()+"验收了节点["+event.getEventName()+"]");
		node.setEventId(TransSerialNumTool.createSerialNum());
		Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 5);//取得事件完成时间后5秒
		Date t=calendar.getTime();
		node.setEventRealFinishDate(t);
		node.setEventCreateDate(t);
		node.setEventLastChangeDate(t);
		node.setEventType(EventType.CHECK);
		node.setEventState(EventState.FINISH);
		node.setHigherEventId(event.getEventId());
		this.dao.insert(node);
		addCreaterManagerChecker(node.getEventId(),cusId);
		return node;
	}
	
	/**
	 * 事件分页查询
	 * @param eQuery
	 * @return
	 */
	public Page<Event> queryPage(EventQuery eQuery){
		Query query = eQuery.getClass().getAnnotation(Query.class);
		String whereSql="(M.CUS_ID=?)or(SELECT COUNT(1) FROM EVENT EE LEFT JOIN EVENT_CUS_MAPPING MM ON MM.EVENT_ID=EE.EVENT_ID"
				+" WHERE EE.HIGHER_EVENT_ID=E.EVENT_ID  AND MM.CUS_ID=? )>0";
		QueryObject<Event> eventQ =QueryObject.select(query.select(),Event.class).from(query.from()).distinct(true);
		if(eQuery.getP()==null){
			eQuery.setP(1);
		}
		eventQ.page(eQuery.getP());
		eventQ.pageSize(eQuery.getS());
		eventQ.cond("E.HIGHER_EVENT_ID").isNull();
		if(eQuery.getEventTagName()!=null){
			eventQ.cond("T.EVENT_TAG_NAME").equ(eQuery.getEventTagName());
		}
		if(eQuery.getEventState()!=null){
			eventQ.cond("E.EVENT_STATE").equ(eQuery.getEventState());
		}
		Object[] param=new Object[]{eQuery.getCusId(),eQuery.getCusId()};;
		if(eQuery.getEventRole()!=null){
			param=new Object[]{eQuery.getCusId(),eQuery.getEventRole(),
					eQuery.getCusId(),eQuery.getEventRole()};
			whereSql="(M.CUS_ID=? AND M.EVENT_ROLE=? "
					+" )or(SELECT COUNT(1) FROM EVENT EE"
					+" LEFT JOIN EVENT_CUS_MAPPING MM ON MM.EVENT_ID=EE.EVENT_ID"
					+" WHERE EE.HIGHER_EVENT_ID=E.EVENT_ID "
					+" AND MM.CUS_ID=? AND MM.EVENT_ROLE=?"
					+ ")>0";
		}
		eventQ.condition(whereSql, param);
		if(eQuery.getSortCondition()!=null && eQuery.getSortCondition().equals(SortCondition.LONGTIME)){
			eventQ.asc("TIMESTAMPDIFF(SECOND,E.EVENT_CREATE_DATE,NOW()),E.EVENT_LAST_CHANGE_DATE");
		}else{
			eventQ.desc("E.EVENT_LAST_CHANGE_DATE");
		}
		return this.dao.find(eventQ);
	}
	
	/**
	 * 处理分页查询列表数据
	 * @param page
	 * @param cusId
	 * @return
	 */
	public Page<Event> formatList(Page<Event> page,int cusId){
		List<Event> events=page.getData();
		for (Event event : events) {
			try {
				String manageNameSql="select c.cus_Log_Name from customer c"
						+ " left join event_cus_mapping m on m.cus_id=c.cus_id"
						+ " where m.event_id=? and m.event_role=? limit 1";
				String manageName=this.dao.getJdbcTemplate().queryForObject(manageNameSql,new Object[]{event.getEventId(),EventRole.MANAGER},String.class);
				event.setManagerName(manageName);
				String changeFlagSql="select m.change_Flag from event_cus_mapping m"
						+ " left join event e on e.event_id=m.event_id"
						+ " where (e.event_id=? or e.HIGHER_EVENT_ID=?) and m.cus_id=? limit 1";//TODO EXCEPTION
				YesNo changeFlag=YesNo.values()[(this.dao.getJdbcTemplate().queryForObject(changeFlagSql,new Object[]{event.getEventId(),event.getEventId(),cusId},Integer.class))];
				event.setChangeFlag(changeFlag);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("该角色不在该事件中");
			}
			
		}
		return page;
	}
	/**
	 * 	 * 更新事件状态为“有更新记录”
	 * @param cusId 不更新的用户id
	 * @param eventId 事件或节点id
	 * @param higherEventId 节点父id
	 * @param changeFlag 需要改变的状态
	 */
	public void changeFlagReflush(int cusId,String eventId,String higherEventId){
		String sql="update event_cus_mapping set change_flag=?"
				+ " where event_id=? and cus_id!=?";
		this.dao.getJdbcTemplate().update(sql,new Object[]{1,eventId,cusId});
		if(StringUtils.isNotBlank(higherEventId)){
			Event fatherEvent=this.dao.load(Event.class, higherEventId);
			fatherEvent.setEventLastChangeDate(new Date());
			this.dao.update(fatherEvent);
			this.dao.getJdbcTemplate().update(sql,new Object[]{1,higherEventId,cusId});
		}
	}
	/**
	 * 查询事件是否有设置默认完结条件
	 * 若无：则直接默认当事件改变为验收通过时事件也就变为完结。
	 * 若有：则不做任何操作，让事件条件判断机制自行判断
	 * @param mainEventId
	 * @return
	 */
	public int countDefaultConditionForEvent(String mainEventId){
		String countSql="select count(1) from event_change_condition"
				+ " where event_id=? and event_state=?";
		return this.dao.getJdbcTemplate().queryForObject(countSql, new Object[]{mainEventId,EventState.FINISH},Integer.class).intValue();
	}
	
	/**
	 * 更新事件状态为“已读”
	 * @param cusId 更新的用户id
	 * @param eventId
	 */
	public void changeFlagReflushToNo(int cusId,String eventId,String higherEventId){
		String sql="update event_cus_mapping set change_flag=?"
				+ " where event_id=? and cus_id=?";
		this.dao.getJdbcTemplate().update(sql,new Object[]{YesNo.NO.ordinal(),eventId,cusId});
		if(StringUtils.isNotBlank(higherEventId)){
			this.dao.getJdbcTemplate().update(sql,new Object[]{YesNo.NO.ordinal(),higherEventId,cusId});
		}
	}
	/**
	 * 信息推送
	 * @param event
	 * @param cusId
	 */
	public void sendInstationMessage(Event event,int cusId){
		Event mainEvent=event;//2017年4月27日14:15:11  推送需要主事件id，修改
		if(StringUtils.isNotBlank(event.getHigherEventId())){
			mainEvent=this.dao.load(Event.class, event.getHigherEventId());
		}
		List<Customer> cList=getNeedSendMessageCustomer(event,cusId);
		Customer cus=this.dao.load(Customer.class, cusId);
		if(CheckTool.checkListIsNotNull(cList)){
			LinkServiceImpl linkService=new LinkServiceImpl();
			List<String> aliasIds=new ArrayList<String>();
			Date now =new Date();
			PushMessage message=new PushMessage();
			message.setConditionId(mainEvent.getEventId());
			message.setContent("您参与的'"+mainEvent.getEventName()+"'事件有更新，快打开app查看吧！");
			message.setMessageType(MessageType.EVENT_MESSAGE);
			message.setPushDate(now);
			message.setPushMessageId(UUIDTool.getUUID());
			message.setTitle("事件更新");
			this.dao.insert(message);
			for (Customer customer : cList) {	
				//TODO 检查是否为空
				PushSetting setting=QueryObject.select(PushSetting.class).cond("CUS_ID").equ(cusId).firstResult(dao);
				if(setting==null) setting=new PushSetting();
				if(setting.getInstation().equals(YesNo.YES) && StringUtils.isNotBlank(customer.getPushAlias())){
					aliasIds.add(customer.getPushAlias());
					CusMessageMapping mapping=new CusMessageMapping();
					mapping.setCusMessageMappingId(UUIDTool.getUUID());
					mapping.setCus_Id(customer.getCusId());
					mapping.setMessageId(message.getPushMessageId());
					mapping.setReadFlag(YesNo.YES);
					this.dao.insert(mapping);
				}
//				if(setting.getSms().equals(YesNo.YES) && StringUtils.isNotBlank(customer.getCusPhone())){
//					SmsTool.sendSms(customer.getCusPhone(),SmsTool.EVENT_CHANGE_TEMPLATE, "name", "["+cus.getCusLogName()+"]","eventName","["+mainEvent.getEventName()+"]");				
//				}
				if(setting.getWechat().equals(YesNo.YES) && StringUtils.isNotBlank(customer.getWxOpenId())){
					String[] temData = {WeiXinConstant.CREATE_NODE_FIRST, cus.getCusLogName(), mainEvent.getEventName(), mainEvent.getEventState().title(),mainEvent.getEventDetail(), DateUtil.dateToStr(now)};
					linkService.SendTemplateMessage(WeiXinConstant.Event_CHANGE_Template, customer.getWxOpenId(), "", CreatTemplate.loadingFourKeyword(temData));
				}
				if(setting.getEmail().equals(YesNo.YES) && StringUtils.isNotBlank(customer.getEmailAddress())){
					//TODO 发送邮件推送
				}
			}
			JPushTool.pushAliasNoticeGzb(message.getTitle(), message.getContent(), message.getConditionId(), message.getUrl(), aliasIds);
		}
	}
	/**
	 * 查询需要发送消息的用户
	 * @param event
	 * @return
	 */
	private List<Customer> getNeedSendMessageCustomer(Event event,int cusId){
		List<Customer> cList=null;
		if(StringUtils.isNotBlank(event.getHigherEventId())){
			cList=QueryObject.select("C.*",Customer.class).distinct(true)
					.from("CUSTOMER C LEFT JOIN EVENT_CUS_MAPPING M ON M.CUS_ID=C.CUS_ID")
					.condition("M.EVENT_ID=? OR M.EVENT_ID=?", new Object[]{event.getHigherEventId(),event.getEventId()})
					.cond("C.CUS_ID").notEqu(cusId)
					.list(dao);
		}else{
			cList=QueryObject.select("C.*",Customer.class).distinct(true)
			.from("CUSTOMER C LEFT JOIN EVENT_CUS_MAPPING M ON M.CUS_ID=C.CUS_ID"
				+ " LEFT JOIN EVENT E ON E.EVENT_ID=M.EVENT_ID")
			.condition("E.EVENT_ID=? OR E.HIGHER_EVENT_ID=?", new Object[]{event.getEventId(),event.getEventId()})
			.cond("C.CUS_ID").notEqu(cusId)
			.list(dao);
		}
		return cList;
	}
	/**
	 * 搜索事件
	 * @param eventKey	根据事件id与名称查询事件
	 * @return 事件列表
	 */
	@SuppressWarnings("rawtypes")
	public BaseResult searchEvents(int cusId,String eventKey){
		BaseResult<List<Event>> result=new BaseResult<List<Event>>();
		List<Event> events=QueryObject.select("E.*,M.CHANGE_FLAG AS changeFlag,"
				+ "(select cc.cus_Log_Name from customer cc"
							+ " left join event_cus_mapping mc on mc.cus_id=cc.cus_id"
							+ " where mc.event_id=E.EVENT_ID and mc.event_role=? ) AS managerName"
				,Event.class)
				.distinct(true)
				.from("EVENT E LEFT JOIN EVENT_CUS_MAPPING M ON E.EVENT_ID=M.EVENT_ID")
				.cond("M.CUS_ID").equ(cusId)
				.cond("E.HIGHER_EVENT_ID").isNull()
				.condition("E.EVENT_ID LIKE ? OR E.EVENT_NAME LIKE ?", new Object[]{EventRole.MANAGER,"%"+eventKey+"%","%"+eventKey+"%"})
				.list(dao);
		result.setData(events);
		return result;
	}
}
