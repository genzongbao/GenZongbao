package ydh.event.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;
import ydh.event.dict.TemplateType;

@Entity(name = "EVENT_TEMPLATE")
public class EventTemplate {
	
	@Id
	@Column(name = "EVENT_TEMPLATE_ID")
	private String eventTemplateId;

	@Column(name = "EVENT_ID")
	private String eventId;
	
	@Column(name = "EVENT_NAME")
	private String templateName;
	
	@Column(name = "TEMPLATE_TYPE")
	private TemplateType templateType;
	
	@Column(name = "CUS_ID")
	private Integer cusId;
	
	public EventTemplate() {
	}

	

	public String getEventTemplateId() {
		return eventTemplateId;
	}



	public void setEventTemplateId(String eventTemplateId) {
		this.eventTemplateId = eventTemplateId;
	}



	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public TemplateType getTemplateType() {
		return templateType;
	}

	public void setTemplateType(TemplateType templateType) {
		this.templateType = templateType;
	}

	public Integer getCusId() {
		return cusId;
	}

	public void setCusId(Integer cusId) {
		this.cusId = cusId;
	}

}
