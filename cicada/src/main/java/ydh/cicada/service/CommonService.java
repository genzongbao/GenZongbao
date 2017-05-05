package ydh.cicada.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ydh.cicada.dao.BaseDao;
import ydh.cicada.dao.Page;
import ydh.cicada.query.Deleter;
import ydh.cicada.query.QueryObject;
import ydh.cicada.query.Updater;

@Service
public class CommonService {

	@Autowired
	protected BaseDao dao;
	
	/**
     * 插入一个对象
     * @param entity    需保存的对象
     */
	@Transactional
    public void insert(final Object entity) {
    	dao.insert(entity);
    }
    
    /**
     * 修改一个对象
     * @param entity    需保存的对象
     */
	@Transactional
    public void update(final Object entity) {
    	dao.update(entity);
    }

	@Transactional
    public int update(final Updater<?> updater) {
    	return dao.update(updater);
    }
	
    /**
     * 删除一个对象
     * @param entity    需删除的对象
     */
	@Transactional
	public void delete(final Object entity) {
    	dao.delete(entity);
    }
	
    /**
     * 使用Deleter删除对象
     * @param deleter
     */
	@Transactional
    public int delete(Deleter<?> deleter) {
		return dao.delete(deleter);
	}

    /**
     * 取一个持久对象
     * @param entityClass   持久类
     * @param id            标识
     * @return  持久对象
     */
	@Transactional
	public <T> T load(final Class<T> entityClass, final Serializable id) {
    	return dao.load(entityClass, id);
    }

    /**
     * 执行查询语句,返回结果列表
     * @param entityClass  结果类型
     * @param query        查询对象（注解方式）
     * @return 结果列表
     */
	@Transactional
	public <T> List<T> list(final Class<T> entityClass, final Object query) {
    	return dao.list(entityClass, query);
    }
    
	/**
     * 执行查询语句,返回结果列表
     * @param query        查询对象
     * @return 结果列表
     */
	@Transactional
	public <T> List<T> list(final QueryObject<T> query) {
    	return dao.list(query);
    }
    
    /**
     * 执行查询语句,返回结果列表
     * @param entityClass  结果类型
     * @param query        查询对象（注解方式）
     * @param top          数量限制
     * @return 结果列表
     */
	@Transactional
	public <T> List<T> list(final Class<T> entityClass, final Object query, int top) {
    	return dao.list(entityClass, query, top);
    }
	
    /**
     * 执行查询语句,返回结果列表
     * @param query        查询对象
     * @param top          数量限制
     * @return 结果列表
     */
	@Transactional
	public <T> List<T> list(final QueryObject<T> query, int top) {
    	return dao.list(query, top);
    }

	/**
     * 分页查询
     * @param entityClass  结果类型
     * @param query   查询对象（注解方式）
     * @return  查询结果
     */
	@Transactional
	public <T> Page<T> find(final Class<T> entityClass, final Object query) {
    	return dao.find(entityClass, query);
    }
    
	/**
     * 分页查询
     * @param entityClass  结果类型
     * @param query   查询对象（注解方式）
     * @return  查询结果
     */
	@Transactional
	public <T> Page<T> find(final QueryObject<T> query) {
    	return dao.find(query);
    }

	/**
     * 统计符合条件的记录数量
     * @param query 查询对象
     * @return 符合条件的记录数量
     */
	@Transactional
	public long count(final Object query) {
    	return dao.count(query);
    }

	/**
     * 统计符合条件的记录数量
     * @param query 查询对象
     * @return 符合条件的记录数量
     */
	@Transactional
	public long count(final QueryObject<?> query) {
    	return dao.count(query);
    }

    /**
     * 查询符合条件的唯一记录,如果查询结果不唯一将抛出错误
     * @param entityClass  结果类型
     * @param query   查询对象（注解方式）
     * @return 符合条件的记录
     */
	@Transactional
	public <T> T uniqueResult(final Class<T> entityClass, final Object query) {
		return dao.uniqueResult(entityClass, query);
	}
    
    /**
     * 查询符合条件的唯一记录,如果查询结果不唯一将抛出错误
     * @param query   查询对象
     * @return 符合条件的记录
     */
	@Transactional
	public <T> T uniqueResult(final QueryObject<T> query) {
		return dao.uniqueResult(query);
	}

	/**
     * 查询符合条件的第一条记录，没找到就返回空
     * @param entityClass  结果类型
     * @param query   查询对象（注解方式）
     * @return 符合条件的第一条记录
     */
	@Transactional
	public <T> T firstResult(final Class<T> entityClass, final Object query) {
		return dao.firstResult(entityClass, query);
	}

	/**
     * 查询符合条件的第一条记录，没找到就返回空
     * @param query   查询对象
     * @return 符合条件的第一条记录
     */
	@Transactional
	public <T> T firstResult(final QueryObject<T> query) {
		return dao.firstResult(query);
	}
	
}
