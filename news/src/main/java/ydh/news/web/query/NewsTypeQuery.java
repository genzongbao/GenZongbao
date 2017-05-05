package ydh.news.web.query;

import ydh.cicada.query.PagableQueryCmd;
import ydh.cicada.query.api.Query;
import ydh.cicada.query.api.QueryOperator;
import ydh.cicada.query.api.QueryParam;

/**
 * 
 * @author chenkailun
 *
 */
@Query(from = "NEWS_TYPE", orderBy = "NEWS_TYPE_ID ASC")
public class NewsTypeQuery extends PagableQueryCmd{

	@QueryParam(fieldName = "NEWS_TYPE_ID")
	private Integer newsTypeId;
	
	@QueryParam(fieldName = "NEWS_TYPE_ID", op = QueryOperator.NOT_IN)
	private Integer[] notNewsTypeId;
	
	@QueryParam(fieldName = "NEWS_TYPE_NAME")
	private String newsTypeName;
	
	@QueryParam(fieldName = "NEWS_COLUMN_ID")
	private Integer newsColumnId;

	public Integer[] getNotNewsTypeId() {
		return notNewsTypeId;
	}

	public void setNotNewsTypeId(Integer[] notNewsTypeId) {
		this.notNewsTypeId = notNewsTypeId;
	}

	public Integer getNewsTypeId() {
		return newsTypeId;
	}

	public void setNewsTypeId(Integer newsTypeId) {
		this.newsTypeId = newsTypeId;
	}

	public String getNewsTypeName() {
		return newsTypeName;
	}

	public void setNewsTypeName(String newsTypeName) {
		this.newsTypeName = newsTypeName;
	}

	public Integer getNewsColumnId() {
		return newsColumnId;
	}

	public void setNewsColumnId(Integer newsColumnId) {
		this.newsColumnId = newsColumnId;
	}
	
}
