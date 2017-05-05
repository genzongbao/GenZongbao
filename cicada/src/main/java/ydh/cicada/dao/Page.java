package ydh.cicada.dao;

import java.util.Collections;
import java.util.List;

public class Page<T> {
	private long totalPage;
	private long page;
	private int pageSize;
	private List<T> data;
	private long totalSize;
	
	public Page(long totalSize, List<T> data, long page, int pageSize) {
		this.totalSize = totalSize;
		this.data = Collections.unmodifiableList(data);
		this.page = page;
		this.pageSize = pageSize;
		this.totalPage = (totalSize + pageSize - 1) / pageSize;
	}

    /**
     * 取得总页数.
     * @return  总页数
     */
	public long getTotalPage() {
		return this.totalPage;
	}

    /**
     * 取得当前页码.
     * @return  当前页码
     */
	public long getPage() {
		return this.page;
	}

    /** 取得每页数据行数
     * @return 每页的数据行数
     */
	public int getPageSize() {
		return this.pageSize;
	}
	
    /**
     * 是否有前页.
     * @return  true --> 有前页，false --> 无前页
     */
	public boolean hasPreviousPage() {
    	return this.page > 1;
    }

    /**
     * 是否有后页.
     * @return  true --> 有后页，false --> 无后页
     */
    public boolean hasNextPage() {
    	return this.page < this.totalPage;
    }

    /**
     * 返回当前页数据.
     * @return  当前页数据
     */
    public List<T> getData() {
    	return this.data;
    }

    /**
     * @return 总记录数
     */
    public long getTotalSize() {
    	return this.totalSize;
    }
}
