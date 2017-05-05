package ydh.event.dict;

import ydh.cicada.dict.DictEnum;
import ydh.cicada.dict.TitleDict;

/**
 * 验收状态
 * @author tearslee
 *
 */
@DictEnum
public enum InspectedState implements TitleDict{

	/**
	 * 待验收
	 */
	CHECKING("待验收"),
	/**
	 * 已验收
	 */
	CHECKED("已验收");

	private String title;
	
	private InspectedState(String title) {
		this.title = title;
	}
	
	public String title() {
		return title;
	}
}
