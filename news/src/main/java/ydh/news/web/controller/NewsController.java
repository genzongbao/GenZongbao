package ydh.news.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ydh.cicada.service.CommonService;
import ydh.layout.FrontLayout;
import ydh.news.web.service.NewsService;
import ydh.website.localization.service.WebSiteExceptionService;

/**
 * 查询新闻，用户购买记录
 * @author Administrator
 *
 */
@Controller
@RequestMapping("news")
@SuppressWarnings("rawtypes")
public class NewsController {

	@Autowired
	private FrontLayout layout;
	@Autowired
	private NewsService newsService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private WebSiteExceptionService exceptionService;
//	/**
//	 * 判断是否需要开启评论
//	 * */
//	private boolean allowComment(){
//		return ConfigTool.getBoolean(NewsConfig.allowComment);
//	}
//	
//	/**
//	 * 跳转到公共页面
//	 * @param model
//	 * @return ModelAndView
//	 */
//	@RequestMapping(value="news-item", method=RequestMethod.GET)
//	public ModelAndView newsItem(NewsQuery query ,ModelMap model){
//		model=weixinNewsItem(query,model);
//		return layout.layout("news/news-item",getMenuId(query.getNewsTypeId()));
//	}
//	/**
//	 * 跳转到公共页面微信
//	 * @param model
//	 * @return ModelAndView
//	 */
//	@SuppressWarnings("unchecked")
//	@RequestMapping("weixin-news-item")
//	private ModelMap weixinNewsItem(NewsQuery query ,ModelMap model){
//		query.setNewsState(NewsState.NORMAL);
//		List crowds=new ArrayList();
//		if(StringUtils.isEmpty(query.getNewsTypeId())){
//			//按点击量排序query
//			query.setNewsTypeId(2);
//			query.orderByClickCount(false);
//			Page<Map> newsMap = commonService.find(Map.class, query);
//			for(Map obj:newsMap.getData()){
//				crowds.add(obj);
//			}
//			model.put("page",newsMap);
//			model.put("crowds", crowds);
//			model.put("query", query);
//			query.setNewsTypeId(0);
//		} else {
//			if(StringUtils.isEmpty(query.getNewsColumnId())){
//				query.setNewsColumnId(1);
//			}
//			query.orderByPublishTime(false);
//			Page<Map> newsMap = commonService.find(Map.class, query);
//			for(Map obj:newsMap.getData()){
//				crowds.add(obj);
//			}
//			//按时间排序
//			model.put("page",newsMap);
//			model.put("crowds", crowds);
//			model.put("query", query);
//		}
//		model.put("type", query.getNewsTypeId());
//		model.put("imageUrlPrefix", ConfigTool.getString(UploadConfig.imageUrlPrefix));
//		
//		return model;
//	}
//
//	/**
//	 * 成功案例
//	 * @param model
//	 * @param query
//	 * @return
//	 */
//	@RequestMapping(value="successful-case", method=RequestMethod.GET)
//	public ModelAndView successfulCase(ModelMap model, NewsCommonQuery query){
//		query.setNewsState(NewsState.NORMAL);
//		model.put("page", commonService.find(News.class,query));
//		model.put("imageUrlPrefix", ConfigTool.getString(UploadConfig.imageUrlPrefix));
//		return layout.layout("successful-case/successful-case","successful-case");
//	}
//	
//	/**
//	 * 抢标专栏
//	 * @param model
//	 * @param query
//	 * @return
//	 */
//	@RequestMapping(value="grab-column", method=RequestMethod.GET)
//	public ModelAndView grabColumn(ModelMap model, NewsCommonQuery query){
//		query.setS(6);
//		query.setNewsState(NewsState.NORMAL);
//		model.put("page", commonService.find(News.class,query));
//		model.put("imageUrlPrefix", ConfigTool.getString(UploadConfig.imageUrlPrefix));
//		return layout.layout("successful-case/successful-case","grab");
//	}
//	/**
//	 * 查看新闻详情  新闻模块
//	 * @param model
//	 * @param query
//	 * @return ModelAndView
//	 */
//	@RequestMapping(value="news-detail")
//	public ModelAndView newsDetail(NewsCommonQuery query, Integer newsTypeId, ModelMap model){
//		News news = this.commonService.load(News.class, query.getNewsId());
//		news.setClickCount(news.getClickCount()+1);
//		commonService.update(news);
//		model.put("news",news);
//		model.put("type",newsTypeId);
//		if(!this.allowComment()){
//			return layout.layout("news/news-detail",getMenuId(newsTypeId));
//		}
//		Customer customer = CustomerToken.loginCustomer(); 
//		boolean loginFlag=true;
//		model.put("newsPraiseFlag",false);
//		//判断是否登录
//		if(null == customer){
//			loginFlag=false;
//		} else {
//			//查询新闻是否点赞
//			long newsPraisecount = this.newsService.selectNewsPraise(query.getNewsId(), customer.getCusId());
//			if(0 != newsPraisecount){
//				model.put("newsPraiseFlag",true);
//			}
//		}
//		model.put("loginFlag",loginFlag);
//		List<Map> comments=new ArrayList<Map>();
//		Page<NewsComment> newsComments=this.commonService.find(NewsComment.class, query);
//		for (NewsComment newsComment : newsComments.getData()) {
//			Map<String, Object> map=new HashMap<String, Object>();
//			List<NewsCommentReply> replys= this.newsService.selectNewsCommentReplys(newsComment.getNewsCommentId());
//			map.put("replys", replys);
//			map.put("newsComment", newsComment);
//			boolean newsCommentPraiseFlag=false;
//			//查询评论是否点赞
//			if(loginFlag){
//				long newsCommentPraiseCount = this.newsService.selectNewsCommentPraise(newsComment.getNewsCommentId(), customer.getCusId());
//				if(0 != newsCommentPraiseCount){
//					newsCommentPraiseFlag = true;
//				}
//			}
//			map.put("newsCommentPraiseFlag", newsCommentPraiseFlag);
//			comments.add(map);
//		}
//		model.put("comments", comments);
//		model.put("page", newsComments);
//		model.put("menuId", getMenuId(news.getNewsTypeId()));
//		return layout.layout("news/news-detail-comment",getMenuId(news.getNewsTypeId()));
//	}
//	/**
//	 * 新闻详情 项目新闻
//	 * @param model
//	 * @param query
//	 * @param newsTypeId
//	 * @return
//	 */
//	@RequestMapping(value="news-details")
//	public ModelAndView newsDetails(ModelMap model, NewsCommentQuery query, Integer newsTypeId){
//		News news = this.commonService.load(News.class, query.getNewsId());
//		news.setClickCount(news.getClickCount()+1);
//		commonService.update(news);
//		model.put("news",news);
//		model.put("type",newsTypeId);
//		if(!this.allowComment()){
//			return layout.layout("news/news-detail","news");
//		}
//		Customer customer = CustomerToken.loginCustomer(); 
//		boolean loginFlag=true;
//		model.put("newsPraiseFlag",false);
//		//判断是否登录
//		if(null == customer){loginFlag=false;} else {
//			//查询新闻是否点赞
//			long newsPraisecount = this.newsService.selectNewsPraise(query.getNewsId(), customer.getCusId());
//			if(0 != newsPraisecount) model.put("newsPraiseFlag",true);
//		}
//		model.put("loginFlag",loginFlag);
//		List<Map> comments=new ArrayList<Map>();
//		Page<NewsComment> newsComments=this.commonService.find(NewsComment.class, query);
//		for (NewsComment newsComment : newsComments.getData()) {
//			Map<String, Object> map=new HashMap<String, Object>();
//			List<NewsCommentReply> replys= this.newsService.selectNewsCommentReplys(newsComment.getNewsCommentId());
//			map.put("newsComment", newsComment);
//			map.put("replys", replys);
//			boolean newsCommentPraiseFlag=false;
//			//查询评论是否点赞
//			if(loginFlag){
//				long newsCommentPraiseCount = this.newsService.selectNewsCommentPraise(newsComment.getNewsCommentId(), customer.getCusId());
//				if(0 != newsCommentPraiseCount) newsCommentPraiseFlag = true;
//			}
//			map.put("newsCommentPraiseFlag", newsCommentPraiseFlag);
//			comments.add(map);
//		}
//		model.put("comments", comments);
//		model.put("page", newsComments);
//		model.put("menuId", "investment");
//		return new ModelAndView("news/news-detail-comment-content");
//
//	}
//
//	/**
//	 * 保存回复内容
//	 * @param reply
//	 * @return
//	 * @throws UnsupportedEncodingException
//	 */
//	@RequestMapping(value="save-publish", method=RequestMethod.POST)
//	public @ResponseBody String savePublish(NewsCommentReply reply) throws UnsupportedEncodingException{
//		Customer customer = CustomerToken.loginCustomer();
//		if(null == customer){
//			return "false";
//		}
//		if("/".equals(reply.getReplyCusName())){
//			reply.setReplyCusName(null);
//		}
//		reply.setCusName(customer.getCusName());
//		reply.setCommentDate(new Date());
//		commonService.insert(reply);
//		return "true";
//	}
//
//	/**
//	 * 保存新闻评论
//	 * @param newsComment
//	 * @return
//	 */
//	@RequestMapping(value="save-comment", method=RequestMethod.POST)
//	public@ResponseBody String saveComment(NewsComment newsComment) {
//		Customer customer = CustomerToken.loginCustomer();
//		if(null == customer){
//			return "false";
//		}
//		newsComment.setCusName(customer.getCusName());
//		newsComment.setNewsCommentDate(new Date());
//		newsComment.setNewsCommentPraiseCount(0);
//		this.commonService.insert(newsComment);
//		News news=commonService.load(News.class, newsComment.getNewsId());
//		news.setCommentCount(news.getCommentCount()+1);
//		commonService.update(news);
//		return "true";
//	}
//
//	/**
//	 * 新闻点赞
//	 * @param newsPraise
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping("praise-news")
//	public String praiseNews(NewsPraiseQuery query){
//		Customer customer = CustomerToken.loginCustomer();
//		String state="";
//		if(null == customer){
//			return "false";
//		}
//		query.setCusId(customer.getCusId());
//		NewsPraise _newsPraise=commonService.firstResult(NewsPraise.class, query);
//		News news = commonService.load(News.class, query.getNewsId());
//		try {
//			if(null == _newsPraise){
//				NewsPraise newsPraise=new NewsPraise();
//				newsPraise.setNewsId(news.getNewsId());
//				newsPraise.setCusId(customer.getCusId());
//				newsPraise.setNewsPriseDate(new Date());
//				commonService.insert(newsPraise);
//				news.setPraiseCount(news.getPraiseCount()+1);
//				state="praised";
//			}else{
//				commonService.delete(_newsPraise);
//				news.setPraiseCount(news.getPraiseCount()-1);
//				state="praise";
//			}
//		} catch (Exception e) {
//			exceptionService.createWebSiteException("点赞异常", e);
//			e.getMessage();
//			e.printStackTrace();
//		}
//		this.commonService.update(news);
//		return state;
//	}
//
//	/**
//	 * 评论点赞
//	 * @param newsCommentPraise
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping("praise-comment")
//	public String praiseComment(NewsCommentPraiseQuery query){
//		Customer customer = CustomerToken.loginCustomer();
//		String state="";
//		if(null == customer){
//			return "false";
//		}
//		query.setCusId(customer.getCusId());
//		NewsCommentPraise _newsCommentPraise=commonService.firstResult(NewsCommentPraise.class, query);
//		NewsComment comment = this.commonService.load(NewsComment.class, query.getNewsCommentId());
//		if(null == _newsCommentPraise){
//			NewsCommentPraise newsCommentPraise=new NewsCommentPraise();
//			newsCommentPraise.setCusId(customer.getCusId());
//			newsCommentPraise.setNewsCommentId(comment.getNewsCommentId());
//			newsCommentPraise.setNewsCommentPriseDate(new Date());
//			newsCommentPraise.setNewsId(comment.getNewsId());
//			commonService.insert(newsCommentPraise);
//			comment.setNewsCommentPraiseCount(comment.getNewsCommentPraiseCount()+1);
//			state="praised";
//		}else{
//			commonService.delete(_newsCommentPraise);
//			comment.setNewsCommentPraiseCount(comment.getNewsCommentPraiseCount()-1);
//			state="praise";
//		}
//		this.commonService.update(comment); 
//		return state;
//	}
//	
//	/**
//	 * 判断返回的菜单id 
//	 * @param newsTypeId
//	 * @return
//	 */
//	public String getMenuId(Integer newsTypeId){
//		String menuId="";
//		if(!StringUtils.isEmpty(newsTypeId)){
//			if(newsTypeId == 3){
//				menuId = "commonWeal";//丰盈微信
//			} else {
//				//如果project.name 不等于 crowdfunding 则返回的菜单id为新闻
//				if(ConfigTool.getString("project.name").equals("crowdfunding")){
//					if(newsTypeId == 1){
//						menuId = "abundance";
//					}else if(newsTypeId == 2){
//						menuId = "successful-case";
//					}
//				}else{
//					menuId = "news";
//				}
//			}
//		}
//		return menuId;
//	}
}
