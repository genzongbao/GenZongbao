package ydh.cicada.cached;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ydh.cicada.dao.BaseDao;
import ydh.cicada.dao.Page;
import ydh.cicada.query.QueryObject;

public class CachedQuery<T> {
	private String key;
	private QueryObject<T> queryObject;
	private int pageSize;
	private int expiry;
	private TimeUnit unit;
	
	public CachedQuery(String key, QueryObject<T> queryObject, int expiry, TimeUnit unit, int pageSize) {
		this.key = key;
		this.queryObject = queryObject;
		this.expiry = expiry;
		this.unit = unit;
		this.pageSize = pageSize;
	}
	
	public Class<T> entityClass() {
		return queryObject.entityClass();
	}
	
	public String key() {
		return key;
	}
	
	public int expiry() {
		return expiry;
	}
	
	public TimeUnit unit() {
		return unit;
	}
	
	public int pageSize() {
		return pageSize;
	}
	
	public List<T> list(BaseDao dao) {
		return this.queryObject.list(dao);
	}
	
	public Page<T> find(BaseDao dao, long page) {
		return this.queryObject.page(page).find(dao);		
	}
	
	public Page<T> find(BaseDao dao, long page, long total) {
		return this.queryObject.page(page).find(dao, total);
	}
}
