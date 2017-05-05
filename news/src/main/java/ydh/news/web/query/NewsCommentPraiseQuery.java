package ydh.news.web.query;

import ydh.cicada.query.PagableQueryCmd;
import ydh.cicada.query.api.Query;
import ydh.cicada.query.api.QueryParam;
/**
 * 新闻评论点赞 query
 * @author lyb
 *
 */
@Query(from = "NEWS_COMMENT_PRAISE")
public class NewsCommentPraiseQuery extends PagableQueryCmd{
	/**新闻id*/
	@QueryParam(fieldName = "NEWS_ID")
	private Integer newsId;
	/**客户id*/
	@QueryParam(fieldName = "CUS_ID")
	private String cusId;
	/**评论id*/
	@QueryParam(fieldName = "NEWS_COMMENT_ID")
	private Integer newsCommentId;
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
	public Integer getNewsCommentId() {
		return newsCommentId;
	}
	public void setNewsCommentId(Integer newsCommentId) {
		this.newsCommentId = newsCommentId;
	}
	
}
