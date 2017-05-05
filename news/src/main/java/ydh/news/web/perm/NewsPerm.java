package ydh.news.web.perm;

import ydh.base.perm.Perm;
import ydh.base.perm.PermOperator;
import ydh.base.perm.PermResource;

@Perm(type="news", name = "新闻管理")
public class NewsPerm {

	@PermResource(name="新闻管理")
	public static final class News {
		@PermOperator(name = "列表")
		public static final String list = "News:list";
		@PermOperator(name = "添加")
		public static final String add = "News:add";
		@PermOperator(name = "审核")
		public static final String audit = "News:audit";
		@PermOperator(name = "编辑")
		public static final String edit = "News:edit";
		@PermOperator(name = "删除")
		public static final String delete = "News:delete";
		@PermOperator(name = "预览")
		public static final String look = "News:look";
	}
	
	@PermResource(name="关于我们")
	public static final class AboutUs {
		@PermOperator(name = "列表")
		public static final String list = "AboutUs:list";
	}
	@PermResource(name="首页轮播管理")
	public static final class Banner {
		@PermOperator(name = "列表")
		public static final String list = "Banner:list";
		@PermOperator(name = "添加")
		public static final String add = "Banner:add";
		@PermOperator(name = "删除")
		public static final String delete = "Banner:delete";
		@PermOperator(name = "修改")
		public static final String update = "Banner:update";
	}
}
