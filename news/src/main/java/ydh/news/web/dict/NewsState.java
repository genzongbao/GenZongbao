package ydh.news.web.dict;

import ydh.cicada.dict.DictEnum;
import ydh.cicada.dict.TitleDict;

/**
 * 新闻状态
 * @author LXL
 */
@DictEnum
public enum NewsState implements TitleDict {
	
	APPLING("申请中"),NORMAL("通过"),REFUSE("拒绝"),DELETE("删除");
	
	private String title;
	
	private NewsState(String title) {
		this.title = title;
	}
	
	public String title() {
		return title;
	}
}
