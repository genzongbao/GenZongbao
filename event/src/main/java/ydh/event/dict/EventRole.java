package ydh.event.dict;

import ydh.cicada.dict.DictEnum;
import ydh.cicada.dict.TitleDict;

/**
 * 角色
 *
 */
@DictEnum
public enum EventRole implements TitleDict {
	/**
	 * 创建人
	 */
	CREATER("创建人"), 
	/**
	 * 责任人
	 */
	MANAGER("责任人"), 
	/**
	 * 验收人
	 */
	CHECKER("验收人"),
	/**
	 * 评论人
	 */
	COMMENTER("评论人"), 
	/**
	 * 节点责任人
	 */
	NODEMANAGER("节点责任人");
	
	private String title;
	
	private EventRole(String title) {
		this.title = title;
	}
	
	public String title() {
		return title;
	}
}
