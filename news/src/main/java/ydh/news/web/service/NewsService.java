package ydh.news.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ydh.cicada.dao.JdbcDao;
import ydh.cicada.dao.Page;
import ydh.cicada.query.QueryObject;
import ydh.news.web.dict.NewsState;
import ydh.news.web.dict.ProjectNewsType;
import ydh.news.web.entity.News;

/**
 * 新闻Service
 * @author 张以孟 李永炳
 *
 */
@Service
public class NewsService {
	
	@Autowired
	JdbcDao jdbcDao;
	
	
	/**
	 * 获取最后一个news
	 * @return News
	 */
	public News selectLastNews(){
		return QueryObject.select(News.class)
					      .desc("NEWS_ID").firstResult(jdbcDao);
				
	}
	
	/**
	 * 根据编号获取新闻
	 * @return News
	 */
	public News selectNewsById(String newsId){
		return QueryObject.select(News.class)
					      .condition("NEWS_ID=?", newsId)
					      .firstResult(jdbcDao);
	}
	
	/**
	 * 通过newsTypeId查询NEWS（最新）时间逆序
	 * @param newsTypeId sort(排序方式)
	 * @return Page<News>
	 */
	public Page<News> selectNewsByNewsTypeId(Integer newsTypeId,Integer newsColumnId,String sort){
		return QueryObject.select(News.class)
				.condition("NEWS_TYPE_ID=?", newsTypeId)
				.condition("NEWS_COLUMN_ID=?", newsColumnId)
				.condition("NEWS_STATE=?", NewsState.NORMAL)
				.desc(sort)
				.find(jdbcDao);
	}
	/**
	 * 通过projectId查询所有项目相关事件;股东看的信息
	 * @param projectId
	 * @return
	 */
	public List<News> selectNewsByProejctId(String projectId){
		return QueryObject.select(News.class)
				.from("NEWS N LEFT JOIN NEWS_PROJECT_MAPPING M ON N.NEWS_ID = M.NEWS_ID")
				.condition("PROJECT_ID=?", projectId)
				.condition("NEWS_STATE IN("+NewsState.NORMAL.ordinal()+")")
				.desc("PUBLISH_TIME")
				.list(jdbcDao);
	}
	/***
	 * 通过projectId查询所有项目相关事件;普通人员
	 * @param projectId
	 * @return
	 */
	public List<News> selectNewsByProejct(String projectId){
		return QueryObject.select(News.class)
				.from("NEWS N LEFT JOIN NEWS_PROJECT_MAPPING M ON N.NEWS_ID = M.NEWS_ID")
				.condition("PROJECT_ID=?", projectId)
				.condition("NEWS_STATE IN("+NewsState.NORMAL.ordinal()+")")
				.condition("PROJECT_NEWS_TYPE IN("+ProjectNewsType.PUBLIC.ordinal()+")")
				.desc("PUBLISH_TIME")
				.list(jdbcDao);
	}
	
}
