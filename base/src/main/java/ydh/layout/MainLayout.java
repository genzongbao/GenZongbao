package ydh.layout;

import org.springframework.web.servlet.ModelAndView;

public interface MainLayout {
	
	/**
	 * 根据传入页面地址生成布局输出(不修改当前选中菜单)
	 * @param contentView
	 * @return
	 */
	public ModelAndView layout(String contentView);
	
	/**
	 * 根据传入页面地址生成布局输出
	 * @param contentView 内容页面地址
	 * @param menuId      选中菜单ID
	 * @return
	 */
	public ModelAndView layout(String contentView, String menuId);
	
	/**
	 * 菜单点击时处理菜单跳转
	 * @param menuId
	 * @return
	 */
	public String menu(String menuId);
}
