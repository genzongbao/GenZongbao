/*
 * Copyright: SBT (c) 2015
 * Company: 成都盛比特信息技术有限公司
 */
package ydh.cicada.query.factory;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import ydh.cicada.api.Column;
import ydh.cicada.api.Id;
import ydh.cicada.query.Deleter;
import ydh.cicada.query.PagableQueryCmd;
import ydh.cicada.query.QueryObject;
import ydh.cicada.query.SortableQueryCmd;
import ydh.cicada.query.Updater;
import ydh.cicada.query.api.Query;
import ydh.cicada.query.api.QueryOperator;
import ydh.cicada.query.api.QueryParam;
import ydh.cicada.query.api.QueryParams;
import ydh.cicada.query.cond.Cond;
import ydh.cicada.query.cond.Conditionable;
import ydh.cicada.utils.EntityUtils;

/**
 * 查询对象工厂类的基础实现，不同的数据库由各自的实现支持
 * @author lizx
 */
public abstract class AbstractQueryFactory implements QueryFactory {
	
	/**
	 * 将要like语句中的特殊字符进行转义
	 * 
	 * @param object like语句对象
	 * @return 替换后的字符串
	 */
	protected abstract String buildLikeValue(Object obj);
	
	/**
	 * 创建日期类型比较运算语句
	 * 
	 * @param sql
	 * @param params
	 * @param fieldName
	 * @param op
	 * @param value
	 */
	protected abstract void buildDateCondition(StringBuilder sql, List<Object> params, String fieldName,
			QueryOperator op, Date value);
	
	/**
	 * 创建分页查询语句
	 * @param sql
	 * @param query
	 */
	protected abstract void buildPageSql(StringBuilder sql, List<Object> params, QueryObject<?> query);
	
	/**
	 * 创建查询语句
	 * @param sql
	 * @param query
	 */
	protected abstract void buildTopSql(StringBuilder sql, List<Object> params, QueryObject<?> query, int top);
	
	/**
	 * 创建for update子句
	 * @param sql
	 */
	protected void appendForUpdate(StringBuilder sql) {
		sql.append(" for update");
	}
	
	/* (non-Javadoc)
	 * @see ydh.cicada.query.factory.QueryFactory#createQuery(java.lang.Class, java.lang.Object)
	 */
	@Override
	public <T> QueryObject<T> createQuery(Class<T> entityClass, Object cmd) {
		try {
			Query query = cmd.getClass().getAnnotation(Query.class);
			QueryObject<T> qo = QueryObject.select(query.select(), entityClass);
			qo.distinct(query.distince());
			qo.from(query.from());
			qo.or(query.or());
			StringBuilder sql = new StringBuilder();
			List<Object> params = new ArrayList<Object>();
			// 处理固定查询条件
			if (query.where().length() > 0) {
				buildCondition(sql, params, cmd, query.where(), null, query.consts());
				qo.condition(sql.toString(), params.toArray());
				sql.setLength(0);
				params.clear();
			}
			// 处理在字段注解中定义的查询条件
	        Field[] fields = cmd.getClass().getDeclaredFields();
	        for(Field field : fields) {
	            QueryParam queryParam = getFieldQueryParam(field, cmd);
	            if (queryParam == null) continue;
	            Object value = PropertyUtils.getProperty(cmd, field.getName());
	            if (queryParam.stmt().length() > 0) {
	    			buildCondition(sql, params, cmd, queryParam.stmt(), value, queryParam.consts());
	            	qo.condition(sql.toString(), params.toArray());
	    		} else {
	    			String fieldName = queryParam.fieldName();
	    			if (fieldName.isEmpty()) {
	    				fieldName = EntityUtils.toUnderscore(field.getName());
	    			}
	    			qo.cond(fieldName).op(queryParam.op(), value);
	    		}
	            sql.setLength(0);
	            params.clear();
	        }
	        // 处理group by 子句
	        if (StringUtils.isNotEmpty(query.groupBy())) {
	        	if (StringUtils.isNotEmpty(query.having())) {
		        	qo.groupBy(query.groupBy(), query.having());
	        	} else {
	        		qo.groupBy(query.groupBy());
	        	}
	        }
	        // 处理order by 子句
	        if (StringUtils.isNotEmpty(query.orderBy())) {
	        	String[] orderBys = query.orderBy().split(",");
	        	for(String orderBy : orderBys) {
	        		qo.asc(orderBy.trim());
	        	}
	        }
	        if (cmd instanceof SortableQueryCmd) {
	        	String[] orders = ((SortableQueryCmd)cmd).getO();
	        	if (orders != null && orders.length > 0) {
	        		for (String order: orders) {
	        			if (order.charAt(0) == '!') {
	        				qo.desc(order.substring(1));
	        			} else {
	        				qo.asc(order);
	        			}
	        		}
	        	}
	        }
	        if (cmd instanceof PagableQueryCmd) {
	        	PagableQueryCmd pagableCmd = (PagableQueryCmd)cmd;
				Integer p = pagableCmd.getP();
				Integer s = pagableCmd.getS();
				if (p != null) {
					qo.page(p);
				}
				if (s != null) {
					qo.pageSize(s);
				}
	        }
			qo.forUpdate(query.forUpdate());
			return qo;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 创建查询条件
	 * 
	 * @param cmd
	 *            查询对象
	 * @param params
	 *            输出的查询参数
	 * @param stmt
	 *            输入查询语句
	 * @param value
	 *            查询条件的传入参数值
	 * @param consts
	 *            查询条件使用到的常量
	 * @return 查询语句
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	protected void buildCondition(StringBuilder sql, List<Object> params,
			Object cmd, String stmt, Object value, String[] consts) {
		sql.append("(");
		StringBuilder placeholder = new StringBuilder();
		boolean isConst = true;
		int state = 0;
		for (int j = 0; j < stmt.length(); j++) {
			char c = stmt.charAt(j);
			if (state == 0) {
				if (c == '{') {
					state = 1;
					placeholder.setLength(0);
					isConst = true;
				} else if (c == '?') {
					if (stmt.length() > j + 1 && stmt.charAt(j + 1) == '%') { // ?%
						value = buildLikeValue(value) + '%';
						j++;
					}
					sql.append(c);
					params.add(value);
				} else if (c == '%') {
					if (stmt.length() > j + 1 && stmt.charAt(j + 1) == '?') {
						if (stmt.length() > j + 2 && stmt.charAt(j + 2) == '%') {// %?%
							value = '%' + buildLikeValue(value) + '%';
							j += 2;
						} else { // %?
							value = '%' + buildLikeValue(value);
							j++;
						}
						sql.append('?');
					} else {
						sql.append(c);
					}
				} else {
					sql.append(c);
				}
			} else {
				if (c == '}') {
					if (isConst) {
						int idx = Integer.parseInt(placeholder.toString());
						sql.append(consts[idx - 1]);
					} else {
						try {
							Field field = cmd.getClass().getDeclaredField(placeholder.toString());
							Object embedValue = PropertyUtils.getProperty(cmd, field.getName());
							if (embedValue == null) {
								throw new RuntimeException("embed param must be not null: " + field.getName());
							}
							buildCondition(sql, params, cmd, field, embedValue);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
					state = 0;
				} else {
					placeholder.append(c);
					if (c < '0' || c > '9') {
						isConst = false;
					}
				}
			}
		}
		sql.append(")");
	}

	/**
	 * 根据字段注解和值提取字段注解，如果返回null表示本字段不作为参数之一
	 * @param field
	 * @param cmd
	 * @return
	 */
	private QueryParam getFieldQueryParam(Field field, Object cmd) {
		QueryParams queryParams = field.getAnnotation(QueryParams.class);
		QueryParam queryParam = field.getAnnotation(QueryParam.class);
		try {
			if (queryParams != null) {
				if (queryParams.embed()) return null;
				Object value = PropertyUtils.getSimpleProperty(cmd, field.getName());
				if (value == null) return null;
				for (QueryParam qp : queryParams.value()) {
					if (qp.on().isEmpty()
							|| qp.on().equals(value.toString())) {
						return qp;
					}
				}
			} else if (queryParam != null) {
				if (queryParam.embed()) return null;
				Object value = PropertyUtils.getSimpleProperty(cmd, field.getName());
				if (value == null) return null;
				if (queryParam.on().isEmpty() || queryParam.on().equals(value.toString())) return queryParam;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	
	private void buildCondition(StringBuilder sql, List<Object> params,
			Object cmd, Field field, Object value) throws Exception {
		QueryParams queryParams = field.getAnnotation(QueryParams.class);
		if (queryParams != null) {
			for (QueryParam queryParam : queryParams.value()) {
				if (queryParam.on().isEmpty()
						|| queryParam.on().equals(value.toString())) {
					buildCondition(sql, params, cmd, field, queryParam, value);
					break;
				}
			}
		}
		QueryParam queryParam = field.getAnnotation(QueryParam.class);
		if (queryParam != null) {
			if (queryParam.on().isEmpty()
					|| queryParam.on().equals(value.toString())) {
				buildCondition(sql, params, cmd, field, queryParam, value);
			}
		}
	}

	private void buildCondition(StringBuilder sql, List<Object> params,
			Object cmd, Field field, QueryParam queryParam, Object value)
			throws Exception {
		if (queryParam.stmt().length() > 0) {
			buildCondition(sql, params, cmd, queryParam.stmt(), value,
					queryParam.consts());
		} else {
			String fieldName = queryParam.fieldName();
			if (fieldName.isEmpty()) {
				fieldName = EntityUtils.toUnderscore(field.getName());
			}
			buildCondition(sql, params, fieldName, queryParam.op(), value);
		}
	}

	private void buildCondition(StringBuilder sql, List<Object> params,
			String fieldName, QueryOperator op, Object value) {
		if (op == null) {
			sql.append("(").append(fieldName);
			if (value.getClass().isArray()) {
				int len = Array.getLength(value);
				for (int i = 0; i < len; i++) {
					params.add(EntityUtils.strip(Array.get(value, i)));
				}
			}
			sql.append(")");
		} else if (op == QueryOperator.IN) {
			int len = Array.getLength(value);
			sql.append(fieldName).append(" in (?");
			params.add(EntityUtils.strip(Array.get(value, 0)));
			for (int i = 1; i < len; i++) {
				sql.append(",?");
				params.add(EntityUtils.strip(Array.get(value, i)));
			}
			sql.append(")");
		} else if (op == QueryOperator.IS_NULL || op == QueryOperator.IS_NOT_NULL) {
			sql.append(fieldName).append(" ").append(op.getSymbol());
		} else if (value.getClass().isArray()) {
			int len = Array.getLength(value);
			sql.append("(");
			buildCondition(sql, params, fieldName, op, Array.get(value, 0));
			for (int i = 1; i < len; i++) {
				sql.append(" or ");
				buildCondition(sql, params, fieldName, op, Array.get(value, i));
			}
			sql.append(")");
		} else if (Date.class.isAssignableFrom(value.getClass())) {
			buildDateCondition(sql, params, fieldName, op, (Date) value);
		} else {
			sql.append(fieldName).append(" ").append(op.getSymbol())
					.append(" ?");
			if (op == QueryOperator.LIKE) {
				params.add("%" + buildLikeValue(value) + "%");
			} else if (op == QueryOperator.LEFTLIKE) {
				params.add(buildLikeValue(value) + "%");
			} else if (op == QueryOperator.RIGHTLIKE) {
				params.add("%" + buildLikeValue(value));
			} else {
				params.add(EntityUtils.strip(value));
			}
		}
	}
    
    /* (non-Javadoc)
	 * @see ydh.cicada.query.factory.QueryFactory#buildSql(ydh.cicada.query.QueryObject)
	 */
    @Override
	public final String buildSql(List<Object> params, QueryObject<?> query, String... entityHashcodes) {
    	StringBuilder sql = new StringBuilder(512);
    	buildSql(sql, params, query);
    	return replaceEntityHashcodes(sql, entityHashcodes);
    }
    
    /* (non-Javadoc)
	 * @see ydh.cicada.query.factory.QueryFactory#buildCountSql(ydh.cicada.query.QueryObject)
	 */
    @Override
	public final String buildCountSql(List<Object> params, QueryObject<?> query, String... entityHashcodes) {
    	StringBuilder sql = new StringBuilder(512);
    	buildCountSql(sql, params, query);
    	return replaceEntityHashcodes(sql, entityHashcodes);
    }
    
    /* (non-Javadoc)
	 * @see ydh.cicada.query.factory.QueryFactory#buildPageSql(ydh.cicada.query.QueryObject)
	 */
    @Override
	public final String buildPageSql(List<Object> params, QueryObject<?> query, String... entityHashcodes) {
    	StringBuilder sql = new StringBuilder(512);
    	buildPageSql(sql, params, query);
    	return replaceEntityHashcodes(sql, entityHashcodes);
    }
    
    /* (non-Javadoc)
	 * @see ydh.cicada.query.factory.QueryFactory#buildTopSql(ydh.cicada.query.QueryObject, int)
	 */
    @Override
	public final String buildTopSql(List<Object> params, QueryObject<?> query, int top, String... entityHashcodes) {
    	StringBuilder sql = new StringBuilder(512);
    	buildTopSql(sql, params, query, top);
    	return replaceEntityHashcodes(sql, entityHashcodes);
    }
    
    /**
     * 根据QueryObject的Cond对象构造条件
     * @param sql
     * @param params
     * @param cond
     */
    private void buildCondition(StringBuilder sql, List<Object> params, Cond<?> cond) {
		this.buildCondition(sql, params, cond.field(), cond.op(), cond.param());
    }
    
    /**
     * 创建查询语句
     * @param sql
     * @param query
     * @throws Exception 
     */
    protected void buildSql(StringBuilder sql, List<Object> params, QueryObject<?> query, String... entityHashcodes) {
    	sql.append("select ");
    	if (query.distinct()) {
    		sql.append("distinct ");
    	}
    	sql.append(query.select());
    	sql.append(" from ");
    	sql.append(query.from());
    	if ( ! query.conditions().isEmpty()) {
    		sql.append(" where ");
    		this.buildConditions(sql, params, query);
    	}
    	if (StringUtils.isNotEmpty(query.groupBy())) {
    		sql.append(" group by ").append(query.groupBy());
    		if(StringUtils.isNotEmpty(query.having())) {
    			sql.append(" having ").append(query.having());
    		}
    	}
    	if (! query.orders().isEmpty()) {
    		sql.append(" order by ");
    		join(sql, query.orders(), ",");
    	}
    	if (query.forUpdate()) {
    		this.appendForUpdate(sql);
    	}
    }
    
    /**
     * 创建统计数量的查询语句
     * @param sql
     * @param query
     */
	protected void buildCountSql(StringBuilder sql, List<Object> params, QueryObject<?> query) {
    	if (StringUtils.isNotEmpty(query.groupBy())) {
    		// 含有group by子句，使用查询方式查询
    		sql.append("select count(distinct ");
    		sql.append(query.groupBy());
    		sql.append(") from ");
        	sql.append(query.from());
        	if ( ! query.conditions().isEmpty()) {
        		sql.append(" where ");
        		buildConditions(sql, params, query);
        	}
    	} else {
    		// 不含有group by子句，使用count(*)方式查询
	    	if (query.distinct()) {
	    		sql.append("select count(distinct ").append(query.select()).append(")");
	    	} else {
	    		sql.append("select count(*)");
	    	}
	    	sql.append(" from ");
	    	sql.append(query.from());
	    	if ( ! query.conditions().isEmpty()) {
	    		sql.append(" where ");
	    		buildConditions(sql, params, query);
	    	}
	    	if (StringUtils.isNotEmpty(query.groupBy())) {
	    		sql.append(" group by ").append(query.groupBy());
	    	}
    	}
    }

	private <Q extends Conditionable<Q>> void buildConditions(StringBuilder sql, List<Object> params, Q query) {
		List<Cond<Q>> conds = query.conditions();
		Cond<Q> first = conds.get(0);
		this.buildCondition(sql, params, first);
		for (int i = 1; i < conds.size(); i++) {
			sql.append(query.or() ? " or " : " and ");
			this.buildCondition(sql, params, conds.get(i));
		}
	}
	
	/**
	 * 使用分隔符连接字符串
	 * @param sql        连接结果输出
	 * @param list       字符串列表
	 * @param separator  分隔符
	 */
	protected final void join(StringBuilder sql, Collection<String> list, String separator) {
    	Iterator<String> i = list.iterator();
    	sql.append(i.next());
    	while (i.hasNext()) {
    		sql.append(separator);
    		sql.append(i.next());
    	}
    }
	
	/* (non-Javadoc)
	 * @see ydh.cicada.query.factory.QueryFactory#buildInsertSql(java.util.List, java.lang.Object)
	 */
	@Override
	public String buildInsertSql(List<Object> params, Object obj) {
		StringBuilder sql = new StringBuilder(512);
		buildInsertSql(sql, params, obj);
		return sql.toString();
	}
	
	/* (non-Javadoc)
	 * @see ydh.cicada.query.factory.QueryFactory#buildUpdateSql(java.util.List, java.lang.Object)
	 */
	@Override
	public String buildUpdateSql(List<Object> params, Object obj) {
		StringBuilder sql = new StringBuilder(512);
		buildUpdateSql(sql, params, obj);
		return sql.toString();
	}
	
	/* (non-Javadoc)
	 * @see ydh.cicada.query.factory.QueryFactory#buildUpdateSql(ydh.cicada.query.Updater)
	 */
	@Override
	public String buildUpdateSql(List<Object> params, Updater<?> updater, String entityHashcode) {
		StringBuilder sql = new StringBuilder(512);
		buildUpdateSql(sql, params, updater, entityHashcode);
		return sql.toString();
	}
	
	/* (non-Javadoc)
	 * @see ydh.cicada.query.factory.QueryFactory#buildDeleteSql(java.util.List, java.lang.Object)
	 */
	@Override
	public String buildDeleteSql(List<Object> params, Object obj) {
		StringBuilder sql = new StringBuilder(256);
		buildDeleteSql(sql, params, obj);
		return sql.toString();
	}
	
	/* (non-Javadoc)
	 * @see ydh.cicada.query.factory.QueryFactory#buildDeleteSql(java.util.List, java.lang.Class,  java.lang.Integer, java.io.Serializable)
	 */
	@Override
	public String buildDeleteSql(List<Object> params, Class<?> entityClass, String entityHashcode, Serializable id) {
		StringBuilder sql = new StringBuilder(256);
		buildDeleteSql(sql, params, entityClass, entityHashcode, id);
		return sql.toString();
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.query.factory.QueryFactory#buildDeleteSql(ydh.cicada.query.Deleter, java.lang.String)
	 */
	@Override
	public String buildDeleteSql(List<Object> params, Deleter<?> deleter, String entityHashcode) {
		StringBuilder sql = new StringBuilder(512);
		buildDeleteSql(sql, params, deleter, entityHashcode);
		return sql.toString();
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.query.factory.QueryFactory#buildLoadSql(java.util.List, java.lang.Class, java.io.Serializable, boolean)
	 */
	@Override
	public String buildLoadSql(List<Object> params, Class<?> entityClass, String entityHashcode, Serializable id, boolean forUpdate) {
		StringBuilder sql = new StringBuilder(256);
		buildLoadSql(sql, params, entityClass, entityHashcode, id, forUpdate);
		return sql.toString();
	}
	
	/**
	 * 创建插入语句
	 * @param sql     输出语句
	 * @param params  输出参数
	 * @param obj     实体对象
	 */
	protected void buildInsertSql(StringBuilder sql, List<Object> params, Object obj) {
		// 清空输出对象
		sql.setLength(0);
		params.clear();
		
		sql.append("insert into ").append(EntityUtils.tablename(obj)).append("(");
		Field[] fields = obj.getClass().getDeclaredFields();
		List<String> columnNames = new ArrayList<String>();
		String sequence = null;
		// 处理ID
		for (Field field : fields) {
			Id id = field.getAnnotation(Id.class);
			if (id != null) {
				if (id.autoincrement()) {
					continue;
				} else if (id.sequence().length() > 0) {
					sequence = id.sequence();
					Column column = field.getAnnotation(Column.class);
					columnNames.add(column.name());
				} else {
					Column column = field.getAnnotation(Column.class);
					columnNames.add(column.name());
					params.add(EntityUtils.strip(EntityUtils.getProperty(obj, field.getName())));
				}
			}
		}
		// 处理非ID字段
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			Id id = field.getAnnotation(Id.class);
			if (column != null && id == null) {
				columnNames.add(column.name());
				params.add(EntityUtils.strip(EntityUtils.getProperty(obj, field.getName())));
			}
		}
		join(sql, columnNames, ",");
		sql.append(") values (");
		if (sequence != null) {
			sql.append(sequence).append(".nextval,");
		}
		for (int i = 0; i < params.size(); i++) {
			sql.append(i == 0 ? "?" : ",?");
		}
		sql.append(")");
	}
	
	/**
	 * 创建更新语句
	 * @param sql
	 * @param params
	 * @param obj
	 */
	protected void buildUpdateSql(StringBuilder sql, List<Object> params, Object obj) {
		// 清空输出对象
		sql.setLength(0);
		params.clear();
		
		sql.append("update ").append(EntityUtils.tablename(obj)).append(" set ");
		Field[] fields = obj.getClass().getDeclaredFields();
		List<String> fieldList = new ArrayList<String>();
		// 处理非ID字段
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			Id id = field.getAnnotation(Id.class);
			if (column != null && id == null) {
				fieldList.add(column.name() + "=?");
				params.add(EntityUtils.strip(EntityUtils.getProperty(obj, field.getName())));
			}
		}
		join(sql, fieldList, ",");
		sql.append(" where ");
		fieldList.clear();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			Id id = field.getAnnotation(Id.class);
			if (column != null && id != null) {
				fieldList.add(column.name() + "=?");
				Object value = EntityUtils.getProperty(obj, field.getName());
				if (value == null) {
					throw new RuntimeException(column.name() + " field in update sql MUST NOT NULL");
				}
				params.add(EntityUtils.strip(value));
			}
		}
		join(sql, fieldList, " and ");
	}
	
	private void buildUpdateSql(StringBuilder sql, List<Object> params, Updater<?> updater, String entityHashcode) {
		String tablename = EntityUtils.tablename(updater.entityClass(), entityHashcode);
		sql.setLength(0);
		sql.append("update ").append(tablename).append(" set ");
		join(sql, updater.setters(), ",");
		for (Object value : updater.values()) {
			params.add(value);			
		}
		if (! updater.forAll()) {
			sql.append(" where ");
			this.buildConditions(sql, params, updater);
		}
	}

	/**
	 * 创建删除语句
	 * @param sql
	 * @param params
	 * @param obj
	 */
	protected void buildDeleteSql(StringBuilder sql, List<Object> params, Object obj) {
		// 清空输出对象
		sql.setLength(0);
		params.clear();
		
		sql.append("delete from ").append(EntityUtils.tablename(obj));
		sql.append(" where ");
		Field[] fields = obj.getClass().getDeclaredFields();
		List<String> fieldList = new ArrayList<String>();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			Id id = field.getAnnotation(Id.class);
			if (column != null && id != null) {
				fieldList.add(column.name() + "=?");
				Object value = EntityUtils.getProperty(obj, field.getName());
				if (value == null) {
					throw new RuntimeException(column.name() + " field in delete sql MUST NOT NULL");
				}
				params.add(EntityUtils.strip(value));
			}
		}
		join(sql, fieldList, " and ");
	}
	
	/**
	 * 创建更新语句
	 * @param sql
	 * @param params
	 * @param obj
	 */
	protected void buildDeleteSql(StringBuilder sql, List<Object> params, Class<?> entityClass, String entityHashcode, Serializable id) {
		// 清空输出对象
		sql.setLength(0);
		params.clear();
		
		sql.append("delete from ").append(EntityUtils.tablename(entityClass, entityHashcode));
		sql.append(" where ");
		Field[] fields = entityClass.getDeclaredFields();
		List<String> fieldList = new ArrayList<String>();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			Id idAnn = field.getAnnotation(Id.class);
			if (column != null && idAnn != null) {
				fieldList.add(column.name() + "=?");
			}
		}
		join(sql, fieldList, " and ");
		if (fieldList.size() > 1) {
			for (int i = 0; i < fieldList.size(); i++) {
				params.add(Array.get(id, i));
			}
		} else {
			params.add(id);
		}
	}
	
	/**
	 * 创建删除语句
	 * @param sql
	 * @param deleter
	 * @param entityHashcode
	 */
	private void buildDeleteSql(StringBuilder sql, List<Object> params, Deleter<?> deleter, String entityHashcode) {
		String tablename = EntityUtils.tablename(deleter.entityClass(), entityHashcode);
		sql.setLength(0);
		sql.append("delete from ").append(tablename);
		if (! deleter.forAll()) {
			sql.append(" where ");
			this.buildConditions(sql, params, deleter);
		}
	}
	
	/**
	 * 创建更新语句
	 * @param sql
	 * @param params
	 * @param obj
	 */
	protected void buildLoadSql(StringBuilder sql, List<Object> params, Class<?> entityClass, String entityHashcode, Serializable idValue, boolean forUpdate) {
		// 清空输出对象
		sql.setLength(0);
		params.clear();
		
		sql.append("select * from ").append(EntityUtils.tablename(entityClass, entityHashcode));
		sql.append(" where ");

		// 产生语句
		Field[] fields = entityClass.getDeclaredFields();
		List<String> fieldList = new ArrayList<String>();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			Id id = field.getAnnotation(Id.class);
			if (column != null && id != null) {
				fieldList.add(column.name() + "=?");
			}
		}
		join(sql, fieldList, " and ");
		// 根据传入的参数构造参数表
		if (idValue.getClass().isArray()) {
			for (int i = 0; i < Array.getLength(idValue); i++) {
				params.add(EntityUtils.strip(Array.get(idValue, i)));
			}
		} else {
			params.add(EntityUtils.strip(idValue));
		}
		if (forUpdate) {
			this.appendForUpdate(sql);
		}
	}
	
	private String replaceEntityHashcodes(CharSequence sql, String... entityHashcodes) {
		if (entityHashcodes == null || entityHashcodes.length == 0) {
			return sql.toString();
		} else {
			Matcher matcher = Pattern.compile("\\{([1-9])\\}").matcher(sql);
			StringBuffer sb = new StringBuffer();
		    while(matcher.find()) {
		        matcher.appendReplacement(sb, entityHashcodes[Integer.parseInt(matcher.group(1))-1]); 
		    }
			matcher.appendTail(sb);
			return sb.toString();
		}
	}
}
