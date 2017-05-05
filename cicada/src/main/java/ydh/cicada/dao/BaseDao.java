/*
 * Copyright: SBT (c) 2015
 * Company: 成都盛比特信息技术有限公司
 */
package ydh.cicada.dao;

import java.io.Serializable;
import java.util.List;

import ydh.cicada.query.Deleter;
import ydh.cicada.query.QueryObject;
import ydh.cicada.query.Updater;

/**
 * 基本的查询DAO对象接口
 * @author lizx
 *
 */
public interface BaseDao {

    /**
     * 插入一个对象
     * @param entity    需保存的对象
     */
    void insert(final Object entity);
    
    /**
     * 修改一个对象
     * @param entity    需保存的对象
     */
    void update(final Object entity);
    
    /**
     * 使用Updater修改一个对象
     * @param updater
     */
    int update(Updater<?> updater);
    
    /**
     * 删除一个对象
     * @param entity    需删除的对象
     */
    void delete(final Object entity);

    /**
     * 删除一个对象
     * @param entityClass    删除的实体类型
     * @param id             删除的对象ID
     */
    void delete(final Class<?> entityClass, final Serializable id);

    /**
     * 使用Updater修改一个对象
     * @param updater
     */
    int delete(Deleter<?> deleter);
    
    /**
     * 取一个持久对象
     * @param entityClass   持久类
     * @param id            标识
     * @param forUpdate     锁定
     * @return  持久对象
     */
    <T> T load(final Class<T> entityClass, final Serializable id);
    
    /**
     * 取一个持久对象并锁定
     * @param entityClass   持久类
     * @param id            标识
     * @return  持久对象
     */
    <T> T loadForUpdate(final Class<T> entityClass, final Serializable id);

    /**
     * 执行查询语句,返回结果列表
     * @param entityClass  结果类型
     * @param query        查询对象（注解方式）
     * @return 结果列表
     */
    <T> List<T> list(final Class<T> entityClass, final Object query);

    /**
     * 执行查询语句,返回结果列表,限制最大条数
     * @param entityClass  结果类型
     * @param query        查询对象（注解方式）
     * @param top 	       条数限制
     * @return 结果列表
     */
    <T> List<T> list(final Class<T> entityClass, final Object query, int top);

    /**
     * 执行查询语句,返回结果列表
     * @param query        查询对象
     * @return 结果列表
     */
    <T> List<T> list(final QueryObject<T> query);
    
    /**
     * 执行查询语句,返回结果列表,限制最大条数
     * @param query        查询对象
     * @param top 	       条数限制
     * @return 结果列表
     */
    <T> List<T> list(final QueryObject<T> query, int top);
    
    /**
     * 分页查询
     * @param entityClass  结果类型
     * @param query   查询对象（注解方式）
     * @return  查询结果
     */
    <T> Page<T> find(final Class<T> entityClass, final Object query);
    
    /**
     * 分页查询
     * @param query   查询对象
     * @return  查询结果
     */
    <T> Page<T> find(final QueryObject<T> query);
    
    /**
     * 快速分页查询（已知查询总数,再次查询后续页面时，不再次统计总数）
     * @param query   查询对象
     * @param total   已知的查询记录总数
     * @return  查询结果
     */
    <T> Page<T> find(final QueryObject<T> query, long total);
    
    /**
     * 统计符合条件的记录数量
     * @param query 查询对象
     * @return 符合条件的记录数量
     */
    long count(final Object query);
    
    /**
     * 统计符合条件的记录数量
     * @param query 查询对象
     * @return 符合条件的记录数量
     */
    long count(final QueryObject<?> query);
    
    /**
     * 查询符合条件的唯一记录,如果查询结果不唯一将抛出错误
     * @param entityClass  结果类型
     * @param query   查询对象（注解方式）
     * @return 符合条件的记录
     */
    <T> T uniqueResult(final Class<T> entityClass, final Object query);
    
    /**
     * 查询符合条件的唯一记录,如果查询结果不唯一将抛出错误
     * @param query   查询对象
     * @return 符合条件的记录
     */
    <T> T uniqueResult(final QueryObject<T> query);
    
    /**
     * 查询符合条件的第一条记录，没找到就返回空
     * @param entityClass  结果类型
     * @param query   查询对象（注解方式）
     * @return 符合条件的第一条记录
     */
    <T> T firstResult(final Class<T> entityClass, final Object query);

    /**
     * 查询符合条件的第一条记录，没找到就返回空
     * @param query   查询对象
     * @return 符合条件的第一条记录
     */
    <T> T firstResult(final QueryObject<T> query);
    
}
