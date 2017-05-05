package ydh.customer.controller;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ydh.cicada.service.CommonService;
import ydh.customer.entity.Customer;
import ydh.customer.realm.CustomerToken;
import ydh.layout.FrontLayout;
import ydh.website.localization.service.WebSiteExceptionService;

@Controller
@RequestMapping("personal")
public class CustomerController {
	@Autowired
	private FrontLayout layout;
	@Autowired
	private CommonService commonService;
	@Autowired
	private WebSiteExceptionService exceptionService;
	
	/**
	 * 修改信息
	 * @param model
	 * @param targetId
	 * @return ModelAndView
	 * @author 
	 */
	@RequestMapping("update-personal-message")
	public ModelAndView updatePersonalMessage(ModelMap model,String targetId){
		Customer customer = CustomerToken.loginCustomer();
		customer=commonService.load(Customer.class, customer.getCusId());
		model.put("customer_info", customer);
		return layout.layout("personal/customer/update-personal-message");
	}
	
	/**
	 * 修改信息
	 * @return ModelAndView
	 */
	@RequestMapping("update-message")
	public RedirectView updateMessage(ModelMap model, Customer cus, RedirectAttributes attributes){
		Customer customer=commonService.load(Customer.class, CustomerToken.loginCustomer().getCusId());
		customer.setCusId(customer.getCusId());
		customer.setCusLogName(cus.getCusLogName());
		try {
			commonService.update(customer);
			CustomerToken.updateLoginCustomer(customer);
			attributes.addFlashAttribute("alertType", "success");
			attributes.addFlashAttribute("alertMsg", "修改成功！"); 
		} catch (Exception e) {
			exceptionService.createWebSiteException("修改个人信息异常", e);
			attributes.addFlashAttribute("alertType", "danger");
			attributes.addFlashAttribute("alertMsg", "个人信息修改失败！出现未知异常，请重新尝试或联系管理员");
			e.printStackTrace();
		}
		return new RedirectView("personal-message");
	}
	
	/**
	 * 修改手机号
	 * @param model
	 * @param cus
	 * @param attributes
	 * @return
	 */
	@RequestMapping("update-phone")
	public RedirectView updatePhone(ModelMap model,Customer cus, RedirectAttributes attributes){
		cus.setCusId(CustomerToken.loginCustomer().getCusId());
		Customer customer=commonService.load(Customer.class, CustomerToken.loginCustomer().getCusId());
		customer.setCusPhone(cus.getCusPhone());
		commonService.update(customer);
		attributes.addFlashAttribute("alertType", "success");
		attributes.addFlashAttribute("alertMsg","手机号修改成功,需在此重新登录!");
		SecurityUtils.getSubject().logout();
		return new RedirectView("/auth/login",true);
	}
	
}
	
