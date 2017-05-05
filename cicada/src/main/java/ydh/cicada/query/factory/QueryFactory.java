package ydh.cicada.query.factory;

import java.io.Serializable;
import java.util.List;

import ydh.cicada.query.Deleter;
import ydh.cicada.query.QueryObject;
import ydh.cicada.query.Updater;

/**
 * 查询对象工厂类，不同的数据库由各自的实现支持
 * @author lizx
 */
public interface QueryFactory {

	/**
	 * 通过具有查询注解的POJO对象生成查询对象
	 * @param cmd  POJO对象
	 * @return     查询对象
	 * @throws Exception
	 */
	public abstract <T> QueryObject<T> createQuery(Class<T> entityClass, Object cmd);

	/**
	 * 创建查询语句
	 * @param query
	 * @return
	 */
	public abstract String buildSql(List<Object> params, QueryObject<?> query, String... entityHashcodes);

	/**
	 * 创建统计数量的查询语句
	 * @param query
	 * @return
	 */
	public abstract String buildCountSql(List<Object> params, QueryObject<?> query, String... entityHashcodes);

	/**
	 * 创建分页查询语句
	 * @param query
	 * @return
	 */
	public abstract String buildPageSql(List<Object> params, QueryObject<?> query, String... entityHashcodes);

	/**
	 * 创建查询语句(仅查询前面指定数量的行)
	 * @param query
	 * @param top    数量限制
	 * @return
	 */
	public abstract String buildTopSql(List<Object> params, QueryObject<?> query, int top, String... entityHashcodes);

	/**
	 * 创建插入语句
	 * @param params
	 * @param obj
	 * @return
	 */
	public abstract String buildInsertSql(List<Object> params, Object obj);

	/**
	 * 创建更新语句
	 * @param params
	 * @param obj
	 * @return
	 */
	public abstract String buildUpdateSql(List<Object> params, Object obj);

	/**
	 * 创建更新语句
	 * @param params
	 * @param obj
	 * @return
	 */
	public abstract String buildUpdateSql(List<Object> params, Updater<?> updater, String entityHashcode);

	/**
	 * 创建删除语句
	 * @param params
	 * @param obj
	 * @return
	 */
	public abstract String buildDeleteSql(List<Object> params, Object obj);

	/**
	 * 创建删除语句
	 * @param params
	 * @param entityClass
	 * @param entityHashcode
	 * @param id
	 * @return
	 */
	public abstract String buildDeleteSql(List<Object> params, Class<?> entityClass, String entityHashcode, Serializable id);

	/**
	 * 创建删除语句
	 * @param params
	 * @param entityClass
	 * @param entityHashcode
	 * @param id
	 * @return
	 */
	public abstract String buildDeleteSql(List<Object> params, Deleter<?> deleter, String entityHashcode);

	/**
	 * 创建根据ID查询语句
	 * @param params
	 * @param obj
	 * @return
	 */
	public abstract String buildLoadSql(List<Object> params, Class<?> entityClass, String entityHashcode, Serializable id, boolean forUpdate);

}