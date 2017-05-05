package ydh.news.web.entity;

import java.util.Date;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;
import ydh.news.web.dict.NewsState;
import ydh.news.web.dict.ProjectNewsType;

/**
 * 新闻消息
 * @author LXL
 *
 */

@Entity(name = "NEWS")
public class News {

	/** 新闻ID*/
	@Id
	@Column(name = "NEWS_ID")
	private Integer newsId;

	/** 新闻内容*/
	@Column(name = "CONTENT")
	private String content;

	/** 新闻标题*/
	@Column(name = "TITLE")
	private String title;

	/** 点击量*/
	@Column(name = "CLICK_COUNT")
	private Integer clickCount;
	
	/** 评论量*/
	@Column(name = "COMMENT_COUNT")
	private Integer commentCount;
	
	/** 点赞量*/
	@Column(name = "PRAISE_COUNT")
	private Integer praiseCount;

	/** 新闻状态|0-未审核|1-正常 |2 -拒绝 | 3 - 删除*/
	@Column(name = "NEWS_STATE")
	private NewsState newsState;

	/** 发布时间*/
	@Column(name = "PUBLISH_TIME")
	private Date publishTime;

	/** 作者*/
	@Column(name = "AUTHOR")
	private String author;
	
	/** 栏目id */
	@Column(name = "NEWS_COLUMN_ID")
	private Integer newsColumnId;
	
	/** 新闻主图id */
	@Column(name = "NEWS_IMG_ID")
	private String newsImgId;
	
	/**类型id*/
	@Column(name = "NEWS_TYPE_ID")
	private Integer newsTypeId;
	/**临时字段*****************/
	
	/**新闻栏目名*/
	private String newsColumnName;
	
	/**新闻类型名*/
	private String newsTypeName;
	
	/**项目新闻类型 0为普通新闻，1为股东新闻*/
	private ProjectNewsType projectNewsType;
	
	public Integer getNewsColumnId() {
		return newsColumnId;
	}

	public void setNewsColumnId(Integer newsColumnId) {
		this.newsColumnId = newsColumnId;
	}

	public String getNewsColumnName() {
		return newsColumnName;
	}

	public void setNewsColumnName(String newsColumnName) {
		this.newsColumnName = newsColumnName;
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

	public Integer getNewsId() {
		return newsId;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}
	
	public Integer getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(Integer praiseCount) {
		this.praiseCount = praiseCount;
	}

	public String getNewsImgId() {
		return newsImgId;
	}

	public void setNewsImgId(String newsImgId) {
		this.newsImgId = newsImgId;
	}

	public ProjectNewsType getProjectNewsType() {
		return projectNewsType;
	}

	public void setProjectNewsType(ProjectNewsType projectNewsType) {
		this.projectNewsType = projectNewsType;
	}

	
}
