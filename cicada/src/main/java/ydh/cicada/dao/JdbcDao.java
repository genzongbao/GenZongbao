/*
 * Copyright: SBT (c) 2015
 * Company: 成都盛比特信息技术有限公司
 */
package ydh.cicada.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import ydh.cicada.query.Deleter;
import ydh.cicada.query.QueryObject;
import ydh.cicada.query.Updater;
import ydh.cicada.query.factory.QueryFactory;
import ydh.cicada.utils.EntityUtils;

/**
 * 基于JdbcTemplate的DAO实现
 * @author lizx
 */
public class JdbcDao implements SplitableDao {
	static final Logger log = LoggerFactory.getLogger(JdbcDao.class);
	
	private JdbcTemplate jdbc;
	private QueryFactory queryFactory;

	public JdbcDao(JdbcTemplate jdbc, QueryFactory queryFactory) {
		this.jdbc = jdbc;
		this.queryFactory = queryFactory;
	}
	
	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#insert(java.lang.Object)
	 */
	@Override
	public void insert(Object entity) {
		Assert.notNull(entity, "entity must not be null");
		
		List<Object> params = new ArrayList<Object>();
		String sql = queryFactory.buildInsertSql(params, entity);
		log.error(sql);
		log.error(params.toString());
		try{
			int count = jdbc.update(sql, params.toArray());
			Assert.isTrue(count == 1);
			if (EntityUtils.isAutoincrement(entity.getClass())) {
				Serializable id = jdbc.queryForObject("select last_insert_id()", Serializable.class);
				EntityUtils.setId(entity, id);
			}
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#update(java.lang.Object)
	 */
	@Override
	public void update(Object entity) {
		Assert.notNull(entity, "entity must not be null");
		
		List<Object> params = new ArrayList<Object>();
		String sql = queryFactory.buildUpdateSql(params, entity);
		log.error(sql);
		log.error(params.toString());
		int result = jdbc.update(sql, params.toArray());
		Assert.isTrue(result == 1);
	}
	
	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#update(ydh.cicada.query.Updater)
	 */
	public int update(Updater<?> updater) {
		return update(updater, null);
	}
	
	/* (non-Javadoc)
	 * @see ydh.cicada.dao.SplitableDao#update(ydh.cicada.query.Updater, java.lang.String)
	 */
	public int update(Updater<?> updater, String entityHashcode) {
		Assert.notNull(updater, "updater must not be null");
		Assert.isTrue(updater.forAll() || ! updater.conditions().isEmpty(), 
				"Updater is not allow update without conditions. "
				+ "Call updater.updateAll() if you need indeed.");
		List<Object> params = new ArrayList<Object>();
		String sql = queryFactory.buildUpdateSql(params, updater, entityHashcode);
		log.error(sql);
		log.error(params.toString());
		return jdbc.update(sql, params.toArray());
	}
	
	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#delete(java.lang.Object)
	 */
	@Override
	public void delete(Object entity) {
		Assert.notNull(entity, "entity must not be null");
		
		List<Object> params = new ArrayList<Object>();
		String sql = queryFactory.buildDeleteSql(params, entity);
		log.error(sql);
		log.error(params.toString());
		jdbc.update(sql, params.toArray());
	}
	
	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#delete(java.lang.Class, java.io.Serializable)
	 */
	@Override
	public void delete(final Class<?> entityClass, final Serializable id) {
		delete(entityClass, null, id);
	}
	
	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#delete(java.lang.Class, java.lang.Integer, java.io.Serializable)
	 */
	@Override
	public void delete(final Class<?> entityClass, final String entityHashcode, final Serializable id) {
		Assert.notNull(entityClass, "entityClass must not be null");
		Assert.notNull(id, "id must not be null");
		
		List<Object> params = new ArrayList<Object>();
		String sql = queryFactory.buildDeleteSql(params, entityClass, entityHashcode, id);
		log.error(sql);
		log.error(params.toString());
		jdbc.update(sql, params.toArray());
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#delete(ydh.cicada.query.Deleter)
	 */
	@Override
	public int delete(Deleter<?> deleter) {
		return delete(deleter, null);
	}
			
	/* (non-Javadoc)
	 * @see ydh.cicada.dao.SplitableDao#delete(ydh.cicada.query.Deleter, java.lang.String)
	 */
	@Override
	public int delete(Deleter<?> deleter, String entityHashcode) {
		Assert.notNull(deleter, "deleter must not be null");
		Assert.isTrue(deleter.forAll() || ! deleter.conditions().isEmpty(), 
				"Deleter is not allow delete without conditions. "
				+ "Call deleter.deleteAll() if you need indeed.");
		List<Object> params = new ArrayList<Object>();
		String sql = queryFactory.buildDeleteSql(params, deleter, entityHashcode);
		log.error(sql);
		log.error(params.toString());
		return jdbc.update(sql, params.toArray());
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
		Assert.notNull(entityClass, "entityClass must not be null");
		Assert.notNull(id, "id must not be null");

		List<Object> params = new ArrayList<Object>();
		String sql = queryFactory.buildLoadSql(params, entityClass, entityHashcode, id, false);
		CicadaRowMapper<T> rowMapper = new CicadaRowMapper<T>(entityClass);
		log.error(sql);
		log.error(params.toString());
		return jdbc.queryForObject(sql, rowMapper, params.toArray());
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#load(java.lang.Class, java.io.Serializable)
	 */
	@Override
	public <T> T loadForUpdate(Class<T> entityClass, Serializable id) {
		return loadForUpdate(entityClass, null, id);
	}

	@Override
	public <T> T loadForUpdate(Class<T> entityClass, String entityHashcode, Serializable id) {
		Assert.notNull(entityClass, "entityClass must not be null");
		Assert.notNull(id, "id must not be null");

		List<Object> params = new ArrayList<Object>();
		String sql = queryFactory.buildLoadSql(params, entityClass, entityHashcode, id, true);
		CicadaRowMapper<T> rowMapper = new CicadaRowMapper<T>(entityClass);
		log.error(sql);
		log.error(params.toString());
		return jdbc.queryForObject(sql, rowMapper, params.toArray());
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#list(java.lang.Class, java.lang.Object)
	 */
	@Override
	public <T> List<T> list(Class<T> entityClass, Object query) {
		Assert.notNull(entityClass, "entityClass must not be null");
		Assert.notNull(query, "query must not be null");

		return this.list(queryFactory.createQuery(entityClass, query));
	}
	
	/* (non-Javadoc)
	 * @see ydh.cicada.dao.SplitableDao#list(java.lang.Class, java.lang.Object, java.lang.String[])
	 */
	@Override
	public <T> List<T> list(Class<T> entityClass, Object query, String... entityHashcodes) {
		Assert.notNull(entityClass, "entityClass must not be null");
		Assert.notNull(query, "query must not be null");

		return this.list(queryFactory.createQuery(entityClass, query), entityHashcodes);
	}
	
	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#list(ydh.cicada.query.QueryObject)
	 */
	@Override
	public <T> List<T> list(final QueryObject<T> query) {
		return list(query, (String[])null);
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#list(ydh.cicada.query.QueryObject)
	 */
	@Override
	public <T> List<T> list(final QueryObject<T> query, String... entityHashcodes) {
		Assert.notNull(query, "query must not be null");
		List<Object> params = new ArrayList<Object>();
		String sql = queryFactory.buildSql(params, query, entityHashcodes);
		log.error(sql);
		log.error(params.toString());
		return this.query(query.entityClass(), sql, params);
	}

	@Override
	public <T> List<T> list(Class<T> entityClass, Object query, int top) {
		return list(entityClass, query, top, (String[])null);
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#list(java.lang.Class, java.lang.Object, int)
	 */
	public <T> List<T> list(final Class<T> entityClass, final Object query, int top, String... entityHashcodes) {
		Assert.notNull(entityClass, "entityClass must not be null");
		Assert.notNull(query, "query must not be null");
		
		return this.list(queryFactory.createQuery(entityClass, query), top, entityHashcodes);
	}

	@Override
	public <T> List<T> list(final QueryObject<T> query, int top, String... entityHashcodes) {
		Assert.notNull(query, "query must not be null");
		List<Object> params = new ArrayList<Object>();
		String sql = queryFactory.buildTopSql(params, query, top, entityHashcodes);
		log.error(sql);
		log.error(params.toString());
		return this.query(query.entityClass(), sql, params);
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#list(ydh.cicada.query.QueryObject, int)
	 */
	@Override
	public <T> List<T> list(final QueryObject<T> query, int top) {
		return this.list(query, top, (String[])null);
	}
	
	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#find(java.lang.Class, java.lang.Object)
	 */
	@Override
	public <T> Page<T> find(Class<T> entityClass, Object query) {
		return this.find(entityClass, query, (String[])null);
	}

	@Override
	public <T> Page<T> find(Class<T> entityClass, Object query, String... entityHashcodes) {
		Assert.notNull(entityClass, "entityClass must not be null");
		Assert.notNull(query, "query must not be null");
		
		return this.find(queryFactory.createQuery(entityClass, query), entityHashcodes);
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#find(ydh.cicada.query.QueryObject)
	 */
	@Override
	public <T> Page<T> find(final QueryObject<T> query) {
		return find(query, (String[])null);
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.SplitableDao#find(ydh.cicada.query.QueryObject, java.lang.String[])
	 */
	@Override
	public <T> Page<T> find(QueryObject<T> query, String... entityHashcodes) {
		Assert.notNull(query, "query must not be null");
		List<Object> params = new ArrayList<Object>();
		String countSql = queryFactory.buildCountSql(params, query, entityHashcodes);
		log.error(countSql);
		log.error(params.toString());
		long total = jdbc.queryForObject(countSql, params.toArray(), Long.class);
		
		params.clear();
		String pageSql = queryFactory.buildPageSql(params, query, entityHashcodes);
		log.error(pageSql);
		log.error(params.toString());
		List<T> data = this.query(query.entityClass(), pageSql, params);
		return new Page<T>(total, data, query.page(), query.pageSize());
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#find(ydh.cicada.query.QueryObject)
	 */
	@Override
	public <T> Page<T> find(final QueryObject<T> query, long total) {
		return find(query, total, (String[])null);
	}
	
	public <T> Page<T> find(final QueryObject<T> query, long total, String... entityHashcodes) {
		Assert.notNull(query, "query must not be null");
		List<Object> params = new ArrayList<Object>();
		String pageSql = queryFactory.buildPageSql(params, query);
		log.error(pageSql);
		log.error(params.toString());
		List<T> data = this.query(query.entityClass(), pageSql, params);
		return new Page<T>(total, data, query.page(), query.pageSize());
	}
	 
	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#count(java.lang.Object)
	 */
	@Override
	public long count(Object query) {
		return this.count(query, (String[])null);
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.SplitableDao#count(java.lang.Object, java.lang.String[])
	 */
	@Override
	public long count(Object query, String... entityHashcodes) {
		Assert.notNull(query, "query must not be null");
		return this.count(queryFactory.createQuery(Object.class, query), entityHashcodes);
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.SplitableDao#count(ydh.cicada.query.QueryObject, java.lang.String[])
	 */
	@Override
	public long count(QueryObject<?> query, String... entityHashcodes) {
		Assert.notNull(query, "query must not be null");
		List<Object> params = new ArrayList<Object>();
		String countSql = queryFactory.buildCountSql(params, query, entityHashcodes);
		log.error(countSql);
		log.error(params.toString());
		return jdbc.queryForObject(countSql, params.toArray(), Long.class);
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#count(ydh.cicada.query.QueryObject)
	 */
	@Override
	public long count(final QueryObject<?> query) {
		return count(query, (String[])null);
	}
	
	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#uniqueResult(java.lang.Class, java.lang.Object)
	 */
	@Override
	public <T> T uniqueResult(Class<T> entityClass, Object query) {
		return this.uniqueResult(entityClass, query, (String[])null);
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#uniqueResult(ydh.cicada.query.QueryObject)
	 */
	@Override
	public <T> T uniqueResult(final QueryObject<T> query) {
		return this.uniqueResult(query, (String[])null);
	}

	@Override
	public <T> T uniqueResult(Class<T> entityClass, Object query, String... entityHashcodes) {
		Assert.notNull(entityClass, "entityClass must not be null");
		Assert.notNull(query, "query must not be null");
		
		return this.uniqueResult(queryFactory.createQuery(entityClass, query), entityHashcodes);
	}

	@Override
	public <T> T uniqueResult(QueryObject<T> query, String... entityHashcodes) {
		Assert.notNull(query, "query must not be null");
		List<Object> params = new ArrayList<Object>();
		String sql = queryFactory.buildSql(params, query, entityHashcodes);
		log.error(sql);
		log.error(params.toString());
		CicadaRowMapper<T> rowMapper = new CicadaRowMapper<T>(query.entityClass());
		return jdbc.queryForObject(sql, rowMapper, params.toArray());
	}


	/* (non-Javadoc)
	 * @see ydh.cicada.dao.BaseDao#firstResult(java.lang.Class, java.lang.Object)
	 */
	@Override
	public <T> T firstResult(Class<T> entityClass, Object query) {
		return this.firstResult(entityClass, query, (String[])null);
	}
	
	/**
	 * @param query
	 * @return
	 */
	@Override
	public <T> T firstResult(QueryObject<T> query) {
		return this.firstResult(query, (String[])null);
	}
	
	@Override
	public <T> T firstResult(Class<T> entityClass, Object query, String... entityHashcodes) {
		Assert.notNull(entityClass, "entityClass must not be null");
		Assert.notNull(query, "query must not be null");
		return this.firstResult(queryFactory.createQuery(entityClass, query), entityHashcodes);
	}

	@Override
	public <T> T firstResult(QueryObject<T> query, String... entityHashcodes) {
		Assert.notNull(query, "query must not be null");
		List<Object> params = new ArrayList<Object>();
		String sql = queryFactory.buildTopSql(params, query, 1, entityHashcodes);
		log.error(sql);
		log.error(params.toString());
		List<T> results = this.query(query.entityClass(), sql, params);
		return results.isEmpty() ? null : results.get(0);
	}

	private <T> List<T> query(Class<T> entityClass, String sql, List<Object> args) {
		CicadaRowMapper<T> rowMapper = new CicadaRowMapper<T>(entityClass);
		return jdbc.query(sql, rowMapper, args.toArray());
	}
	/**
	 * 获取jdbcTemplate进行基本sql查询
	 * @author tearslee
	 * @return org.springframework.jdbc.core.JdbcTemplate
	 */
	public JdbcTemplate getJdbcTemplate(){
		return this.jdbc;
	}

}
