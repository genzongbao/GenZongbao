package ydh.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ydh.event.entity.EventChangeCondition;
import ydh.event.service.EditEventService;
import ydh.event.service.EventConditionService;
import ydh.website.localization.controller.BaseController;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("event/modification")
public class EditEventController extends BaseController {

	@Autowired
	private EditEventService editEventService;
	@Autowired
	private EventConditionService eventConditionService;
	
	@RequestMapping(value = "base", produces = "text/html;charset=utf8")
	@ResponseBody
	public String editBaseEvent(@RequestBody String result){
		returnResult=this.editEventService.editBaseEvent(result);
		return resultVal();
	}
	@RequestMapping(value = "quotes", produces = "text/html;charset=utf8")
	@ResponseBody
	public String editQuotes(@RequestBody String result,String eventId){
		returnResult=this.editEventService.editQuoteRelation(result, eventId);
		return resultVal();
	}
	/**
	 * 删除附件
	 * @param result
	 * @param eventId
	 * @return
	 */
	@RequestMapping(value = "delete-quote", produces = "text/html;charset=utf8")
	@ResponseBody
	public String deleteQuotes(String quoteId){
		returnResult=this.editEventService.deleteQuoteRelation(quoteId);
		return resultVal();
	}
	/**
	 * 修改分类
	 * @param result
	 * @param eventId
	 * @return
	 */
	@RequestMapping(value = "classify", produces = "text/html;charset=utf8")
	@ResponseBody
	public String classify(@RequestBody String result,String eventId){
		returnResult=this.editEventService.editEventTagMapping(result, eventId);
		return resultVal();
	}
	/**
	 * 修改关系
	 * @param result
	 * @param eventId
	 * @return
	 */
	@RequestMapping(value = "participant", produces = "text/html;charset=utf8")
	@ResponseBody
	public String participant(@RequestBody String result,String eventId){
		returnResult=this.editEventService.editCusMapping(result,eventId);
		return resultVal();
	}
	/**
	 * 修改条件
	 * @param result
	 * @param eventId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "condition", produces = "text/html;charset=utf8")
	@ResponseBody
	public String condition(@RequestBody String result,String eventId){
		returnResult=this.editEventService.editConditions(result,eventId);
		if(returnResult!=null && returnResult.getData()!=null){
			List<EventChangeCondition> list=(ArrayList<EventChangeCondition>) returnResult.getData();
			for (EventChangeCondition condition : list) {				
				this.eventConditionService.ConditionListener(condition, returnResult.getCusId());
			}
		}
		return resultVal();
	}
	
}
