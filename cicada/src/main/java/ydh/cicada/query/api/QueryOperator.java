/*
 * Copyright: SBT (c) 2015
 * Company: 成都盛比特信息技术有限公司
 */
package ydh.cicada.query.api;


/**
 * @author lzx
 */
public enum QueryOperator {
    EQU("="), GT(">"), GT_EQU(">="), LESS("<"), LESS_EQU("<="), 
    LIKE("like"), LEFTLIKE("like"), RIGHTLIKE("like"), IN("in"), NOT_EQU("<>"), NOT_IN("not in"),
    IS_NULL("is null"), IS_NOT_NULL("is not null");
    
    private String symbol;
    private QueryOperator(String symbol) {
        this.symbol = symbol;
    }
    
    public String getSymbol() {
        return this.symbol;
    }

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}
