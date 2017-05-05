/*
 * Copyright: SBT (c) 2015
 * Company: 成都盛比特信息技术有限公司
 */
package ydh.cicada.query;


/**
 * 
 * @version 1.0
 * @author lizx
 * @created  2015年1月14日
 */
public abstract class PagableQueryCmd extends SortableQueryCmd {
	private Integer p; // page
	private Integer s; // page size
	
	public Integer getP() {
		return p;
	}
	public void setP(Integer p) {
		this.p = p;
	}
	public Integer getS() {
		return s;
	}
	public void setS(Integer s) {
		this.s = s;
	}	
}
