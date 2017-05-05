package ydh.event.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ydh.cicada.dao.Page;
import ydh.cicada.dict.YesNo;
import ydh.cicada.query.QueryObject;
import ydh.cicada.service.CommonService;
import ydh.customer.entity.Customer;
import ydh.event.dict.EventRole;
import ydh.event.dict.EventState;
import ydh.event.dict.SortCondition;
import ydh.event.entity.*;
import ydh.event.query.EventQuery;
import ydh.event.service.EventConditionService;
import ydh.event.service.EventService;
import ydh.mvc.BaseResult;
import ydh.mvc.ErrorCode;
import ydh.mvc.ex.WorkException;
import ydh.utils.CheckTool;
import ydh.utils.GsonUtil;
import ydh.utils.TransSerialNumTool;
import ydh.website.localization.controller.BaseController;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@Controller
@RequestMapping("event/interface")
public class EventInterfaceController extends BaseController {
	
	@Autowired
	private CommonService commonService;
	@Autowired
	private EventService eventService;
	@Autowired
	private EventConditionService conditionService;
	
	/**
	 * 获取事件列表接口
	 */
	@SuppressWarnings({ "unchecked" })
	@ResponseBody
	@RequestMapping(value = "get-event-list", produces = "text/html;charset=utf8")
	public String getEventList(@RequestBody String screen) {
		Type type=new TypeToken<EventQuery>(){}.getType();
		EventQuery query=GsonUtil.toObjectChange(screen,type);
		query.set_cusId(query.getCusId());
		if(query.getCusId() == null){
			returnResult.setError(true);
			returnResult.setErrorCode(ErrorCode.NO_LOGIN);
			returnResult.setMsg("请登录后操作");
			return resultVal();
		}
		if(query.getSortCondition()==null){
			query.setSortCondition(SortCondition.LASTDATE);
		}
		if(StringUtils.isBlank(query.getEventTagName())){
			query.setEventTagName("全部");
		}
		query.setTop(false);//查询事件
		query.setS(10);		//每页数量
		Page<Event> pageEvent= this.eventService.formatList(eventService.queryPage(query),query.getCusId());
		returnResult.setData(pageEvent);
		return resultVal();
	}
	
	/**
	 * 保存事件/节点接口
	 */
	@SuppressWarnings({ })
	@RequestMapping(value = "save-event", produces = "text/html;charset=utf8")
	@ResponseBody
	public String saveEvent( @RequestBody String body, int cusId) {
		
		if(cusId == 0){
			returnResult.setMsg("请登录后操作");
			return resultVal();
		}
		try {
			Type type = new TypeToken<ListEventAndCondition>() {}.getType();
			ListEventAndCondition eventAndCondition=null;
			eventAndCondition = GsonUtil.toObjectChange(body, type);//JacksonUtil.getObjectFromJson(body, ListEventAndCondition.class);
			Event mainEvent=this.eventService.dealSave(eventAndCondition.getEvents(), cusId);
			if(mainEvent!=null){
				List<EventChangeCondition> conditions=this.conditionService.saveConditions(eventAndCondition.getCondition()).getData();
				
				//轮寻查找符合状态的
				if(CheckTool.checkListIsNotNull(conditions)){
					for (EventChangeCondition eventChangeCondition : conditions) {
						this.conditionService.ConditionListener(eventChangeCondition,cusId);
					}
				}
				returnResult.setMsg("保存成功.");
				return resultVal();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		returnResult.setMsg("保存失败.");
		return resultVal();
	}
	
	/**
	 * 保存评论/验收接口
	 */
	@RequestMapping(value="save-event-comment", produces = "text/html;charset=utf8")
	@ResponseBody
	public String saveEventComment(@RequestBody String result) {
		Type type=new TypeToken<BaseResult<EventComment>>() {}.getType();
		BaseResult<EventComment> resultbody = GsonUtil.toObjectChange(result, type);
		EventComment comment=resultbody.getData();
		if(comment.getCusId() == null){
			returnResult.setError(true);
			returnResult.setMsg("请登录后提交");
			return resultVal();
		}
		//查看是否可提交
		Event event = this.commonService.load(Event.class, comment.getEventId());
		if(event.getEventState() == EventState.FINISH){
			returnResult.setError(true);
			returnResult.setMsg("本节点不可提交.");
			return resultVal();
		}
		if(event.getOverTimerFlag() == YesNo.YES && event.getOverTimerStop() == YesNo.NO){
			returnResult.setMsg("本节点已超时,不可提交.");
			returnResult.setError(true);
			return resultVal();
		}
		comment=this.eventService.saveEventComment(comment,event);
		boolean eventChangeFlag=true;
		switch (comment.getCommentType()) {
		case SUBMITPROGRESS:
			event.setEventState(EventState.SUBMITPROCESS);
			break;
		case SUBMITEND:
			event.setEventState(EventState.SUBMITEND);
			break;
		case FAILED:
			event.setEventState(EventState.FAILED);
			break;
		case SUCCESS:
			event.setEventState(EventState.SUCCESS);
			//创建验收节点
			eventService.createNodeSuccessNode(event, comment.getCusId());
			//查询条件
			if(eventService.countDefaultConditionForEvent(event.getEventId())<1){					
				event.setEventState(EventState.FINISH);
				//设置事件完成日期
				event.setEventRealFinishDate(nowDate);
			}
			break;
		default:
			eventChangeFlag=false;
			break;
		}
		if(eventChangeFlag){			
			//最近更新
			event.setEventLastChangeDate(nowDate);
			//更新事件
			this.commonService.update(event);
			//更新状态
			this.eventService.changeFlagReflush(comment.getCusId(), event.getEventId(),event.getHigherEventId());
			//条件判断
			this.conditionService.ConditionListener(event,comment.getCusId());
			//通知
			this.eventService.sendInstationMessage(event, comment.getCusId());
		}
		returnResult.setMsg("保存成功.");
		return resultVal();
	}
	/**
	 * 获取标签列表接口
	 */
	@SuppressWarnings({ "unchecked"})
	@RequestMapping(value="get-event-tag", produces = "text/html;charset=utf8")
	@ResponseBody
	public String getEventTag(Integer cusId) {
		if(cusId == null){
			returnResult.setError(true);
			returnResult.setMsg("请登录后操作");
			return resultVal();
		}
		returnResult.setData(QueryObject.select(EventTag.class).cond("CUS_ID").equ(cusId)
				.desc("EVENT_TYPE_CREATE_DATE")
				.list(commonService));
		return resultVal();
	}
	
	/**
	 * 新增标签接口
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value="save-event-tag", produces = "text/html;charset=utf8")
	@ResponseBody
	public String saveEventTag(@RequestBody String tag){
		EventTag eventTag = GsonUtil.toObject(tag, EventTag.class);
		if(eventTag.getCusId() == 0){
			returnResult.setMsg("请登录后操作");
			return resultVal();
		}
		eventTag.setEventTagCreateDate(nowDate);
		this.commonService.insert(eventTag);
		returnResult.setMsg("保存成功");
		return resultVal();
	}
	
	/**
	 * 对接收的人员进行标识接口
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value="deal-person", produces = "text/html;charset=utf8")
	@ResponseBody
	public String dealPerson(String eventId, EventRole eventRole, Integer cusId) {
		String str="";
		List<EventCusMapping> mappings = this.eventService.getEventPersonByEventIdAndRole(eventId, eventRole);
		for (EventCusMapping eventCusMapping : mappings) {
			str+=eventCusMapping.getCusId()+",";
		}
		List<Customer> friends = QueryObject.select("C.*",Customer.class).from("CUSTOMER_FRIEND_MAPPING M LEFT JOIN CUSTOMER C ON M.FRIEND_ID=C.CUS_ID")
											.cond("M.CUS_ID").equ(cusId).list(commonService);
		for (Customer customer : friends) {
			if(str.contains(customer.getCusId().toString())){
				customer.setChecked(true);
			}
		}
		returnResult.setData(friends);
		return resultVal();
	}
	
	/**
	 * 获取节点汇报/验收评论
	 * @param cusId 需要筛选的人
	 * @param eventId	事件/
	 * @return
	 * @throws WorkException 
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "get-event-comments", produces = "text/html;charset=utf8")
	@ResponseBody
	public String getEventComments(int cusId,String eventId) throws WorkException {
		 
		returnResult=new BaseResult<>();
		List<EventComment> eventComments = this.eventService.getEventComment(cusId,eventId);
		returnResult.setData(eventComments);
		return resultVal();
	}
	
	/**
	 * 创建流水号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("create-serial-num")
	public @ResponseBody String createSerialNum(){
		returnResult.setData(TransSerialNumTool.createSerialNum());
		return resultVal();
	}
	
	/**
	 * 用户系统好友列表
	 * @param eventId
	 * @param eventRole
	 * @param cusId
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="people-list", produces = "text/html;charset=utf8")
	@ResponseBody
	public String peopleList(Integer cusId, @RequestBody String mappings , ModelMap model){
		String str="";
		Type type = new TypeToken<List<EventCusMapping>>() {}.getType();
		List<EventCusMapping> eventCusMappings = GsonUtil.toObjectChange(mappings, type);
		for (EventCusMapping eventCusMapping : eventCusMappings) {
			str+=eventCusMapping.getCusId()+",";
		}
		List<Customer> friends = QueryObject.select("C.*",Customer.class).from("CUSTOMER_FRIEND_MAPPING M LEFT JOIN CUSTOMER C ON M.FRIEND_ID=C.CUS_ID")
											.cond("M.CUS_ID").equ(cusId).list(commonService);
		if(str!=""){
			for (Customer customer : friends) {
				if(str.contains(customer.getCusId().toString())){
					customer.setChecked(true);
				}
			}
		}
		returnResult.setData(friends);
		return resultVal();
	}


	/**
	 * 查询可引用的事件
	 * @param cusId	用户id
	 * @param eventId 当前事件id或者节点id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="get-quote-events", produces = "text/html;charset=utf8")
	@ResponseBody
	public String getQuoteEventList(int cusId,String eventId){
		returnResult=new BaseResult<List<Event>>();
		returnResult.setData(eventService.getQuoteEvent(cusId, eventId));
		return resultVal();
	}
	/**
	 * 搜索事件
	 * @param cusId  用户id
	 * @param eventKey  搜索关键字
	 * @return
	 */
	@RequestMapping(value="search-event/{cusId}/{eventKey}", produces = "text/html;charset=utf8")
	@ResponseBody
	public String searchEventByIdOrName(@PathVariable int cusId,@PathVariable String eventKey){
		returnResult=this.eventService.searchEvents(cusId,eventKey);
		return resultVal();
	}
}
