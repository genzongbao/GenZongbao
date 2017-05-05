package ydh.cicada.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ydh.cicada.dao.BaseDao;
import ydh.cicada.query.cond.Conditionable;
import ydh.cicada.service.CommonService;
import ydh.cicada.utils.EntityUtils;

/**
 * 数据库修改对象
 * @author lizx
 */
public class Updater<T> extends Conditionable<Updater<T>> {
	static final Logger log = LoggerFactory.getLogger(Updater.class);
	
	private Class<T> entityClass;
	private List<String> setters;
	private List<Object> values;
	private boolean forAll = false;

	protected Updater(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	public static <T> Updater<T> update(Class<T> entityClass) {
		return new Updater<T>(entityClass);
	}
	
	public Updater<T> put(String fieldName, Object value) {
		return set(fieldName + "=?", value);
	}
	
	public Updater<T> add(String fieldName, Number value) {
		return set(fieldName + "="+fieldName+"+?", value);
	}
	
	public Updater<T> minus(String fieldName, Number value) {
		return set(fieldName + "="+fieldName+"-?", value);
	}
	
	public Updater<T> set(String stmt, Object... values) {
		this._setters().add(stmt);
		for (Object value : values) {
			this._values().add(EntityUtils.strip(value));
		}
		return this;
	}
	
	public Updater<T> updateAll() {
		this.forAll = true;
		return this;
	}

	public Class<T> entityClass() {
		return this.entityClass;
	}
	
	public List<String> setters() {
		return Collections.unmodifiableList(this._setters());
	}
	
	public Object[] values() {
		return this._values().toArray();
	}

	public boolean forAll() {
		return this.forAll;
	}

	public int exec(BaseDao dao) {
		return dao.update(this);
	}

	public int exec(CommonService service) {
		return service.update(this);
	}

	private List<String> _setters() {
		if (this.setters == null) {
			this.setters = new ArrayList<String>();
		}
		return this.setters;
	}
	
	private List<Object> _values() {
		if (this.values == null) {
			this.values = new ArrayList<Object>();
		}
		return this.values;
	}
}
