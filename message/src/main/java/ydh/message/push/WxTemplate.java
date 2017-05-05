package ydh.message.push;


public class WxTemplate {
	
	private String templateId;
	
	public WxTemplate(String templateId){
		this.templateId = templateId;
	}
	
	public void send(String openId, String...params) {
		// TODO send message to wx
	}
	
	public String getTemplateId() {
		return templateId;
	}
	
}
