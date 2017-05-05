package ydh.message.push;


public interface MessageType<U> {

	PushSetting defaultSetting();
	
	PushSetting supportsSetting();
	
	String titleTemplate();
	
	String contentTemplate();
	
	SmsTemplate smsTemplate();
	
	WxTemplate wxTemplate();
	
	String messageType();
}
