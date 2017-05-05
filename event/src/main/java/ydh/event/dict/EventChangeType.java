package ydh.event.dict;

import ydh.cicada.dict.DictEnum;
import ydh.cicada.dict.TitleDict;

/**
 * 事件状态
 *
 */
@DictEnum
public enum EventChangeType implements TitleDict {
	OPEN("开放"), 
	CLOSED("封闭");
	
	private String title;
	
	private EventChangeType(String title) {
		this.title = title;
	}
	
	public String title() {
		return title;
	}
}
