package ydh.message.entity;

import ydh.cicada.dict.TitleDict;

public enum MessageType implements TitleDict{
	
	/**
	 * 普通文本通知
	 */
	NORMAL_MESSAGE("普通消息"),
	/**
	 * 事件消息
	 */
	EVENT_MESSAGE("事件消息"),
	/**
	 * 链接消息
	 */
	URL_MESSAGE("链接消息");

	private String title;
	
	private MessageType(String title) {
		this.title=title;
	}
	
	@Override
	public String title() {
		return this.title;
	}

}
