package ydh.event.dict;

import ydh.cicada.dict.DictEnum;
import ydh.cicada.dict.TitleDict;

/**
 * 事件状态
 *
 */
@DictEnum
public enum EventResult implements TitleDict {
	SUCCESS("成功"),
	FAILED("失败");
	
	private String title;
	
	private EventResult(String title) {
		this.title = title;
	}
	
	public String title() {
		return title;
	}
}
