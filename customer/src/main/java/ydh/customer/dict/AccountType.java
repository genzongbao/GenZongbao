package ydh.customer.dict;

import ydh.cicada.dict.TitleDict;

/**
 * 账户类型
 * @author tearslee
 *
 */
public enum AccountType implements TitleDict {
	
	PERSON("个人账号"),
	COMPANY("企业账号");
	
	
	private String title;
	
	private AccountType(String title) {
		this.title=title;
	}

	@Override
	public String title() {
		// TODO Auto-generated method stub
		return this.title;
	}

}
