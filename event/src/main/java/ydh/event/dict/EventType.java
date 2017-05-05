package ydh.event.dict;

import ydh.cicada.dict.DictEnum;
import ydh.cicada.dict.TitleDict;

/**
 * 节点类型
 * @author LXL
 *
 */
@DictEnum
public enum EventType implements TitleDict {
	/**
	 * 普通添加的节点/事件
	 */
	NORMAL("普通节点/事件"),
	/**
	 * 过程节点
	 */
	RECORD("记录过程"),
	/**
	 * 验收节点
	 */
	CHECK("节点验收"), 
	/**
	 * 表示事件完结
	 */
	FINISH("事件完结");
	
	private String title;
	
	private EventType(String title) {
		this.title = title;
	}
	
	public String title() {
		return title;
	}
}
