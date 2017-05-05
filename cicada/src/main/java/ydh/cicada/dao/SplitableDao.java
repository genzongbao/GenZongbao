package ydh.cicada.dao;

import java.io.Serializable;
import java.util.List;

import ydh.cicada.query.Deleter;
import ydh.cicada.query.QueryObject;
import ydh.cicada.query.Updater;

/**
 * 对拆分表的查询DAO
 * @author lizx
 */
public interface SplitableDao extends BaseDao {

    /**
     * 使用Updater修改一个对象
     * @param updater
     */
    int update(Updater<?> updater, String entityHashcode);
    
    /**
     * 取一个持久对象
     * @param entityClass    实体类
     * @param entityHashcode 实体的分表Hash值
     * @param id             标识
     * @param forUpdate      锁定
     * @return  持久对象
     */
    <T> T load(final Class<T> entityClass, String entityHashcode, final Serializable id);

    /**
     * 取一个持久对象并锁定
     * @param entityClass    实体类
     * @param entityHashcode 实体的分表Hash值
     * @param id             标识
     * @return  持久对象
     */
    <T> T loadForUpdate(final Class<T> entityClass, String entityHashcode, final Serializable id);

    /**
     * 删除一个对象
     * @param entityClass    删除的实体类型
     * @param entityHashcode 删除实体的分表Hash值
     * @param id             删除的对象ID
     */
    void delete(final Class<?> entityClass, String entityHashcode, final Serializable id);
    
    /**
     * 使用Deleter删除对象
     * @param deleter        
     * @param entityHashcode 删除实体的分表Hash值
     */
    public int delete(Deleter<?> deleter, String entityHashcode);
    
    /**
     * 执行查询语句,返回结果列表
     * @param entityClass     结果类型
     * @param query           查询对象（注解方式）
     * @param entityHashcodes 实体的分表Hash值
     * @return 结果列表
     */
    <T> List<T> list(final Class<T> entityClass, final Object query, String... entityHashcodes);
    /**
     * 执行查询语句,返回结果列表
     * @param entityClass     结果类型
     * @param query           查询对象（注解方式）
     * @param top 	          条数限制
     * @param entityHashcodes 实体的分表Hash值
     * @return 结果列表
     */
    <T> List<T> list(final Class<T> entityClass, final Object query, int top, String... entityHashcodes);
    /**
     * 执行查询语句,返回结果列表
     * @param query        查询对象
     * @param entityHashcodes 实体的分表Hash值
     * @return 结果列表
     */
    <T> List<T> list(final QueryObject<T> query, String... entityHashcodes);
    
    /**
     * 执行查询语句,返回结果列表
     * @param query        查询对象
     * @param top 	          条数限制
     * @param entityHashcodes 实体的分表Hash值
     * @return 结果列表
     */
    <T> List<T> list(final QueryObject<T> query, int top, String... entityHashcodes);
    

    /**
     * 分页查询
     * @param entityClass  结果类型
     * @param query   查询对象（注解方式）
     * @param entityHashcodes 分表hashcode
     * @return  查询结果
     */
    <T> Page<T> find(final Class<T> entityClass, final Object query, String... entityHashcodes);
    
    /**
     * 分页查询
     * @param query   查询对象
     * @return  查询结果
     */
    <T> Page<T> find(final QueryObject<T> query, String... entityHashcodes);
    
    /**
     * 快速分页查询（已知查询总数,再次查询后续页面时，不再次统计总数）
     * 支持分表查询，传入对应分表的hashcode替换from语句中的｛1｝～｛9｝参数
     * @param query  查询对象
     * @param total  记录总数
     * @param entityHashcodes 分表hashcode
     * @return 查询结果
     */
    <T> Page<T> find(final QueryObject<T> query, long total, String... entityHashcodes);


    /**
     * 统计符合条件的记录数量
     * @param query 查询对象
     * @return 符合条件的记录数量
     */
    long count(final Object query, String... entityHashcodes);
    
    /**
     * 统计符合条件的记录数量
     * @param query 查询对象
     * @return 符合条件的记录数量
     */
    long count(final QueryObject<?> query, String... entityHashcodes);
    

    /**
     * 查询符合条件的唯一记录,如果查询结果不唯一将抛出错误
     * @param entityClass  结果类型
     * @param query   查询对象（注解方式）
     * @return 符合条件的记录
     */
    <T> T uniqueResult(final Class<T> entityClass, final Object query, String... entityHashcodes);
    
    /**
     * 查询符合条件的唯一记录,如果查询结果不唯一将抛出错误
     * @param query   查询对象
     * @return 符合条件的记录
     */
    <T> T uniqueResult(final QueryObject<T> query, String... entityHashcodes);
    
    /**
     * 查询符合条件的第一条记录，没找到就返回空
     * @param entityClass  结果类型
     * @param query   查询对象（注解方式）
     * @return 符合条件的第一条记录
     */
    <T> T firstResult(final Class<T> entityClass, final Object query, String... entityHashcodes);

    /**
     * 查询符合条件的第一条记录，没找到就返回空
     * @param query   查询对象
     * @return 符合条件的第一条记录
     */
    <T> T firstResult(final QueryObject<T> query, String... entityHashcodes);
    
}
