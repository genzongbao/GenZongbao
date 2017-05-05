package ydh.event.dict;

import ydh.cicada.dict.TitleDict;

/**
 * 操作类型
 * @author tearslee
 *
 */
public enum ActionType implements TitleDict{
	/**
	 * 添加
	 */
    INSERT("新增"),
    /**
     * 删除
     */
    DELETE("删除"),
    /**
     * 修改
     */
    UPDATE("修改");
	
    private String title;

    private ActionType(String title) {
        this.title = title;
    }

	@Override
	public String title() {
		return title;
	}
}
