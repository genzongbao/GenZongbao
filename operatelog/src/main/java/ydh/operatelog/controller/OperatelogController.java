package ydh.operatelog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ydh.cicada.dao.Page;
import ydh.cicada.service.CommonService;
import ydh.layout.MainLayout;
import ydh.operatelog.entity.OperateLog;
import ydh.operatelog.query.OperateLogQuery;

@Controller
@RequestMapping("admin/operatelog")
public class OperatelogController {

	@Autowired
	private MainLayout layout;

	@Autowired
	private CommonService commonService;

	@SuppressWarnings("rawtypes")
	@RequestMapping("operatelog-list")
	public ModelAndView operatelogList(ModelMap model,OperateLogQuery query){
		Page page = commonService.find(OperateLog.class, query);
		model.put("page", page);
		model.put("query", query);
		return layout.layout("operatelog/operatelog-list");
	}

}