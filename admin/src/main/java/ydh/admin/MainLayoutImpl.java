package ydh.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import ydh.layout.MainLayout;
import ydh.upload.utils.UploadConfig;
import ydh.user.entity.Menu;
import ydh.user.menu.MenuManager;
import ydh.user.service.UserService;
import ydh.utils.ConfigTool;

@Service
public class MainLayoutImpl implements MainLayout {
	
	@Autowired
	private UserService userService;
	
	public ModelAndView layout(String contentView) {
		return layout(contentView, null);
	}
	
	public ModelAndView layout(String contentView, String menuId) {
		ModelAndView mav = new ModelAndView("/admin/layout");
		mav.addObject("contentView", contentView + ".vm");
		mav.addObject("menuId", menuId);
		mav.addObject("imageUrlPrefix", ConfigTool.getString(UploadConfig.imageUrlPrefix));
		MenuManager menus = MenuManager.get(userService);
		menus.touch(menuId);
		return mav;
	}
	
	public String menu(String menuId) {
		Menu menu = MenuManager.get(userService).touch(menuId);
		if (menu != null && menu.getUrl() != null) {
			return "redirect:../" + menu.getUrl();
		} else {
			return "redirect:blank";
		}
	}
	
}
