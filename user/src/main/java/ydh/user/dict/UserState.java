package ydh.user.dict;

import ydh.cicada.dict.DictEnum;
import ydh.cicada.dict.TitleDict;

@DictEnum
public enum UserState implements TitleDict {
	REMOVED("注销", "您的用户账户已被注销，不能登录系统"),
	VALID("正常", ""),
	FREEZED("冻结","您的用户账户已被冻结，解冻后才能登录系统");
	
	private String title;
	private String message;
	
	private UserState(String title, String message) {
		this.title = title;
		this.message = message;
	}
	
	public String title() {
		return title;
	}
	
	public String message() {
		return message;
	}
}
