package ydh.event.dict;

import ydh.cicada.dict.TitleDict;

public enum SortCondition implements TitleDict{
	/**
	 * 最近变更的事件
	 */
	LASTDATE("最近更新"), 
	/**
	 * 进行最久的事件
	 */
	LONGTIME("最长时间");
	
	private String title;
	
	private SortCondition(String title) {
		this.title = title;
	}
	
	public String title() {
		return title;
	}
}
