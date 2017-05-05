package ydh.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ydh.event.service.EventTagService;
import ydh.mvc.ex.WorkException;
import ydh.website.localization.controller.BaseController;

/**
 * 标签管理
 * @author tearslee
 *
 */
@Controller
@RequestMapping("eventTag/interface")
@Scope("request")
public class EventTagController extends BaseController{
	@Autowired
	private EventTagService eventTagService;
	/**
	 * 根据用户查询标签
	 * @param cusId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value="get-list", produces = "text/html;charset=utf8")
	public String getTagListByCusId(int cusId){
		returnResult.setData(eventTagService.selectByCusId(cusId));
		return resultVal();
	}
	
	/**
	 * 在事件内根据个人用户id获取标签列表（无历史标签列表数据）
	 * @param cusId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value="get-list-event", produces = "text/html;charset=utf8")
	public String getTagListInEvent(int cusId){
		returnResult.setData(eventTagService.selectListInEvent(cusId));
		return resultVal();
	}
	
	/**
	 * 保存标签
	 * @param tags
	 * @param cusId
	 * @return
	 * @throws WorkException 
	 */
	@ResponseBody
	@RequestMapping(value="save-tag",produces = "text/html;charset=utf8")
	public String saveTag(@RequestBody String tags,int cusId){
		eventTagService.saveEventTag(tags, cusId);
		returnResult.setMsg("保存成功!");
		return resultVal();
	}
	
	/**
	 * 删除标签
	 * @param tags
	 * @param cusId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="delete-tag",produces = "text/html;charset=utf8")
	public String deleteTag(@RequestBody String tags,int cusId){
		eventTagService.deleteEventTag(tags, cusId);
		returnResult.setMsg("删除成功！");
		return resultVal();
	}

}

