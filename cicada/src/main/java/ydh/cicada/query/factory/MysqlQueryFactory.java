/*
 * Copyright: SBT (c) 2015
 * Company: 成都盛比特信息技术有限公司
 */
package ydh.cicada.query.factory;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import ydh.cicada.query.QueryObject;
import ydh.cicada.query.api.QueryOperator;

/**
 * mysql 查询语句创建类
 * @author lizx
 */
public class MysqlQueryFactory extends AbstractQueryFactory {
	
	/* (non-Javadoc)
	 * @see ydh.cicada.query.factory.QueryFactory#buildLikeValue(java.lang.Object)
	 */
	protected String buildLikeValue(Object obj) {
		String value = obj.toString();
		if (StringUtils.isEmpty(value)) {
			return value;
		}
		// 转义反斜杠
		if (StringUtils.contains(value, '\\')) {
			value = value.replaceAll("\\\\", "\\\\\\\\\\\\\\\\");
		}
		// 转义_%?
		if (StringUtils.containsAny(value, "%_")) {
			value = value.replaceAll("([%_])", "\\\\$1");
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.query.QueryFactory#buildDateCondition(java.lang.StringBuilder, java.util.List, java.lang.String, infoql.QueryOperator, java.util.Date)
	 */
	protected void buildDateCondition(StringBuilder sql, List<Object> params,
			String fieldName, QueryOperator op, Date value) {
		sql.append("to_days(").append(fieldName).append(") ")
				.append(op.getSymbol()).append(" to_days(?)");
		params.add(value);
	}
	
	/* (non-Javadoc)
	 * @see ydh.cicada.query.factory.AbstractQueryFactory#buildPageSql(java.lang.StringBuilder, java.util.List, ydh.cicada.query.QueryObject)
	 */
	protected void buildPageSql(StringBuilder sql, List<Object> params, QueryObject<?> query) {
    	buildSql(sql, params, query);
    	sql.append(" limit ?,?");
    	params.add((query.page() - 1) * query.pageSize());
    	params.add(query.pageSize());
    }
	
	/* (non-Javadoc)
	 * @see ydh.cicada.query.factory.AbstractQueryFactory#buildTopSql(java.lang.StringBuilder, java.util.List, ydh.cicada.query.QueryObject, int)
	 */
	public void buildTopSql(StringBuilder sql, List<Object> params, QueryObject<?> query, int top) {
		buildSql(sql, params, query);
    	sql.append(" limit "+ top);
	}

}
