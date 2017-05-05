package ydh.news.web.query;

import ydh.cicada.query.PagableQueryCmd;
import ydh.cicada.query.api.Query;
import ydh.cicada.query.api.QueryParam;

@Query(from = "NEWS_PRAISE")
public class NewsPraiseQuery extends PagableQueryCmd{
	/**新闻id*/
	@QueryParam(fieldName = "NEWS_ID")
	private Integer newsId;
	/**客户id*/
	@QueryParam(fieldName = "CUS_ID")
	private String cusId;
	
	public NewsPraiseQuery() {
	}
	public Integer getNewsId() {
		return newsId;
	}
	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}
	public String getCusId() {
		return cusId;
	}
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}
	
	
}
