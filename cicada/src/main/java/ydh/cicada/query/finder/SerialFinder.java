package ydh.cicada.query.finder;

import java.util.ArrayList;
import java.util.List;

import ydh.cicada.dao.SplitableDao;
import ydh.cicada.query.QueryObject;

/**
 * 对顺序方式分拆的表，按顺序进行逐一查询，直到查询结果达到pagesize或指定表已查询完毕
 * @author lizx
 *
 * @param <T>
 */
public class SerialFinder<T> implements Finder<T> {
	protected QueryObject<T> query;
	
	public SerialFinder(QueryObject<T> query) {
		this.query = query;
	}
	
	/* (non-Javadoc)
	 * @see ydh.cicada.query.finder.Finder#find(ydh.cicada.dao.SplitableDao, java.lang.String[])
	 */
	@Override
	public List<T> find(SplitableDao dao, String... hashcodes) {
		List<T> result = new ArrayList<T>();
		for (String hashcode : hashcodes) {
			result.addAll(dao.list(query, query.pageSize() - result.size(), hashcode));
			if (result.size() >= query.pageSize()) break;
		}
		return result;
	}

}
