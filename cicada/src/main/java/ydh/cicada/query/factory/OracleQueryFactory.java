package ydh.cicada.query.factory;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import ydh.cicada.query.QueryObject;
import ydh.cicada.query.api.QueryOperator;

/**
 * Oracle 查询语句创建类
 * @author lizx
 */
public class OracleQueryFactory extends AbstractQueryFactory {

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
	 * @see ydh.cicada.query.factory.AbstractQueryFactory#buildDateCondition(java.lang.StringBuilder, java.util.List, java.lang.String, ydh.cicada.query.api.QueryOperator, java.util.Date)
	 */
	@Override
	protected void buildDateCondition(StringBuilder sql, List<Object> params,
			String fieldName, QueryOperator op, Date value) {
		sql.append("trunc(").append(fieldName).append(") ");
		sql.append(op.getSymbol()).append(" trunc(?)");
		params.add(value);
	}

	/* (non-Javadoc)
	 * @see ydh.cicada.query.factory.AbstractQueryFactory#buildPageSql(java.lang.StringBuilder, ydh.cicada.query.QueryObject)
	 */
	@Override
	protected void buildPageSql(StringBuilder sql, List<Object> params, QueryObject<?> query) {
		sql.append("select * from (select duang.*, ROWNUM rowno from (");
		this.buildSql(sql, params, query);
		sql.append(") duang WHERE ROWNUM <= ?) table_alias WHERE table_alias.rowno >= ?");
		params.add(query.page() * query.pageSize());
		params.add((query.page() - 1) * query.pageSize());
	}

	@Override
	protected void buildTopSql(StringBuilder sql, List<Object> params, QueryObject<?> query, int top) {
		sql.append("select * from (");
		this.buildSql(sql, params, query);
		sql.append(") WHERE ROWNUM <= ").append(top).append(")");
	}

}
