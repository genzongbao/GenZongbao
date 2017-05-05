package ydh.customer.dict;

import ydh.cicada.dict.TitleDict;
import ydh.customer.entity.Customer;
import ydh.message.push.MessageType;
import ydh.message.push.PushSetting;
import ydh.message.push.SmsTemplate;
import ydh.message.push.WxTemplate;

public enum CustomerMessageType implements TitleDict,MessageType<Customer> {
	
	WELCOME("欢迎信息", CustomerMessageType.TYPE_NOTICE,new PushSetting(false,false,false,false)
			,new PushSetting(false,false,false,false), 
			"新用户注册", "尊敬的 {1}，你已成功注册成为钱来乐新客户。注册的手机号为:{2}", null, null),
	UPDATE_PASSWORD("修改密码", CustomerMessageType.TYPE_NOTICE,new PushSetting(false,false,false,false)
			,new PushSetting(false,false,false,false),
			"修改密码", "尊敬的 {1}，你已于{2} 成功修改密码！如非本人操作请尽快修改！", null, null),
	UPDATE_PHONE("修改手机号", CustomerMessageType.TYPE_NOTICE,new PushSetting(false,false,false,false)
			,new PushSetting(false,false,false,false),
			"修改手机号", "尊敬的 {1}，你已于{2} 成功修改手机号！新手机号:{3}如非本人操作请尽快修改！", null, null),
	UPDATE_EMAIL("修改邮箱", CustomerMessageType.TYPE_NOTICE,new PushSetting(false,false,false,false)
			,new PushSetting(false,false,false,false),
			"修改邮箱", "尊敬的 {1}，你已于{2}成功修改邮箱:{3}！如非本人操作请尽快修改！", null, null),
	SYSTEM_NOTICE("系统公告",CustomerMessageType.TYPE_NEWS,new PushSetting(false,false,false,false),
			new PushSetting(false,false,false,false),
			"{1}","",null, null),
	 NEW_PROJECT_ONLINE("新项目上线", CustomerMessageType.TYPE_NEWS, new PushSetting(false,false,false,false),
			new PushSetting(false,false,false,false),
			"新标上线","尊敬的钱来乐用户，钱来乐平台新标{1}已上线。您可以抢先关注！",null, null),
	/**退款*/
	REFUND_MONEY("退款通知", CustomerMessageType.TYPE_NOTICE, new PushSetting(false,false,false,false),
			new PushSetting(false,false,false,false),
			"退款通知","尊敬的钱来乐用户，【{1}】项目融资失败，现已将您的投资款{2}元退回您的账户中，请注意查收。", null, null),
	REFERER_NOTICE("推荐通知", CustomerMessageType.TYPE_NOTICE, new PushSetting(false,false,false,false),
			new PushSetting(false,false,false,false),
			"推荐通知","尊敬的钱来乐用户，您推荐的{1}已经成功完成任务获得{2}金币！奖励金币{3}已经发送到账户，请注意查看。", null, null);	
	public static final String TYPE_NEWS ="news";
	public static final String TYPE_NOTICE ="notice";
	
	private String title;
	private PushSetting supportsSetting;
	private PushSetting defaultSetting;
	private String titleTemplate;
	private String contentTemplate;
	private WxTemplate wxTemplate;
	private SmsTemplate smsTemplate;
	private String type;

	private CustomerMessageType(String title, String type,
			PushSetting supportsSettings,PushSetting defaultSetting,
			String titleTemplate,String contentTemplate,
			WxTemplate wxTemplate,SmsTemplate smsTemplate) {
		this.title=title;
		this.defaultSetting = defaultSetting;
		this.supportsSetting = supportsSettings;
		this.titleTemplate = titleTemplate;
		this.contentTemplate = contentTemplate;
		this.smsTemplate = smsTemplate;
		this.wxTemplate = wxTemplate;
		this.type = type;
	}

	@Override
	public String title() {
		return title;
	}

	@Override
	public PushSetting supportsSetting() {
		return supportsSetting;
	}
	
	@Override
	public PushSetting defaultSetting() {
		return defaultSetting;
	}

	@Override
	public String titleTemplate() {
		return titleTemplate;
	}

	@Override
	public String contentTemplate() {
		return contentTemplate;
	}
	
	@Override
	public WxTemplate wxTemplate() {
		return wxTemplate;
	}
	
	@Override
	public SmsTemplate smsTemplate() {
		return smsTemplate;
	}

	@Override
	public String messageType() {
		return type;
	}
}
