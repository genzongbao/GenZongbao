package ydh.push.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ydh.push.service.PushService;
import ydh.website.localization.controller.BaseController;

/**
 * 推送控制器
 * @author tearslee
 *
 */
@Controller
@RequestMapping("push")
public class PushController extends BaseController{
	
	@Autowired
	private PushService pushService;
	/**
	 *添加pushSetting
	 * @return
	 */
	@RequestMapping(value="setting", produces = "text/html;charset=utf8")
	@ResponseBody
	public String addPushSetting(@RequestBody String result){
		returnResult=this.pushService.addPushSetting(result);
		return resultVal();
	}
	/**
	 * 获取用户设置
	 * @param cusId
	 * @return
	 */
	@RequestMapping(value="get-setting", produces = "text/html;charset=utf8")
	@ResponseBody
	public String getPushSetting(int cusId){
		returnResult=this.pushService.selectSetting(cusId);
		return resultVal();
	}
	/**
	 * 获取消息列表
	 * @param cusId
	 * @return
	 */
	@RequestMapping(value="get-message", produces = "text/html;charset=utf8")
	@ResponseBody
	public String getMessageList(int cusId){
		returnResult=this.pushService.selectMessageList(cusId);
		return resultVal();
	}
	
	@RequestMapping("change-read-state/{cusId}/{messageId}")
	public String changeMessageReadState(@PathVariable int cusId,@PathVariable String messageId){
		
		return resultVal();
	}
}
