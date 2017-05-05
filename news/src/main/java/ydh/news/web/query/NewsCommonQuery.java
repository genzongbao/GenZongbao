package ydh.news.web.query;

import java.util.Date;

import ydh.cicada.query.PagableQueryCmd;
import ydh.cicada.query.api.Query;
import ydh.cicada.query.api.QueryParam;
import ydh.news.web.dict.NewsState;

@Query(from = "NEWS ", orderBy = "PUBLISH_TIME DESC")
public class NewsCommonQuery  extends PagableQueryCmd{
	@QueryParam(fieldName = "NEWS_ID")
	private Integer newsId;
	
	@QueryParam(fieldName = "TITLE")
	private String title;
	
	@QueryParam(fieldName = "CONTENT")
	private String content;
	
	@QueryParam(fieldName = "CLICK_COUNT")
	private Integer clickCount;
	
	@QueryParam(fieldName = "NEWS_STATE")
	private NewsState newsState;
	
	@QueryParam(fieldName = "PUBLISH_TIME")
	private Date publishTime;
	
	@QueryParam(fieldName = "AUTHOR")
	private String author;
	
	@QueryParam(fieldName = "NEWS_COLUMN_ID")
	private Integer newsColumnId;
	
	@QueryParam(fieldName = "NEWS_TYPE_ID")
	private Integer newsTypeId;

	public Integer getNewsId() {
		return newsId;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getClickCount() {
		return clickCount;
	}

	public void setClickCount(Integer clickCount) {
		this.clickCount = clickCount;
	}

	public NewsState getNewsState() {
		return newsState;
	}

	public void setNewsState(NewsState newsState) {
		this.newsState = newsState;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Integer getNewsColumnId() {
		return newsColumnId;
	}

	public void setNewsColumnId(Integer newsColumnId) {
		this.newsColumnId = newsColumnId;
	}

	public Integer getNewsTypeId() {
		return newsTypeId;
	}

	public void setNewsTypeId(Integer newsTypeId) {
		this.newsTypeId = newsTypeId;
	}


	
}
