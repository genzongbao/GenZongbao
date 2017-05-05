package ydh.cicada.query.cond;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Conditionable<P extends Conditionable<P>> {
	private List<Cond<P>> conditions;
	private boolean or; // 是否使用or连接各个条件

	/**
	 * 指定查询条件
	 * @param field  查询条件对应字段
	 * @return 查询条件对象
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Cond<P> cond(String field) {
		return new Cond(this, field);
	}
	
	/**
	 * 指定查询条件
	 * @param field
	 * @param alias
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Cond<P> cond(String field, String alias) {
		return new Cond(this, field, alias);
	}

	protected List<Cond<P>> _conditions() {
		if (this.conditions == null) {
			this.conditions = new ArrayList<Cond<P>>();
		}
		return this.conditions;
	}

	/**
	 * @return 所有查询条件的列表
	 */
	public List<Cond<P>> conditions() {
		return Collections.unmodifiableList(this._conditions());
	}

	/**
	 * 指定各个查询条件之间的连接方式
	 * @param or  true:使用or连接，否则默认使用and连接
	 * @return 查询对象本身
	 */
	@SuppressWarnings("unchecked")
	public P or(boolean or) {
		this.or = or;
		return (P)this;
	}

	/**
	 * @return 查询条件的连接方式
	 */
	public boolean or() {
		return this.or;
	}

	public P condition(String stmt, Object... params) {
		return this.cond(stmt).op(null, params);
	}
}
