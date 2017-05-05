package ydh.cicada.query.cond;

import ydh.cicada.query.api.QueryOperator;

public class Cond<P extends Conditionable<P>> {
	private final P conditionable;
	private final String field;
	private final String alias;
	private QueryOperator op;
	private Object param;
	
	public Cond(P conditionable, String field) {
		this.conditionable = conditionable;
		this.field = field;
		this.alias = null;
	}
	
	public Cond(P conditionable, String field, String alias) {
		this.conditionable = conditionable;
		this.field = field;
		this.alias = alias;
	}
	
	public String field(){
		return this.alias == null ? this.field : this.alias + "." + this.field;
	}

	public QueryOperator op() {
		return this.op;
	}
	
	public Object param() {
		return this.param;
	}
	
	public P op(QueryOperator op, Object value) {
		this.op = op;
		this.param = value;
		this.conditionable._conditions().add(this);
		return conditionable;
	}
	
	public P equ(Object value) {
		return op(QueryOperator.EQU, value);
	}
	public P gt(Object value) {
		return op(QueryOperator.GT, value);
	}
	public P gtEqu(Object value) {
		return op(QueryOperator.GT_EQU, value);
	}
	public P less(Object value) {
		return op(QueryOperator.LESS, value);
	}
	public P lessEqu(Object value) {
		return op(QueryOperator.LESS_EQU, value);
	}
	public P like(Object value) {
		return op(QueryOperator.LIKE, value);
	}
	public P leftLike(Object value) {
		return op(QueryOperator.LEFTLIKE, value);
	}
	public P rightLike(Object value) {
		return op(QueryOperator.RIGHTLIKE, value);
	}
	public P notEqu(Object value) {
		return op(QueryOperator.NOT_EQU, value);
	}
	public P in(Object... value) {
		return op(QueryOperator.IN, value);
	}
	public P notIn(Object... value) {
		return op(QueryOperator.NOT_IN, value);
	}
	public P isNull() {
		return op(QueryOperator.IS_NULL, null);
	}
	public P isNotNull() {
		return op(QueryOperator.IS_NOT_NULL, null);
	}
}