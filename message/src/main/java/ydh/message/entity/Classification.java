package ydh.message.entity;

import ydh.cicada.dict.TitleDict;

/**
 * 消息分类
 * @author tearslee
 *
 */
public enum Classification implements TitleDict{
	/**
	 * 全平台公告消息
	 */
	PUBLIC_NOTICE("公告消息"),
	/**
	 * 用户群/标签消息
	 */
	TAG_NOTICE("群组消息"),
	/**
	 * 用户个人消息
	 */
	PRIVATE_NOTICE("用户消息")
	;

	
	private String title;
	
	private Classification(String title) {
		this.title=title;
	}
	@Override
	public String title() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
