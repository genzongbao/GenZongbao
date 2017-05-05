package ydh.message.dict;

import ydh.cicada.dict.DictEnum;
import ydh.cicada.dict.TitleDict;

/**
 * 设备代码
 * @author lixl
 */
@DictEnum
public enum MobileCodeType implements TitleDict {
	
	IOS("IOS设备"),
	ANDROID("Android设备");
	
	private String title;
	
	private MobileCodeType(String title) {
		this.title=title;
	}
	
	@Override
	public String title() {
		return title;
	}
}
