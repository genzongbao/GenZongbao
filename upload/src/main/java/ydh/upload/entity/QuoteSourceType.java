package ydh.upload.entity;

import ydh.cicada.dict.TitleDict;

public enum QuoteSourceType implements TitleDict{
	/**
	 * 图片
	 */
	IMG("图片"),
	/**
	 * 文件
	 */
	FILE("文件"),
	/**
	 * 事件
	 */
	EVENT("事件");
	private String title;
	
	private QuoteSourceType(String title) {
		this.title = title;
	}
	
	public String title() {
		return title;
	}
}
