package ydh.news.web.query;

import java.util.Date;

import ydh.cicada.query.PagableQueryCmd;
import ydh.cicada.query.api.Query;
import ydh.cicada.query.api.QueryOperator;
import ydh.cicada.query.api.QueryParam;
/***
 * 留言
 * @author Administrator
 *
 */
@Query(from = "NEWS_COMMENT")
public class NewsCommentQuery extends PagableQueryCmd{
	
	@QueryParam(fieldName = "NEWS_COMMENT_ID")
	private Integer newsCommentId;
	
	@QueryParam(fieldName = "NEWS_ID")
	private Integer newsId;
	/**留言者*/
	@QueryParam(fieldName = "CUS_NAME")
	private String cusName;
	/**留言时间（最小值）*/
	@QueryParam(fieldName = "NEWS_COMMENT_DATE",op=QueryOperator.GT_EQU)
	private Date minNewsCommentDate;
	/**留言时间（最大值）*/
	@QueryParam(fieldName = "NEWS_COMMENT_DATE",op=QueryOperator.LESS_EQU)
	private Date maxNewsCommentDate;
	
	private Integer newsColumnId;
	public NewsCommentQuery() {
	}

	public Integer getNewsCommentId() {
		return newsCommentId;
	}

	public void setNewsCommentId(Integer newsCommentId) {
		this.newsCommentId = newsCommentId;
	}

	public Integer getNewsId() {
		return newsId;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public Date getMinNewsCommentDate() {
		return minNewsCommentDate;
	}

	public void setMinNewsCommentDate(Date minNewsCommentDate) {
		this.minNewsCommentDate = minNewsCommentDate;
	}

	public Date getMaxNewsCommentDate() {
		return maxNewsCommentDate;
	}

	public void setMaxNewsCommentDate(Date maxNewsCommentDate) {
		this.maxNewsCommentDate = maxNewsCommentDate;
	}

	public Integer getNewsColumnId() {
		return newsColumnId;
	}

	public void setNewsColumnId(Integer newsColumnId) {
		this.newsColumnId = newsColumnId;
	}

	
}
