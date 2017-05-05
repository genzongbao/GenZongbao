package ydh.news.web.query;

import ydh.cicada.query.PagableQueryCmd;
import ydh.cicada.query.api.Query;
import ydh.cicada.query.api.QueryParam;
import ydh.news.web.dict.ProjectNewsType;

@Query(select = "N.*,NC.*,NT.*,M.PROJECT_NEWS_TYPE", 
		from = "NEWS AS N RIGHT JOIN NEWS_PROJECT_MAPPING AS M ON N.NEWS_ID = M.NEWS_ID "
		+ "LEFT JOIN NEWS_COLUMN AS NC ON N.NEWS_COLUMN_ID=NC.NEWS_COLUMN_ID "
		+ "LEFT JOIN NEWS_TYPE AS NT ON N.NEWS_TYPE_ID=NT.NEWS_TYPE_ID",
		orderBy = "N.NEWS_ID asc")
public class ProjectNewsQuery extends PagableQueryCmd{
	/**新闻类型id*/
	@QueryParam(fieldName = "N.NEWS_TYPE_ID")
	private Integer newsTypeId;
	/**新闻id*/
	@QueryParam(fieldName = "N.NEWS_ID")
	private Integer newsId;
	/**项目id*/
	@QueryParam(fieldName = "M.PROJECT_ID")
	private String projectId;
	/**项目新闻类型*/
	@QueryParam(fieldName = "M.PROJECT_NEWS_TYPE")
	private ProjectNewsType projectNewsType;

	public ProjectNewsQuery() {
	}

	public Integer getNewsTypeId() {
		return newsTypeId;
	}

	public void setNewsTypeId(Integer newsTypeId) {
		this.newsTypeId = newsTypeId;
	}

	public Integer getNewsId() {
		return newsId;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public ProjectNewsType getProjectNewsType() {
		return projectNewsType;
	}

	public void setProjectNewsType(ProjectNewsType projectNewsType) {
		this.projectNewsType = projectNewsType;
	}
	
}
