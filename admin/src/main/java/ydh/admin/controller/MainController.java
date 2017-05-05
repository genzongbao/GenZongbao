package ydh.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import ydh.layout.MainLayout;

@Controller
@RequestMapping("admin")
public class MainController {
	@Autowired
	private MainLayout layout;
	
	@RequestMapping("main")
	public ModelAndView main() {
		return layout.layout("admin/main", "MAIN");
	}
	
	@RequestMapping("")
	public RedirectView login() {
		return new RedirectView("login");
	}
	
	
	@RequestMapping("menu")
	public String menu(String menuId) {
		return layout.menu(menuId);
	}
	
	@RequestMapping("blank")
	public ModelAndView blank() {
		return layout.layout("admin/main","MAIN");
	}
}
