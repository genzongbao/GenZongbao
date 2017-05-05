package ydh.event.service;

import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ydh.cicada.dao.JdbcDao;
import ydh.cicada.dict.YesNo;
import ydh.cicada.query.QueryObject;
import ydh.event.dict.EventState;
import ydh.event.entity.Event;
import ydh.event.entity.EventChangeCondition;
import ydh.mvc.BaseResult;
import ydh.utils.CheckTool;
import ydh.utils.GsonUtil;
import ydh.utils.UUIDTool;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * 事件条件Service
 * @author tearslee
 *
 */
@Service
@Transactional
public class EventConditionService {
	@Autowired
	private JdbcDao dao;
	@Autowired
	private EventService eventService;
	/**
	 * 新增事件条件
	 * @param result
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BaseResult saveConditions(String result){
		Type type=new TypeToken<BaseResult<List<EventChangeCondition>>>(){}.getType();
		BaseResult<List<EventChangeCondition>> baseResult=null;
		try {
			baseResult=GsonUtil.toObjectChange(result, type);
			if(baseResult!=null && CheckTool.checkListIsNotNull(baseResult.getData())){
				
				deleteCoditionForEvent(baseResult.getData().get(0).getEventId());
				
				for (EventChangeCondition eventChangeCondition : baseResult.getData()) {
						eventChangeCondition.setEventChangeConditionId(UUIDTool.getUUID());
						eventChangeCondition.setConditionCreateDate(new Date());
						eventChangeCondition.setIsFulfil(YesNo.NO);
						dao.insert(eventChangeCondition);
						ConditionListener(eventChangeCondition,baseResult.getCusId());//TODO 放这里合适吗？
				}
				baseResult.setData(baseResult.getData());
				baseResult.setMsg("保存成功！");
				return baseResult;
			}
			baseResult=new BaseResult();
			baseResult.setMsg("保存失败！");
			baseResult.setError(true);
			return baseResult;
		} catch (Exception e) {
			e.getLocalizedMessage();
		}
		return baseResult;
	}
	/**
	 * 保存事件条件
	 * @param conditions
	 * @return
	 */
	public BaseResult<List<EventChangeCondition>> saveConditions(List<EventChangeCondition> conditions){
		BaseResult<List<EventChangeCondition>> baseResult=new BaseResult<List<EventChangeCondition>>();
		try {
			
			if(CheckTool.checkListIsNotNull(conditions)){
				Date t=new Date();
				for (EventChangeCondition eventChangeCondition : conditions) {
						eventChangeCondition.setEventChangeConditionId(UUIDTool.getUUID());
						eventChangeCondition.setConditionCreateDate(t);
						dao.insert(eventChangeCondition);
				}
				baseResult.setData(conditions);
				baseResult.setMsg("保存成功！");
				return baseResult;
			}
			baseResult.setMsg("保存失败！");
			baseResult.setError(true);
			return baseResult;
		} catch (Exception e) {
			e.getLocalizedMessage();
		}
		return baseResult;
	}
	/**
	 * 添加事件/节点关联条件 
	 * @param condition
	 */
	public void addCondition(EventChangeCondition condition,int cusId){
		if(condition.getConditionEventState()!=null){
			condition.setEventChangeConditionId(UUIDTool.getUUID());
			condition.setConditionCreateDate(new Date());
			dao.insert(condition);
			ConditionListener(condition,cusId);
		}
	}
	
	/**
	 * 删除事件条件
	 * @param result
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BaseResult deleteCondition(String result){
		Type type=new TypeToken<BaseResult<EventChangeCondition>>(){}.getType();
		BaseResult<EventChangeCondition> baseResult=null;
		try {
			baseResult=GsonUtil.toObjectChange(result, type);
			if(baseResult!=null && baseResult.getData()!=null){
				dao.delete(baseResult.getData());
//				ConditionListener(baseResult.getData(),cusId);
				baseResult.setMsg("删除成功");
				baseResult.setData(null);
				return baseResult;
			}
			baseResult=new BaseResult();
			baseResult.setMsg("删除失败");
			baseResult.setError(true);
		} catch (Exception e) {
			e.getLocalizedMessage();
		}
		
		return baseResult;
	}
	/**
	 * 删除某一事件的所有条件
	 * @param eventId  事件id
	 */
	public void deleteCoditionForEvent(String eventId){
		String deleteSql="delete from EventChangeCondition c where c.eventId=?";
		this.dao.getJdbcTemplate().update(deleteSql, new Object[]{eventId});
	}
	/**
	 * 事件改变监听（待定）
	 * @param changedEvent
	 */
	public void ConditionListener(Event changedEvent, int cusId){
		//作为被改变事件引起的改变
		eventConditionFulFil(changedEvent);
		//作为改变事件引起的改变
			//查询符合当前事件的状态，且状态为未完成的事件条件
			List<EventChangeCondition> con=QueryObject.select(EventChangeCondition.class)
					.cond("CONDITION_EVENT_ID").equ(changedEvent.getEventId())
					.cond("CONDITION_EVENT_STATE").equ(changedEvent.getEventState())
					.cond("IS_FULFIL").equ(YesNo.NO)//状态为未完成。已完成的则不需再监听需要改变的事件
					.list(dao);
			if(CheckTool.checkListIsNotNull(con)){
				//该事件被设置为某事件的启动或者完结条件
				for (EventChangeCondition eventChangeCondition : con) {
					//若符合判定 	且	条件状态为	“未完成”
						//改变该条件为已完成
						eventChangeCondition.setIsFulfil(YesNo.YES);
						dao.update(eventChangeCondition);
						
						//被改变事件
						//若事件已为该状态则不做任何操作
						Event needChangeEvent=QueryObject.select(Event.class)
								.cond("EVENT_ID").equ(eventChangeCondition.getEventId())
								.cond("EVENT_STATE").notEqu(eventChangeCondition.getEventState())
								.firstResult(dao);
						if(eventChangeCondition.getConditionAnd().equals(YesNo.NO)){//满足之一即可完成的条件
								//若事件与条件设置状态不符  则判断事件当前状态								
								eventStateUpdate(eventChangeCondition,needChangeEvent,cusId);
								//TODO 新增事件状态改变记录节点......
							
						}else{
							String sql="select count(1) from event_change_condition"
									+ " where event_id=? and is_fulfil=?";
							//查询是否有未满足的条件>1则仍有
							Integer totalCount=this.dao.getJdbcTemplate()
									.queryForObject(sql,new Object[]{needChangeEvent.getEventId(),YesNo.NO},Integer.class);
							if(totalCount.intValue()<1){//>0还是>1取决于是否在同一个事务内
								eventStateUpdate(eventChangeCondition,needChangeEvent,cusId);
							}
							
						}
				}
			}
	}
	/**
	 * 查询与该事件相关的其他未完成条件，若“需修改状态”与该事件一致，则全部修改为已完成。
	 * @param changedEvent
	 */
	private void eventConditionFulFil(Event changedEvent){
		List<EventChangeCondition> fulfil=QueryObject.select(EventChangeCondition.class)
				.cond("EVENT_ID").equ(changedEvent.getEventId())
				.cond("EVENT_STATE").equ(changedEvent.getEventState())
				.cond("IS_FULFIL").equ(YesNo.NO)
				.list(dao);
		if(CheckTool.checkListIsNotNull(fulfil)){
			for (EventChangeCondition eventChangeCondition : fulfil) {
				eventChangeCondition.setIsFulfil(YesNo.YES);
				dao.update(eventChangeCondition);
			}
		}
	}
	
	/**
	 * 更新事件状态
	 * @param eventChangeCondition
	 * @param needChangeEvent
	 */
	private void eventStateUpdate(EventChangeCondition eventChangeCondition,Event needChangeEvent,int cusId){
		boolean eventChangeFlag=false;
		//若事件与条件设置状态不符  则判断事件当前状态								
		if(eventChangeCondition.getEventState().equals(EventState.RUNNING)
				&& needChangeEvent.getEventState().equals(EventState.NOSTART)){
			//是事件启动条件，状态为“未完成”，则事件必须是未启动状态,才可改变事件状态
			needChangeEvent.setEventState(eventChangeCondition.getEventState());
			needChangeEvent.setEventStartDate(new Date());
			dao.update(needChangeEvent);
			eventChangeFlag=true;
			
		}else if(eventChangeCondition.getEventState().equals(EventState.FINISH) 
				&& !needChangeEvent.getEventState().equals(EventState.FINISH)){
			//是事件完结条件，状态为“未完成”，事件必须不是完结状态，才可改变事件状态
			needChangeEvent.setEventState(EventState.FINISH);
			needChangeEvent.setEventRealFinishDate(new Date());
			dao.update(needChangeEvent);
			eventChangeFlag=true;
		}
		if(eventChangeFlag){			
				String sql="update event_cus_mapping set change_flag=?"
						+ " where event_id=? and cus_id!=?";
				this.dao.getJdbcTemplate().update(sql,new Object[]{YesNo.YES.ordinal(),needChangeEvent.getEventId(),cusId});
			eventService.sendInstationMessage(needChangeEvent, cusId);
			ConditionListener(needChangeEvent,cusId);//监听
		}
	}
	/**
	 * 添加事件条件时判断是否已有满足该条件的事件
	 * @param eventChangeCondition
	 */
	public void ConditionListener(EventChangeCondition eventChangeCondition,int cusId){
		if(eventChangeCondition.getIsFulfil().equals(YesNo.NO)){
			List<Event> events=QueryObject.select(Event.class)
					.cond("EVENT_ID").equ(eventChangeCondition.getConditionEventId())
					.cond("EVENT_STATE").equ(eventChangeCondition.getConditionEventState())
					.list(dao);
//			List<EventChangeCondition> con=QueryObject.select(EventChangeCondition.class)
////					.cond("IS_FULFIL").equ(YesNo.NO)//状态为未完成。已完成的则不需再监听需要改变的事件
//					.list(dao);
			if(CheckTool.checkListIsNotNull(events)){
				for (Event event : events) {
					ConditionListener(event,cusId);
				}
			}
		}
	}
}
