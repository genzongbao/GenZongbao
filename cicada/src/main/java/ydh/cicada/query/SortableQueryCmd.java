/*
 * Copyright: SBT (c) 2015
 * Company: 成都盛比特信息技术有限公司
 */
package ydh.cicada.query;

/**
 * 排序的查询
 * @author lizx
 */
public abstract class SortableQueryCmd {
	/** 排序的字段名,可传多个,!开始的字段名为逆序，否则为正序 */
	private String[] o;

	public String[] getO() {
		return o;
	}

	public void setO(String... o) {
		this.o = o;
	}
	
}
