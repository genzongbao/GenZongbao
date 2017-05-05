package ydh.cicada.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ydh.cicada.dao.BaseDao;
import ydh.cicada.query.cond.Conditionable;
import ydh.cicada.service.CommonService;

/**
 * 数据库删除对象
 * @author lizx
 */
public class Deleter<T> extends Conditionable<Deleter<T>> {
	static final Logger log = LoggerFactory.getLogger(Deleter.class);
	
	private Class<T> entityClass;
	private boolean forAll = false;
	private boolean or; // 是否使用or连接各个条件

	protected Deleter(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	public static <T> Deleter<T> delete(Class<T> entityClass) {
		return new Deleter<T>(entityClass);
	}

	public Deleter<T> deleteAll() {
		this.forAll = true;
		return this;
	}

	public Deleter<T> or(boolean or) {
		this.or = or;
		return this;
	}
	
	public Class<T> entityClass() {
		return this.entityClass;
	}

	public boolean forAll() {
		return this.forAll;
	}
	
	public boolean or() {
		return this.or;
	}

	public int exec(BaseDao dao) {
		return dao.delete(this);
	}

	public int exec(CommonService service) {
		return service.delete(this);
	}
	
}
