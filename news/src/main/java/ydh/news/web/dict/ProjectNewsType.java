package ydh.news.web.dict;

import ydh.cicada.dict.DictEnum;
import ydh.cicada.dict.TitleDict;
/**
 * 项目新闻类型
 * @author 李永炳
 *
 */
@DictEnum
public enum ProjectNewsType implements TitleDict {
	
	PUBLIC("普通事件"),PARTNER("股东事件");
	
	private String title;
	
	private ProjectNewsType(String title) {
		this.title = title;
	}
	@Override
	public String title() {
		return title;
	}

}
