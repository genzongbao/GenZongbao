package ydh.cicada.dao;

public interface SplitStrategy<T> {
	public String hashcode(T entity);
}
