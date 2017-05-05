package ydh.news.web.query;

import java.util.Date;

import ydh.cicada.query.PagableQueryCmd;
import ydh.cicada.query.api.Query;
import ydh.cicada.query.api.QueryOperator;
import ydh.cicada.query.api.QueryParam;
import ydh.news.web.dict.NewsState;

@Query(	select = "N.*,NC.*,NT.*", 
		from = "NEWS AS N "
		+ "LEFT JOIN NEWS_COLUMN AS NC ON N.NEWS_COLUMN_ID=NC.NEWS_COLUMN_ID "
		+ "LEFT JOIN NEWS_TYPE AS NT ON N.NEWS_TYPE_ID=NT.NEWS_TYPE_ID",orderBy="N.PUBLISH_TIME desc")
public class NewsQuery extends PagableQueryCmd{
	
	/** 栏目-新闻 */
	public static final Integer COLUMN_NEWS = 1;
	/** 栏目-关于 */
	public static final Integer COLUMN_ABOUT = 2;
	
	/** 新闻ID*/
	@QueryParam(fieldName = "N.NEWS_ID")
	private Integer newsId;

	/** 新闻标题*/
	@QueryParam(fieldName = "N.TITLE", op = QueryOperator.LIKE)
	private String title;

	/** 点击量（大于） */
	@QueryParam(fieldName = "N.CLICK_COUNT", op = QueryOperator.GT_EQU)
	private Integer minClickCount;
	
	/** 点击量（小于） */
	@QueryParam(fieldName = "N.CLICK_COUNT", op = QueryOperator.LESS_EQU)
	private Integer maxClickCount;

	/** 新闻状态|0-删除|1-正常*/
	@QueryParam(fieldName = "N.NEWS_STATE")
	private NewsState[] newsState;

	/** 发布时间（大于） */
	@QueryParam(fieldName = "N.PUBLISH_TIME", op = QueryOperator.GT_EQU)
	private Date minPublishTime;

	/** 发布时间（小于） */
	@QueryParam(fieldName = "N.PUBLISH_TIME", op = QueryOperator.LESS_EQU)
	private Date maxPublishTime;
	
	/** 作者*/
	@QueryParam(fieldName = "N.AUTHOR", op = QueryOperator.LIKE)
	private String author;
	
	@QueryParam(fieldName = "NC.NEWS_COLUMN_ID")
	private Integer newsColumnId;
	
	@QueryParam(fieldName = "NC.NEWS_COLUMN_NAME", op = QueryOperator.LIKE)
	private String newsColumnName;
	
	@QueryParam(fieldName = "NT.NEWS_TYPE_ID")
	private Integer newsTypeId;
	
	@QueryParam(fieldName = "NT.NEWS_TYPE_NAME", op = QueryOperator.LIKE)
	private String newsTypeName;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getMinClickCount() {
		return minClickCount;
	}

	public void setMinClickCount(Integer minClickCount) {
		this.minClickCount = minClickCount;
	}

	public Integer getMaxClickCount() {
		return maxClickCount;
	}

	public void setMaxClickCount(Integer maxClickCount) {
		this.maxClickCount = maxClickCount;
	}
	
	public NewsState[] getNewsState() {
		return newsState;
	}

	public void setNewsState(NewsState... newsState) {
		if(newsState.length<1){
			this.newsState=null;
		}else{
			this.newsState = newsState;
		}
	}

	public static Integer getColumnNews() {
		return COLUMN_NEWS;
	}

	public static Integer getColumnAbout() {
		return COLUMN_ABOUT;
	}

	public Date getMinPublishTime() {
		return minPublishTime;
	}

	public void setMinPublishTime(Date minPublishTime) {
		this.minPublishTime = minPublishTime;
	}

	public Date getMaxPublishTime() {
		return maxPublishTime;
	}

	public void setMaxPublishTime(Date maxPublishTime) {
		this.maxPublishTime = maxPublishTime;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public NewsQuery orderByClickCount(boolean asc) {
		this.setO(asc ? "N.CLICK_COUNT" : "!N.CLICK_COUNT");
		return this;
	}
	
	public NewsQuery orderByPublishTime(boolean asc) {
		this.setO(asc ? "N.PUBLISH_TIME" : "!N.PUBLISH_TIME");
		return this;
	}
}
