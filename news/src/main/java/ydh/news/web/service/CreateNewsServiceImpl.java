package ydh.news.web.service;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import ydh.cicada.service.CommonService;
import ydh.customer.dict.CustomerMessageType;
import ydh.customer.entity.Customer;
import ydh.message.push.BroadcastMessage;
import ydh.news.web.dict.NewsState;
import ydh.news.web.entity.News;
import ydh.news.web.entity.NewsType;
import ydh.news.web.query.NewsTypeQuery;
import ydh.website.localization.service.WebSiteExceptionService;

@Service
public class CreateNewsServiceImpl {
	private static final Logger logger = LoggerFactory.getLogger(CreateNewsServiceImpl.class);
	@Autowired
	private NewsService newsService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private WebSiteExceptionService exceptionService;

	
	public void createNews(String userName, CustomerMessageType messageType, String... args) {
		Assert.isTrue(messageType.messageType() == CustomerMessageType.TYPE_NEWS);
		try{
			BroadcastMessage<Customer, CustomerMessageType> broadcastMessage = 
					new BroadcastMessage<Customer, CustomerMessageType>(CustomerMessageType.NEW_PROJECT_ONLINE,null,args);
			//写入一条新标预告的公告新闻
			NewsTypeQuery type = new NewsTypeQuery();
			type.setNewsTypeName("公告");
			NewsType t = commonService.firstResult(NewsType.class,type);
			News news = new News();
			news.setNewsColumnId(1);//1新闻动态2关于我们
			news.setTitle(broadcastMessage.getTitle());
			news.setContent(broadcastMessage.getContent());
			news.setPublishTime(new Date());
			news.setAuthor(userName);
			news.setNewsState(NewsState.NORMAL);
			news.setClickCount(0);
			news.setCommentCount(0);
			news.setPraiseCount(0);
			commonService.insert(news);
			News lastNews = newsService.selectLastNews();
			broadcastMessage.setMessageId(lastNews.getNewsId().toString());
		}catch(Exception e){
			exceptionService.createWebSiteException("新建新闻异常", e);
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
