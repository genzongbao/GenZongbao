package ydh.user.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import ydh.user.entity.Menu;
import ydh.user.service.UserService;
import ydh.website.localization.service.WebSiteExceptionService;


public class MenuManager {
	public static String MENUS_ATTRIBUTE_NAME = "menus";
	private List<Menu> menuList;
	private List<Menu> topList;
	private List<Menu> submenuList;
	private Menu current;
	private Menu currentTop;
	@Autowired
	private WebSiteExceptionService exceptionService;
	private MenuManager() {
	}
	
	public static MenuManager get(UserService userService) {
		Subject subject = SecurityUtils.getSubject();
		MenuManager menus = (MenuManager)subject.getSession().getAttribute(MENUS_ATTRIBUTE_NAME);
		if (menus == null) {
			menus = new MenuManager();
			menus.loadData(userService);
			subject.getSession().setAttribute(MENUS_ATTRIBUTE_NAME, menus);
		}
		return menus;
	}
	
	private void loadData(UserService authService) {
		Subject subject = SecurityUtils.getSubject();
		List<Menu> menuList = authService.listMenu();
		this.topList = new ArrayList<Menu>();
		for (Iterator<Menu> i = menuList.iterator(); i.hasNext();) {
			Menu menu = i.next();
			String permission = menu.getPermissionCode();
			if (permission != null ) {
				try {
					if(! subject.isPermitted(permission)) {
						i.remove();
					}
				} catch (Exception e) {
					exceptionService.createWebSiteException("LoadMenu异常", e);
					i.remove();
				}
			} else if (menu.getParentMenuId() == null) {
				this.topList.add(menu);
			}
		}
		this.menuList = menuList;
		romeveEmptyTopMenu();
		this.submenuList = new ArrayList<Menu>();
	}
	
	private void romeveEmptyTopMenu() {
		for (Iterator<Menu> i = this.topList.iterator(); i.hasNext();) {
			Menu top = i.next();
			boolean flag = false;
			for (Menu menu : this.menuList) {
				if(top.getMenuId().equals(menu.getParentMenuId())) {
					flag = true;
				}
			}
			if(!flag){
				i.remove();
				this.menuList.remove(top);
			}
		}
	}
	
	public Menu touch(String menuId) {
		if (menuId == null) return null;
		// 查找点击的菜单
		for (Iterator<Menu> i = menuList.iterator(); i.hasNext();) {
			Menu menu = i.next();
			if (menuId.equals(menu.getMenuId())) {
				current = menu;
				break;
			}
		}
		//如果没找到则直接退出
		if (current == null) return null;
		if (current.getParentMenuId() == null) {
			//如果点击的是主菜单
			currentTop = current;
			this.submenuList.clear();
			for (Iterator<Menu> i = menuList.iterator(); i.hasNext();) {
				Menu menu = i.next();
				if (menu.getParentMenuId() != null) {
					if (menu.getParentMenuId().equals(currentTop.getMenuId())) {
						submenuList.add(menu);
					}
				}
			}
			//选中子菜单的第一个
			if ( ! submenuList.isEmpty()) {
				current = submenuList.get(0);
			} else {
				current = null;
			}
		} else {
			//点击的是子菜单
			this.submenuList.clear();
			this.currentTop = null;
			for (Iterator<Menu> i = menuList.iterator(); i.hasNext();) {
				Menu menu = i.next();
				if (menu.getParentMenuId() != null) {
					if (menu.getParentMenuId().equals(current.getParentMenuId())) {
						submenuList.add(menu);
					}
				} else {
					if (menu.getMenuId().equals(current.getParentMenuId())) {
						this.currentTop = menu;
					}
				}
			}
		}
		return current;
	}
	
	public List<Menu> getMenuList() {
		return menuList;
	}

	public List<Menu> getTopList() {
		return topList;
	}

	public List<Menu> getSubmenuList() {
		return submenuList;
	}

	public Menu getCurrent() {
		return current;
	}

	public Menu getCurrentTop() {
		return currentTop;
	}
}
