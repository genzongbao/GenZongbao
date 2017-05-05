package ydh.cicada.query.finder;

import java.util.List;

import ydh.cicada.dao.SplitableDao;

/**
 * 分表查询接口
 * 目前仅支持单hash值查询
 * @author lizx
 * @param <T> 查询结果对象类型
 */
public interface Finder<T> {
	/**
	 * 分拆表的查询，根据规则进行分拆表的查询，查询后归并输出
	 * 
	 * @param dao        
	 * @param hashcodes  需要查询的分拆表的hashcode的列表
	 * @return
	 */
	public List<T> find(SplitableDao dao, String... hashcodes);
}
