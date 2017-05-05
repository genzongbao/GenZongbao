package ydh.event.dict;

import ydh.cicada.dict.DictEnum;
import ydh.cicada.dict.TitleDict;

@DictEnum
public enum TemplateType implements TitleDict {
	COMMON("公用模板"), 
	PERSONAL("个人模板");
	
	private String title;
	
	private TemplateType(String title) {
		this.title = title;
	}
	
	public String title() {
		return title;
	}
}
