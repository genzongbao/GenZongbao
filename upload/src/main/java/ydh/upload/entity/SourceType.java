package ydh.upload.entity;

import ydh.cicada.dict.TitleDict;

public enum SourceType implements TitleDict{
	/**
	 * 事件或者节点
	 */
	EVENT("事件/节点"),
	/**
	 * 评论
	 */
	COMMENT("评论");

	private String title;
	
	private SourceType(String title) {
		this.title = title;
	}
	
	public String title() {
		return title;
	}
}
