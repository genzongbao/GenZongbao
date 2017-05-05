package ydh.customer.dict;

import ydh.cicada.dict.DictEnum;
import ydh.cicada.dict.TitleDict;

/**
 * 客户状态
 * @author LXL
 *
 */
@DictEnum
public enum CustomerState implements TitleDict {
	NORMAL("正常"), 
	TEMP("临时"), 
	LOCKED("冻结"), 
	REMOVED("注销");
	
	private String title;
	
	private CustomerState(String title) {
		this.title = title;
	}
	
	public String title() {
		return title;
	}
}
