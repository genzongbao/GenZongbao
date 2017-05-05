package ydh.message.dict;

import ydh.cicada.dict.DictEnum;
import ydh.cicada.dict.TitleDict;

@DictEnum
public enum MessageState implements TitleDict{
	
	UNREAD("未读"),
	READED("已读"),
	REMOVED("删除");
	
	private String title;
	
	private MessageState(String title) {
		this.title=title;
	}
	
	@Override
	public String title() {
		return title;
	}
}
