package ydh.news.web.admin;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import ydh.news.web.dict.NewsState;
import ydh.news.web.entity.News;
import ydh.news.web.entity.NewsType;
import ydh.news.web.query.NewsQuery;
import ydh.cicada.dao.Page;
import ydh.cicada.query.QueryObject;
import ydh.cicada.service.CommonService;
import ydh.layout.MainLayout;
import ydh.user.entity.User;
import ydh.user.realm.UserLoginZone;
import ydh.website.localization.service.WebSiteExceptionService;

/**
 * 
 * @author chenkailun
 *
 */
@Controller
@RequestMapping("admin/news-manage")
public class NewsManageController {

	@Autowired
	private MainLayout layout;
	@Autowired
	private CommonService commonService;
	@Autowired
	private WebSiteExceptionService exceptionService;
	/**
	 * 新闻列表
	 * @param query
	 * @param menuId
	 * @param model
	 * @return
	 */
	@RequestMapping("news-list")
	public ModelAndView newsList(NewsQuery query,String menuId,ModelMap model) {
		List<NewsType> types = QueryObject.select(NewsType.class)
				.condition("NEWS_COLUMN_ID=?", query.getNewsColumnId())
				.list(commonService);
		query.orderByPublishTime(false);
		Page<News> page = commonService.find(News.class, query);
		model.put("page", page	);
		model.put("types", types);
		model.put("menuId", menuId);
		model.put("query", query);
		return layout.layout("news/news-list",menuId);
	}

	/**
	 * 新闻发布界面 
	 * @param newsColumnId
	 * @param menuId
	 * @param model
	 * @param flash
	 * @return
	 */
	@RequestMapping("news-publish-page")
	public ModelAndView newPublishPage(Integer newsColumnId,String menuId, ModelMap model, RedirectAttributes flash) {
		List<NewsType> types = QueryObject.select(NewsType.class)
				.condition("NEWS_COLUMN_ID=?", newsColumnId)
				.list(commonService);
		model.put("types", types);
		model.put("newsColumnId", newsColumnId);
		return layout.layout("news/news-publish",menuId);
	}

	/**
	 * 新闻发布操作
	 * @param news
	 * @param flash
	 */
	@RequestMapping("news-publish")
	public RedirectView newsPublish(News news, RedirectAttributes flash,String menuId) {
		try {
			User user = UserLoginZone.loginUser();
			//如果是关于我们栏目，判断该种类型数据的唯一性
			if(news.getNewsColumnId() == NewsQuery.COLUMN_ABOUT) {
				NewsQuery query = new NewsQuery();
				query.setNewsState(NewsState.NORMAL,NewsState.APPLING);
				query.setNewsTypeId(news.getNewsTypeId());
				if(commonService.count(query) > 0) {
					flash.addFlashAttribute("alertType", "error");
					flash.addFlashAttribute("alertMsg", "该类型数据已存在，请重新选择一种类型添加");
					flash.addFlashAttribute("news", news);
					return new RedirectView("news-list?newsColumnId="+news.getNewsColumnId()+"&menuId="+menuId);
				}
			}
			news.setClickCount(0);
			news.setCommentCount(0);
			news.setPraiseCount(0);
			news.setAuthor(user.getRealName());
			news.setPublishTime(new Date());
			news.setNewsState(NewsState.APPLING);
			commonService.insert(news);
			NewsType newsType= commonService.load(NewsType.class, news.getNewsTypeId());
			flash.addFlashAttribute("alertType", "info");
			flash.addFlashAttribute("alertMsg", "添加成功");
			return new RedirectView("news-list?newsColumnId="+news.getNewsColumnId()+"&menuId="+menuId);
		} catch (Exception e) {
			exceptionService.createWebSiteException("新闻发布异常", e);
			e.printStackTrace();
			flash.addFlashAttribute("alertType", "error");
			flash.addFlashAttribute("alertMsg", "出现未知异常，请重新尝试或联系管理员");
			flash.addFlashAttribute("fund", news);
			return new RedirectView("news-publish-page?newsColumnId="+news.getNewsColumnId()+"&menuId="+menuId);
		}
	}

	/**
	 * 新闻删除
	 * @param newsId
	 * @param flash
	 */
	@RequestMapping("news-remove")
	public RedirectView newsRemove(Integer newsId,String newsColumnId,String menuId,RedirectAttributes flash) {
		try {
			News news = commonService.load(News.class, newsId);
			NewsType newsType = commonService.load(NewsType.class, news.getNewsTypeId());
			this.commonService.delete(news);
			flash.addFlashAttribute("alertType", "info");
			flash.addFlashAttribute("alertMsg", "删除新闻成功");
		} catch (Exception e) {
			exceptionService.createWebSiteException("新闻删除异常", e);
			e.printStackTrace();
			flash.addFlashAttribute("alertType", "error");
			flash.addFlashAttribute("alertMsg", "出现未知异常，请重新尝试或联系管理员");
		}
		return new RedirectView("news-list?newsColumnId="+newsColumnId+"&menuId="+menuId);
	}

	/**
	 * 修改新闻状态
	 * @param newsId
	 * @param state
	 * @param flash
	 * @return 
	 */
	@RequestMapping("update-state")
	public RedirectView updateState(Integer newsId,NewsState state,String menuId, RedirectAttributes flash) {
		News news = commonService.load(News.class, newsId);
		news.setNewsState(state);
		commonService.update(news);
		NewsType newsType = commonService.load(NewsType.class, news.getNewsTypeId());
		flash.addFlashAttribute("menuId", menuId);
		flash.addFlashAttribute("alertType","success");
		flash.addFlashAttribute("alertMsg", "操作成功");
		return new RedirectView("news-list?menuId="+menuId+"&newsColumnId="+news.getNewsColumnId());
	}

	/**
	 * 新闻修改界面
	 * @param newsId
	 * @param flash
	 */
	@RequestMapping("news-edit-page")
	public ModelAndView newsEditPage(String menuId ,NewsQuery query, ModelMap model) {
		News news = commonService.load(News.class, query.getNewsId());
		List<NewsType> types = QueryObject.select(NewsType.class)
				.condition("NEWS_COLUMN_ID=?", news.getNewsColumnId())
				.list(commonService);
		model.put("news", news);
		model.put("types", types);
		model.put("newsColumnId", news.getNewsColumnId());
		return layout.layout("news/news-publish",menuId);
	}

	/**
	 * 新闻修改操作
	 * @param news
	 * @param flash
	 */
	@RequestMapping("news-edit")
	public RedirectView newsEdit(News news, RedirectAttributes flash,String menuId) {
		try {
			News oldNews = commonService.load(News.class, news.getNewsId());
			oldNews.setContent(news.getContent());
			oldNews.setTitle(news.getTitle());
			oldNews.setNewsTypeId(news.getNewsTypeId());
			oldNews.setNewsImgId(news.getNewsImgId());
			oldNews.setNewsState(NewsState.APPLING);
			oldNews.setPublishTime(new Date());
			commonService.update(oldNews);
			NewsType newsType = commonService.load(NewsType.class, news.getNewsTypeId());
			flash.addFlashAttribute("alertType", "info");
			flash.addFlashAttribute("alertMsg", "修改成功");
		} catch (Exception e) {
			exceptionService.createWebSiteException("新闻修改异常", e);
			e.printStackTrace();
			flash.addFlashAttribute("alertType", "error");
			flash.addFlashAttribute("alertMsg", "出现未知异常，请重新尝试或联系管理员");
			flash.addFlashAttribute("news", news);
		}
		return new RedirectView("news-list?newsColumnId="+news.getNewsColumnId()+"&menuId="+menuId);
	}

	/**
	 * 新闻预览
	 * @param newsId
	 * @param model
	 * @return ModelAndView
	 */
	@RequestMapping("news-look")
	public ModelAndView newsLook(Integer newsId, ModelMap model, String menuId) {
		model.put("menuId", menuId);
		model.put("news", commonService.load(News.class, newsId));
		return layout.layout("news/news-look", menuId);
	}

	/**
	 * 验证关于我们栏目的任意一个类型是否已经存在一条数据
	 * @param query
	 */
	@RequestMapping("check-about-us-required")
	@ResponseBody
	public boolean checkAboutUsRequired(NewsQuery query) {
		return commonService.count(query) > 0 ? false:true;
	}

}
