/*
 * Copyright: SBT (c) 2015
 * Company: 成都盛比特信息技术有限公司
 */
package ydh.cicada.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ydh.cicada.api.Entity;
import ydh.cicada.dao.BaseDao;
import ydh.cicada.dao.Page;
import ydh.cicada.query.cond.Conditionable;
import ydh.cicada.service.CommonService;
import ydh.cicada.utils.EntityUtils;

/**
 * 查询对象
 * @version 1.0
 * @author lizx
 * @created  2015年1月14日
 */
public class QueryObject<T> extends Conditionable<QueryObject<T>> {
	static final Logger log = LoggerFactory.getLogger(QueryObject.class);
	
	public static final int DEFAULT_PAGE_SIZE = 20;
	
	private Class<T> entityClass;
	
	private List<String> orders;
	private String select;
	private boolean distinct;
	private String from;
	private String groupBy;
	private String having;
	private boolean forUpdate;
	private long page = 1;
	private int pageSize = DEFAULT_PAGE_SIZE;
	
	private QueryObject(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	/**
	 * 单表查询
	 * @param entityClass 输出对象类型及查询的表实体
	 * @return 新建的查询对象
	 */
	public static <T> QueryObject<T> select(Class<T> entityClass) {
		return select(entityClass, null);
	}
	
	/**
	 * 单表查询（拆分表的查询）
	 * @param entityClass    输出对象类型及查询的表实体
	 * @param entityHashcode 分表hash码
	 * @return
	 */
	public static <T> QueryObject<T> select(Class<T> entityClass, String entityHashcode) {
		Entity entity = entityClass.getAnnotation(Entity.class);
		QueryObject<T> qo = new QueryObject<T>(entityClass);
		qo.select = "*";
		if (entity != null) {
			qo.from(EntityUtils.tablename(entity, entityHashcode));
		}
		return qo;
	}
	
	/**
	 * 复合查询
	 * @param entityClass   输出对象类型
	 * @param selectClause  select子句
	 * @return 新建的查询对象
	 */
	public static <T> QueryObject<T> select(String selectClause, Class<T> entityClass) {
		return select(selectClause, entityClass, null);
	}
	
	/**
	 * 复合查询
	 * @param entityClass     输出对象类型
	 * @param entityHashcode  分表Hash值
	 * @param selectClause    select子句
	 * @return 新建的查询对象
	 */
	public static <T> QueryObject<T> select(String selectClause, Class<T> entityClass, String entityHashcode) {
		QueryObject<T> qo = select(entityClass, entityHashcode);
		qo.select = selectClause;
		return qo;
	}
	
	/**
	 * 指定是否添加distinct字句
	 * @param distinct 
	 * @return 查询对象本身
	 */
	public QueryObject<T> distinct(boolean distinct) {
		this.distinct = distinct;
		return this;
	}

	/**
	 * 指定from子句，单表查询时可以省略
	 * @param fromClause
	 * @return 查询对象本身
	 */
	public QueryObject<T> from(String fromClause) {
		this.from = fromClause;
		return this;
	}
	
	/**
	 * 指定查询条件
	 * @param stmt   查询条件语句
	 * @param params 查询条件的参数，与语句中的？匹配对应
	 * @return 查询对象本身
	 */
	public QueryObject<T> condition(String stmt, Object... params) {
		this.cond(stmt).op(null, params);
		return this;
	}

	/**
	 * 指定group by 子句
	 * @param groupBy group by子句内容
	 * @return 查询对象本身
	 */
	public QueryObject<T> groupBy(String groupBy) {
		return groupBy(groupBy, null);
	}
	
	/**
	 * 指定group by 子句
	 * @param groupBy group by子句内容
	 * @param having having子句内容
	 * @return 查询对象本身
	 */
	public QueryObject<T> groupBy(String groupBy, String having) {
		this.groupBy = groupBy;
		this.having = having;
		return this;
	}

	/**
	 * 添加正序排列的排序字段
	 * @param fieldName 字段名
	 * @return 查询对象本身
	 */
	public QueryObject<T> asc(String fieldName) {
		this._orders().add(fieldName);
		return this;
	}
	
	/**
	 * 添加逆序排列的排序字段
	 * @param fieldName 字段名
	 * @return 查询对象本身
	 */
	public QueryObject<T> desc(String fieldName) {
		this._orders().add(fieldName + " desc");
		return this;
	}

	/**
	 * 指定查询页码（从1开始）
	 * @param page 页码
	 * @return 查询对象本身
	 */
	public QueryObject<T> page(long page) {
		this.page = page;
		return this;
	}
	
	/**
	 * 指定分页大小
	 * @param pageSize 分页大小
	 * @return 查询对象本身
	 */
	public QueryObject<T> pageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}
	
	/**
	 * 指定是否数据加锁
	 * @param forUpdate
	 * @return 查询对象本身
	 */
	public QueryObject<T> forUpdate(boolean forUpdate) {
		this.forUpdate = forUpdate;
		return this;
	}
	
	private List<String> _orders() {
		if (this.orders == null) {
			this.orders = new ArrayList<String>();
		}
		return this.orders;
	}
	
	// 读取查询语句各个部分

	/**
	 * @return 查询结果的输出类型
	 */
	public Class<T> entityClass() {
		return this.entityClass;
	}
	
	/**
	 * @return select子句
	 */
	public String select() {
		return this.select;
	}

	/**
	 * @return 是否distinct
	 */
	public boolean distinct() {
		return this.distinct;
	}
	
	/**
	 * @return from子句
	 */
	public String from() {
		return this.from;
	}
	
	/**
	 * @return group by子句
	 */
	public String groupBy() {
		return this.groupBy;
	}
	
	/**
	 * @return having子句
	 */
	public String having() {
		return this.having;
	}

	/**
	 * @return 排序列表
	 */
	public List<String> orders() {
		return Collections.unmodifiableList(this._orders());
	}

	/**
	 * @return 查询页码
	 */
	public long page() {
		return this.page;
	}
	
	/**
	 * @return 分页大小
	 */
	public int pageSize() {
		return this.pageSize;
	}
	
	/**
	 * @return 是否加锁
	 */
	public boolean forUpdate() {
		return this.forUpdate;
	}

	/**
     * 执行查询语句,返回结果列表
     * @return 结果列表
     */
	public List<T> list(BaseDao dao) {
		return dao.list(this);
	}
	
	/**
     * 执行查询语句,返回结果列表,限制最大条数
     * @param top 	       条数限制
     * @return 结果列表
     */
	public List<T> list(BaseDao dao, int top) {
		return dao.list(this, top);
	}
	
	/**
     * 分页查询
     * @return  查询结果
     */
	public Page<T> find(BaseDao dao) {
		return dao.find(this);
	}
	
	/**
     * 快速分页查询（已知查询结果总数）
     * @return  查询结果
     */
	public Page<T> find(BaseDao dao, long total) {
		return dao.find(this, total);
	}
	
	/**
     * 统计符合条件的记录数量
     * @return 符合条件的记录数量
     */
	public long count(BaseDao dao) {
		return dao.count(this);
	}

	/**
     * 查询符合条件的唯一记录,如果查询结果不唯一将抛出错误
     * @return 符合条件的记录
     */
	public T uniqueResult(BaseDao dao) {
		return dao.uniqueResult(this);
	}

	/**
     * 查询符合条件的第一条记录，没找到就返回空
     * @return 符合条件的第一条记录
     */
	public T firstResult(BaseDao dao) {
		return dao.firstResult(this);
	}

	/**
     * 执行查询语句,返回结果列表
     * @return 结果列表
     */
	public List<T> list(CommonService service) {
		return service.list(this);
	}
	
	/**
     * 执行查询语句,返回结果列表,限制最大条数
     * @param top 	       条数限制
     * @return 结果列表
     */
	public List<T> list(CommonService service, int top) {
		return service.list(this, top);
	}
	
	/**
     * 分页查询
     * @return  查询结果
     */
	public Page<T> find(CommonService service) {
		return service.find(this);
	}
	
	/**
     * 统计符合条件的记录数量
     * @return 符合条件的记录数量
     */
	public long count(CommonService service) {
		return service.count(this);
	}

	/**
     * 查询符合条件的唯一记录,如果查询结果不唯一将抛出错误
     * @return 符合条件的记录
     */
	public T uniqueResult(CommonService service) {
		return service.uniqueResult(this);
	}

	/**
     * 查询符合条件的第一条记录，没找到就返回空
     * @return 符合条件的第一条记录
     */
	public T firstResult(CommonService service) {
		return service.firstResult(this);
	}

}

