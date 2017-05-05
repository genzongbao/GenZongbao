package ydh.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ydh.cicada.dao.Page;
import ydh.cicada.query.QueryObject;
import ydh.cicada.service.CommonService;
import ydh.customer.dict.CustomerState;
import ydh.customer.entity.Account;
import ydh.customer.entity.Customer;
import ydh.customer.query.CustomerQuery;
import ydh.customer.service.AfterCustomerFreezed;
import ydh.layout.MainLayout;
import ydh.utils.ServiceProxy;
import ydh.website.localization.service.WebSiteExceptionService;

import java.util.Map;

@Controller
@RequestMapping("admin/customer")
public class CustomerAdminController {
	
	@Autowired
	private MainLayout layout;
	@Autowired
	private CommonService commonService;
	@Autowired
	private WebSiteExceptionService exceptionService;
	private AfterCustomerFreezed afterCustomerFreezed = ServiceProxy.proxy(AfterCustomerFreezed.class);
	/**
	 * 客户列表
	 * @param query
	 * @param model
	 * @return ModelAndView
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("customer-list")
	public ModelAndView customerList(CustomerQuery query, ModelMap model) {
		Page<Map> page = commonService.find(Map.class, query);
		model.put("page", page);
		model.put("query", query);
		return layout.layout("admin/customer/customer-list");
	}
	
	/**
	 * 客户基本信息
	 * @param cusId
	 * @param model
	 * @return
	 */
	@RequestMapping("customer-base-info")
	public ModelAndView customerDetail(String cusId, ModelMap model) {
		try {
			Customer customer = commonService.load(Customer.class, cusId);
				Account account = QueryObject.select(Account.class)
						 .condition("CUS_ID=?", customer.getCusId())
						 .uniqueResult(commonService);
				model.put("account", account);
			model.put("cus", customer);
		} catch (Exception e) {
			exceptionService.createWebSiteException("客户信息查询异常", e);
			e.printStackTrace();
		}
		model.put("cusId", cusId);
		return layout.layout("admin/customer/customer-base-info");
	}
	
	/**
	 * 修改customer状态(冻结用户)
	 * @param state
	 * @param cusId
	 * @param flash
	 * @return
	 */
	@RequestMapping("update-cus-state")
	public RedirectView updateCusState(String cusId, String state, RedirectAttributes flash) {
		try {
			Customer customer = commonService.load(Customer.class, cusId);
			String alertMsg="";
			if(CustomerState.LOCKED.toString().equals(state)) {
				customer.setCusState(CustomerState.LOCKED);
				alertMsg = "冻结成功";
				afterCustomerFreezed.afterCustomerFreezed(customer);
			} else if (CustomerState.NORMAL.toString().equals(state)){
				customer.setCusState(CustomerState.NORMAL);
				alertMsg = "解冻成功";
			}
			commonService.update(customer);
			flash.addFlashAttribute("alertType", "info");
			flash.addFlashAttribute("alertMsg", alertMsg);
		} catch (Exception e) {
			exceptionService.createWebSiteException("修改客户状态异常", e);
			e.printStackTrace();
			flash.addFlashAttribute("alertType", "error");
			flash.addFlashAttribute("alertMsg", "系统异常！");
		}
		return new RedirectView("customer-list");
	}
	
}
