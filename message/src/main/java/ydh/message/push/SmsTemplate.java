package ydh.message.push;

public class SmsTemplate {
	
	private final String templateId;
	
	public SmsTemplate(String templateId) {
		this.templateId = templateId;
	}
	
	public void send(String mobile, String...params) {
//		SmsTool.sendSms(mobile, templateId, params);
	}

	public String getTemplateId() {
		return templateId;
	}

	
}
