package ydh.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ydh.event.entity.Event;
import ydh.event.entity.ListEventAndNodes;
import ydh.event.service.EventInfoService;
import ydh.website.localization.controller.BaseController;

import java.util.List;

/**
 * 事件详情
 * @author tearslee
 *
 */
@Controller
@RequestMapping(value="event/info")
public class EventInfoController extends BaseController{
	@Autowired
	private EventInfoService eventInfoservice;
	
	/**
	 * 查询事件详情--节点列表
	 * @param cusId
	 * @param eventId
	 * @param onlyProgress 是否进行过程节点筛选，默认0：看过程 1：只看过程 2：不看过程
	 * @param fallowId 关注某人的节点，人员反馈
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	@RequestMapping(value = "event-progress", produces = "text/html;charset=utf8")
	@ResponseBody
	public String eventDetail(Integer cusId, String eventId,int progressType,int fallowId){
		if(cusId == null){
			returnResult.setMsg("请登录后查看");
			returnResult.setError(true);
			return resultVal();
		}
		ListEventAndNodes eventDetail=new ListEventAndNodes();
		Event mainEvent=eventInfoservice.getEventDetail(eventId,cusId);
		List<Event> nodes=eventInfoservice.getEventNodes(eventId,cusId,progressType,fallowId);
		eventDetail.setMainEvent(mainEvent);
		eventDetail.setNodes(nodes);
		returnResult.setData(eventDetail);
		return resultVal();
	}
	
	/**
	 * 查询事件详情--附件与评论,标签列表
	 * @param cusId
	 * @param eventId
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	@RequestMapping(value = "event-detail-info", produces = "text/html;charset=utf8")
	@ResponseBody
	public String eventDetailInfo(Integer cusId, String eventId){
		if(cusId == null){
			returnResult.setMsg("请登录后查看");
			returnResult.setError(true);
			return resultVal();
		}
		Event mainEvent=null;		
		mainEvent=eventInfoservice.getEventDetailInfo(eventId,cusId);
		returnResult.setData(mainEvent);
		return resultVal();
	}
	
	/**
	 * 查询当前用户负责的正在进行中的事件列表--可反馈进程事件
	 * @param cusId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "running-event-list", produces = "text/html;charset=utf8")
	@ResponseBody
	public String getRunningEvent(int cusId){
		returnResult.setData(eventInfoservice.getEventListForProgress(cusId));
		return resultVal();
	}
	/**
	 * 查询当前用户负责的正则进行中的节点列表--可反馈进程节点
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "running-node-list", produces = "text/html;charset=utf8")
	@ResponseBody
	public String getProgressDetail(int cusId,String eventId){
		returnResult.setData(eventInfoservice.getNodeListForProgress(cusId,eventId));
		return resultVal();
	}
	/**
	 * 查询事件所有成员
	 * @param eventId  事件id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "get-all-member", produces = "text/html;charset=utf8")
	@ResponseBody
	public String getAllMember(String eventId){
		returnResult.setData(eventInfoservice.getAllMember(eventId));
		return resultVal();
	}
}
