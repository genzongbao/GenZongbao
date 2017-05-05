package ydh.event.dict;

import ydh.cicada.dict.DictEnum;
import ydh.cicada.dict.TitleDict;

/**
 * 客户状态
 * @author LXL
 *
 */
@DictEnum
public enum CommentType implements TitleDict {
	/**
	 * 普通评论
	 */
	NORMAL("普通评论"),
	/**
	 * 进程汇报
	 */
    SUBMITPROGRESS("进程汇报"),
    /**
     * 申请完结
     */
    SUBMITEND("申请完结"),
    /**
     * 验收通过
     */
    SUCCESS("验收通过"),
    /**
     * 验收不通过
     */
    FAILED("验收不通过");;
	
	private String title;
	
	private CommentType(String title) {
		this.title = title;
	}
	
	public String title() {
		return title;
	}
}
