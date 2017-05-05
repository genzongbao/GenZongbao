package ydh.customer.dict;

import ydh.cicada.dict.DictEnum;
import ydh.cicada.dict.TitleDict;

@DictEnum
public enum LoginState implements TitleDict{
	SUCCESS("正常"),
	FAILED("失败"),
	LOCKED("已冻结");
	
	private String title;
	private LoginState(String title) {
		this.title = title;
	}
	
	public String title() {
		return title;
	}
}
