package ydh.event.dict;

import ydh.cicada.dict.TitleDict;

/**
 * 事件标签类型
 * @author tearslee
 *
 */
public enum TagType implements TitleDict{
	/**
	 * 不可更改标签
	 */
	UNABLED("不可更改"),
	/**
	 * 私有
	 */
	PRIVATE("私有标签"),
    /**
     * 公有
     */
	PUBLIC("公共标签");
	
    private String title;

    private TagType(String title) {
        this.title = title;
    }

	@Override
	public String title() {
		return title;
	}
}
