package ydh.event.dict;

import ydh.cicada.dict.DictEnum;
import ydh.cicada.dict.TitleDict;

/**
 * 客户状态
 * @author LXL
 *
 */
@DictEnum
public enum NodeState implements TitleDict {
	READY("未开启"), 
	RUNNING("执行中"), 
	FINISH("完结");
	
	private String title;
	
	private NodeState(String title) {
		this.title = title;
	}
	
	public String title() {
		return title;
	}
}
