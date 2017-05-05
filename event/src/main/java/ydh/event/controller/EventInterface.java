package ydh.event.controller;

import org.springframework.ui.ModelMap;
import ydh.event.dict.EventRole;
import ydh.event.dict.EventState;
import ydh.event.query.EventQuery;


public interface EventInterface {

	/**
	 * 获取事件列表接口
	 * @param query
	 * @return
	 */
	public String getEventList(String screen);
	/**
	 * 新建汇报接口(主事件列表,默认第一个主事件下的节点)
	 * @param cusId
	 * @param eventId
	 * @return
	 */
	public String getCusNodeList(Integer cusId, String eventId);
	/**
	 * 保存事件/节点接口
	 * @param event
	 * @param cusId
	 * @return
	 */
	public String saveEvent(String body, String cusId);
	
	/**
	 * 删除事件
	 * @param eventId
	 * @param cusId
	 * @return
	 */
	public String delEvent(String eventId, Integer cusId);
	/**
	 * 退出事件创建===删除上传的文件与图片
	 * @return
	 */
	public String quitEventCreate();
	
	/**
	 * 保存评论/验收接口
	 * @param eventComment
	 * @return
	 */
	public String saveEventComment(String result);
	//模板
	/**
	 * 获取标签列表接口
	 * @param cusId
	 * @return
	 */
	public String getEventTag(Integer cusId);
	/**
	 * 新增标签接口
	 * @param tag
	 * @return
	 */
	public String saveEventTag(String tag);
	
	/**
	 * 对接收的人员进行标识接口
	 * @param eventId
	 * @param eventRole
	 * @param cusId
	 * @return
	 */
	public String dealPerson(String eventId, EventRole eventRole, Integer cusId);
	/**
	 * 子事件列表
	 * @param query
	 * @return
	 */
	public String eventNodes(EventQuery query);
	/**
	 * 获取事件详情(不包括节点)
	 * @param cusId
	 * @param eventId
	 * @return
	 */
	public String eventDetail(Integer cusId, String eventId);
	/**
	 * 获取节点汇报/验收评论
	 * @param cusId
	 * @param eventId
	 * @return
	 */
	public String getEventComments(String result, String eventId);
	
	/**
	 * 创建流水号
	 * @return
	 */
	public String createSerialNum();
	
	/**
	 * 用户好友列表
	 * @param eventId
	 * @param eventRole
	 * @param cusId
	 * @param model
	 * @return
	 */
	public String peopleList(Integer cusId, String mappings, ModelMap model);
	
	/**
	 * 改变节点状态
	 * @param eventState
	 * @param eventId
	 * @param cusId
	 * @return
	 */
	public String updateNodeState(EventState eventState, String eventId, Integer cusId);
}
