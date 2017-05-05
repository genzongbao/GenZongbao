package ydh.cicada.query.finder;

import java.util.List;

import ydh.cicada.dao.SplitableDao;
import ydh.cicada.query.QueryObject;

/**
 * 并行分表查询（MapReduce）
 * 采用多线程方式查询，对查询结果进行归并
 * @author lizx
 * 
 * @param <T> 查询结果对象
 */
public class ParallelFinder<T> implements Finder<T> {
	protected QueryObject<T> query;
	protected Comparable<T> comparable;
	
	public ParallelFinder(QueryObject<T> query, Comparable<T> comparable) {
		this.query = query;
		this.comparable = comparable;
	}
	
	public List<T> find(SplitableDao dao, String... hashcodes) {
		return null;
	}
	
}
