package ydh.event.dict;

import ydh.cicada.dict.DictEnum;
import ydh.cicada.dict.TitleDict;

/**
 * 事件状态
 *
 */
@DictEnum
public enum EventState implements TitleDict {
	/***==已启动==**/
	/**
	 * 进行中
	 */
	RUNNING("进行中"),//可汇报
	/**
	 * 已完结
	 */
	FINISH("完结状态"),
	/**
	 * 节点
	 * 未开始
	 */
	NOSTART("创建成功"),
	/**
	 * 节点
	 * 已提交过程汇报
	 */
	SUBMITPROCESS("已提交过程汇报"),
	/**
	 * 节点
	 * 已提交申请完结
	 */
	SUBMITEND("已提交申请完结"),
	/**
	 * 节点
	 * 验收不通过
	 */
	FAILED("已提交申请验收不通过"),
	/**
	 * 节点
	 * 验收通过
	 */
	SUCCESS("验收通过");
	
	
	private String title;
	
	private EventState(String title) {
		this.title = title;
	}
	
	public String title() {
		return title;
	}
}
