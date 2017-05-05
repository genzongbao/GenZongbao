package ydh.controller;

import java.util.Date;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ydh.mvc.BaseResult;
import ydh.sms.utils.DateUtil;
import ydh.utils.GsonUtil;
import ydh.utils.MailSender;
import ydh.weixin.entity.WebChatUser;
import ydh.weixin.entity.WeiXinConstant;
import ydh.weixin.service.ILinkService;
import ydh.weixin.template.CreatTemplate;
import ydh.weixin.util.WeixinUtil1;

@Controller
@RequestMapping("message")
public class IndexController {
	@Autowired
	private ILinkService linkService;
	
	/**
	 * 首页
	 * @param model
	 * @return
	 */
	@RequestMapping("login")
	public ModelAndView index(ModelMap model){
		return new ModelAndView("auth/wxlogin");
	}
	
	@RequestMapping("sendSms")
	@ResponseBody
	public String send() throws Exception{//oFXcaxOqUSn-iXASeLKMlVw2sKlQ
		// mFXy-3YDSdFesY_ce8L7iVKHT7LfFrlSCooBymxkXG8 模板id
		String content = "{\"touser\":\"oyCiRwhPLgDea5ymCX1Q2tGfWC5M\",\"template_id\":\"7t_brk-i3T2IFbNHuGD_DapYI4inGwnKz9uhgwBIEeE\","
    			+ "\"data\":{\"first\": {\"value\":\"恭喜你购买成功！\",\"color\":\"#173177\"},"
                + "\"keyword1\":{\"value\":\"巧克力\",\"color\":\"#173177\"},\"keyword2\": {"
                + "\"value\":\"39.8元\","
                + "\"color\":\"#173177\""
                + "},\"keyword3\": {"
                + " \"value\":\"2014年9月22日\","
                 + "   \"color\":\"#173177\""
                 + " },\"keyword4\": {"
                + " \"value\":\"2014年9月22日\","
                 + "   \"color\":\"#173177\""
                 + " },\"remark\":{"
                 + " \"value\":\"欢迎再次购买！\","
                 + "  \"color\":\"#173177\"}}}\"";
		String openId= WeixinUtil1.getOpenIdList("");
		WebChatUser user= WeixinUtil1.getWebChatUserByOpId(WeixinUtil1.getAccessToken(),"oyCiRwhPLgDea5ymCX1Q2tGfWC5M");
		WeixinUtil1.sendMsg(content);
		BaseResult<WebChatUser> result=new BaseResult<WebChatUser>();
		result.setMsg(content);
		result.setData(user);
		return GsonUtil.toJSONString(result);
	}
	
	@RequestMapping("sendEmail")
	public void sendEmail() throws MessagingException{
		MailSender.sendHtmlEmail("tearslee@foxmail.com", "测试", "测试");
	}
	
	@RequestMapping(value = "getTest", method = RequestMethod.GET, produces = "text/html;charset=utf8")
    public String test() {
        String[] temData = {WeiXinConstant.CREATE_NODE_FIRST, "华清松", "app开发", "申请完结","我有一句.jpg", DateUtil.dateToStr(new Date())};
        linkService.SendTemplateMessage(WeiXinConstant.Event_CHANGE_Template, WeiXinConstant.TEST_OPPEN_ID3, "", CreatTemplate.loadingFourKeyword(temData));
        return "成功";
    }
}
