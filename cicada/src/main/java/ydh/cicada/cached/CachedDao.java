package ydh.cicada.cached;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import ydh.cicada.dao.SplitableDao;

/**
 * 具备缓存能力的数据访问对象
 * 是否缓存，缓存时间由实体对象的@Cached注解定义
 * 缓存条件有实体对象实现CachedCondition接口进行定义
 * @author lizx
 */
public interface CachedDao extends SplitableDao {

	public abstract SplitableDao nocached();
	
	public abstract Object cacheGet(String key);
	public abstract void cacheDelete(String key);
	public abstract void cacheSet(String key, Object entity, int expiry, TimeUnit unit);
	public <T> T cacheGet(Class<T> entityClass, Serializable id);
	public void cacheDelete(Class<?> entityClass, Serializable id);
	public void cacheAll(Collection<?> list);
	public void cacheSet(Object entity);
	public void cacheDelete(Object entity);
	
}