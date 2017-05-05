package ydh.cicada.cached;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ydh.cicada.api.Cached;
import ydh.cicada.dao.Page;
import ydh.cicada.dao.SplitableDao;
import ydh.cicada.query.Deleter;
import ydh.cicada.query.QueryObject;
import ydh.cicada.query.Updater;
import ydh.cicada.utils.EntityUtils;

public abstract class AbstractCachedDao implements CachedDao {
	static final Logger log = LoggerFactory.getLogger(AbstractCachedDao.class);
	
	private SplitableDao dao;

	protected AbstractCachedDao(SplitableDao dao) {
		this.dao = dao;
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.cached.CachedDao#nocached()
	 */
	@Override
	public SplitableDao nocached() {
		return dao;
	}
	
	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#insert(java.lang.Object)
	 */
	@Override
	public void insert(Object entity) {
		dao.insert(entity);
		cacheSet(entity);
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#update(java.lang.Object)
	 */
	@Override
	public void update(Object entity) {
		dao.update(entity);
		cacheSet(entity);
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#update(ydh.cicada.query.Updater)
	 */
	@Override
	public int update(Updater<?> updater) {
		return update(updater, null);
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.SplitableDao#update(ydh.cicada.query.Updater, java.lang.String)
	 */
	@Override
	public int update(Updater<?> updater, String entityHashcode) {
		int result = dao.update(updater, entityHashcode);
		Cached cached = updater.entityClass().getAnnotation(Cached.class);
		if (cached != null) {
			log.warn("Updater is not security for cached data: "+updater.entityClass());
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#delete(java.lang.Object)
	 */
	@Override
	public void delete(Object entity) {
		Cached cached = entity.getClass().getAnnotation(Cached.class);
		if (cached != null) {
			cacheDelete(entity);
		}
		dao.delete(entity);
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#delete(java.lang.Class, java.io.Serializable)
	 */
	@Override
	public void delete(Class<?> entityClass, Serializable id) {
		delete(entityClass, null, id);
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.SplitableDao#delete(java.lang.Class, java.lang.String, java.io.Serializable)
	 */
	@Override
	public void delete(Class<?> entityClass, String entityHashcode, Serializable id) {
		Cached cached = entityClass.getAnnotation(Cached.class);
		if (cached != null) {
			cacheDelete(EntityUtils.keyOf(entityClass, id));
		}
		dao.delete(entityClass, entityHashcode, id);
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.SplitableDao#delete(ydh.cicada.query.Deleter, java.lang.String)
	 */
	@Override
	public int delete(Deleter<?> deleter, String entityHashcode) {
		int result = dao.delete(deleter, entityHashcode);
		Cached cached = deleter.entityClass().getAnnotation(Cached.class);
		if (cached != null) {
			log.warn("Deleter is not security for cached data: "+deleter.entityClass());
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#delete(ydh.cicada.query.Deleter)
	 */
	@Override
	public int delete(Deleter<?> deleter) {
		return delete(deleter, null);
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#load(java.lang.Class, java.io.Serializable)
	 */
	@Override
	public <T> T load(Class<T> entityClass, Serializable id) {
		return load(entityClass, null, id);
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.SplitableDao#load(java.lang.Class, java.lang.String, java.io.Serializable)
	 */
	@Override
	public <T> T load(Class<T> entityClass, String entityHashcode, Serializable id) {
		Cached cached = entityClass.getAnnotation(Cached.class);
		if (cached != null) {
			T obj = (T)cacheGet(entityClass, id);
			if (obj == null) {
				obj = dao.load(entityClass, entityHashcode, id);
				if (cachable(obj)) {
					cacheSet(obj);
				}
			}
			return obj;
		} else {
			return dao.load(entityClass, entityHashcode, id);
		}
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#loadForUpdate(java.lang.Class, java.io.Serializable)
	 */
	@Override
	public <T> T loadForUpdate(Class<T> entityClass, Serializable id) {
		return loadForUpdate(entityClass, null, id);
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.SplitableDao#loadForUpdate(java.lang.Class, java.lang.String, java.io.Serializable)
	 */
	@Override
	public <T> T loadForUpdate(Class<T> entityClass, String entityHashcode, Serializable id) {
		Cached cached = entityClass.getAnnotation(Cached.class);
		if (cached != null) {
			/*数据库加锁的记录，不使用缓存数据*/
			T obj = dao.loadForUpdate(entityClass, entityHashcode, id);
			cacheSet(obj);
			return obj;
		} else {
			return dao.loadForUpdate(entityClass, entityHashcode, id);
		}
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#list(java.lang.Class, java.lang.Object)
	 */
	@Override
	public <T> List<T> list(Class<T> entityClass, Object query) {
		return list(entityClass, query, (String[])null);
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.SplitableDao#list(java.lang.Class, java.lang.Object, java.lang.String[])
	 */
	@Override
	public <T> List<T> list(Class<T> entityClass, Object query, String... entityHashcodes) {
		List<T> list = dao.list(entityClass, query, entityHashcodes);
		cacheAll(list);
		return list;
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#list(java.lang.Class, java.lang.Object, int)
	 */
	@Override
	public <T> List<T> list(Class<T> entityClass, Object query, int top) {
		List<T> list = dao.list(entityClass, query, top);
		cacheAll(list);
		return list;
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#list(ydh.cicada.query.QueryObject)
	 */
	@Override
	public <T> List<T> list(QueryObject<T> query) {
		List<T> list = dao.list(query);
		cacheAll(list);
		return list;
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#list(ydh.cicada.query.QueryObject, int)
	 */
	@Override
	public <T> List<T> list(QueryObject<T> query, int top) {
		List<T> list = dao.list(query, top);
		cacheAll(list);
		return list;
	}


	/* (non-Javadoc)
	 * @see ydh.cicada.dao.SplitableDao#list(java.lang.Class, java.lang.Object, int, java.lang.String[])
	 */
	@Override
	public <T> List<T> list(Class<T> entityClass, Object query, int top,
			String... entityHashcodes) {
		List<T> list = dao.list(entityClass, query, top, entityHashcodes);
		cacheAll(list);
		return list;
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.SplitableDao#list(ydh.cicada.query.QueryObject, java.lang.String[])
	 */
	@Override
	public <T> List<T> list(QueryObject<T> query, String... entityHashcodes) {
		List<T> list = dao.list(query, entityHashcodes);
		cacheAll(list);
		return list;
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.SplitableDao#list(ydh.cicada.query.QueryObject, int, java.lang.String[])
	 */
	@Override
	public <T> List<T> list(QueryObject<T> query, int top, String... entityHashcodes) {
		List<T> list = dao.list(query, top, entityHashcodes);
		cacheAll(list);
		return list;
	}

	@Override
	public <T> Page<T> find(Class<T> entityClass, Object query) {
		Page<T> page = dao.find(entityClass, query);
		cacheAll(page.getData());
		return page;
	}

	@Override
	public <T> Page<T> find(QueryObject<T> query) {
		Page<T> page = dao.find(query);
		cacheAll(page.getData());
		return page;
	}

	@Override
	public <T> Page<T> find(QueryObject<T> query, long total) {
		Page<T> page = dao.find(query, total);
		cacheAll(page.getData());
		return page;
	}

	@Override
	public <T> Page<T> find(QueryObject<T> query, String... entityHashcodes) {
		Page<T> page = dao.find(query, entityHashcodes);
		cacheAll(page.getData());
		return page;
	}

	@Override
	public <T> Page<T> find(QueryObject<T> query, long total,
			String... entityHashcodes) {
		Page<T> page = dao.find(query, total, entityHashcodes);
		cacheAll(page.getData());
		return page;
	}

	@Override
	public <T> Page<T> find(Class<T> entityClass, Object query, String... entityHashcodes) {
		Page<T> page = dao.find(entityClass, query, entityHashcodes);
		cacheAll(page.getData());
		return page;
	}

	@Override
	public long count(Object query) {
		return dao.count(query);
	}

	@Override
	public long count(QueryObject<?> query) {
		return dao.count(query);
	}

	@Override
	public long count(Object query, String... entityHashcodes) {
		return dao.count(query, entityHashcodes);
	}

	@Override
	public long count(QueryObject<?> query, String... entityHashcodes) {
		return dao.count(query, entityHashcodes);
	}

	@Override
	public <T> T uniqueResult(Class<T> entityClass, Object query) {
		T obj = dao.uniqueResult(entityClass, query);
		cacheSet(obj);
		return obj;
	}

	@Override
	public <T> T uniqueResult(QueryObject<T> query) {
		T obj = dao.uniqueResult(query);
		cacheSet(obj);
		return obj;
	}

	@Override
	public <T> T uniqueResult(Class<T> entityClass, Object query, String... entityHashcodes) {
		T obj = dao.uniqueResult(entityClass, query, entityHashcodes);
		cacheSet(obj);
		return obj;
	}

	@Override
	public <T> T uniqueResult(QueryObject<T> query, String... entityHashcodes) {
		T obj = dao.uniqueResult(query, entityHashcodes);
		cacheSet(obj);
		return obj;
	}

	@Override
	public <T> T firstResult(Class<T> entityClass, Object query) {
		T obj = dao.firstResult(entityClass, query);
		cacheSet(obj);
		return obj;
	}

	@Override
	public <T> T firstResult(QueryObject<T> query) {
		T obj = dao.firstResult(query);
		cacheSet(obj);
		return obj;
	}
	

	@Override
	public <T> T firstResult(Class<T> entityClass, Object query, String... entityHashcodes) {
		T obj = dao.firstResult(entityClass, query, entityHashcodes);
		cacheSet(obj);
		return obj;
	}

	@Override
	public <T> T firstResult(QueryObject<T> query, String... entityHashcodes) {
		T obj = dao.firstResult(query, entityHashcodes);
		cacheSet(obj);
		return obj;
	}

	public <T> List<T> list(CachedQuery<T> cachedQuery) {
		@SuppressWarnings("unchecked")
		List<T> list = (List<T>)cacheGet(cachedQuery.key());
		if (list == null) {
			list = cachedQuery.list(dao);
			cacheSet(cachedQuery.key(), list, cachedQuery.expiry(), cachedQuery.unit());
		}
		return list;
	}
	
	public void deleteQueryCache(CachedQuery<?> cachedQuery) {
		String key = cachedQuery.key();
		String totalKey = key + ":total";
		Long total = (Long)cacheGet(totalKey);
		if (total != null) {
			for (int i = 0; i < (total + cachedQuery.pageSize() - 1) / cachedQuery.pageSize(); i++) {
				cacheDelete(key + ":" + (i+1));
			}
			cacheDelete(totalKey);
		}
	}

	public <T> Page<T> find(CachedQuery<T> cachedQuery, long page) {
		String totalKey = cachedQuery.key() + ":total";
		String pageKey = cachedQuery.key() + ":" + page;
		Long total = (Long)cacheGet(totalKey);
		if (total == null) {
			Page<T> result = cachedQuery.find(dao, page);
			total = result.getTotalSize();
			cacheSet(totalKey, total, cachedQuery.expiry(), cachedQuery.unit());
			cacheSet(pageKey, result.getData(), cachedQuery.expiry(), cachedQuery.unit());
			return result;
		}
		@SuppressWarnings("unchecked")
		List<T> list = (List<T>)cacheGet(pageKey);
		if(list == null) {
			Page<T> result = cachedQuery.find(dao, page, total);
			list = result.getData();
		}
		cacheSet(totalKey, total, cachedQuery.expiry(), cachedQuery.unit());
		cacheSet(pageKey, list, cachedQuery.expiry(), cachedQuery.unit());
		return new Page<T>(total, list, page, cachedQuery.pageSize());
	}

	protected boolean cachable(Object obj) {
		if (obj instanceof CachedCondition) {
			CachedCondition cond = (CachedCondition)obj;
			return cond.cachable();
		} else {
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T cacheGet(Class<T> entityClass, Serializable id) {
		return (T)cacheGet(EntityUtils.keyOf(entityClass, id));
	}

	public void cacheDelete(Class<?> entityClass, Serializable id) {
		cacheDelete(EntityUtils.keyOf(entityClass, id));
	}
	
	public void cacheAll(Collection<?> entities) {
		for (Object entity : entities) {
			cacheSet(entity);
		}
	}
	
	public void cacheSet(Object entity) {
		Cached cached = entity.getClass().getAnnotation(Cached.class);
		if (cached != null) {
			if (entity instanceof CachedCondition) {
				if (((CachedCondition)entity).cachable()) {
					cacheSet(EntityUtils.keyOf(entity), entity, cached.expiry(), cached.unit());
				}
			} else {
				cacheSet(EntityUtils.keyOf(entity), entity, cached.expiry(), cached.unit());					
			}
		}
	}

	public void cacheDelete(Object entity) {
		cacheDelete(entity.getClass(), EntityUtils.idOf(entity));
	}


}
