package ydh.event.dict;

import ydh.cicada.dict.TitleDict;

/**
 * 标签是否有效
 * @author tearslee
 *
 */
public enum TagStatus implements TitleDict{
	/**
	 * 有效
	 */
    ALIVE("有效"),
    /**
     * 无效
     */
    DIED("无效");
    private String title;

    private TagStatus(String title) {
        this.title = title;
    }

	@Override
	public String title() {
		return title;
	}
}
