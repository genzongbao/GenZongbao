package ydh.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ydh.auth.ExSavedRequest;
import ydh.cicada.query.QueryObject;
import ydh.cicada.service.CommonService;
import ydh.customer.dict.CustomerState;
import ydh.customer.entity.Customer;
import ydh.customer.form.LoginForm;
import ydh.customer.realm.CustomerToken;
import ydh.customer.service.CustomerService;
import ydh.service.InitCustomerService;
import ydh.sms.SmsTool;
import ydh.sms.utils.DateUtil;
import ydh.utils.*;
import ydh.website.localization.controller.BaseController;
import ydh.weixin.entity.WeiXinConstant;
import ydh.weixin.service.ILinkService;
import ydh.weixin.template.CreatTemplate;
import ydh.weixin.util.WeiXinUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录、注册相关
 * @author Lixl
 */
@Controller
@RequestMapping("auth")
public class AuthController extends BaseController{

	public static final String SESSION_KEY = "loginCustomer";

	static Logger logger = LoggerFactory.getLogger(AuthController.class);
	@Autowired
	private CustomerService customerService;
	@Autowired
	private InitCustomerService initCustomerService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ILinkService linkService;
	
	/**
	 * 登录方法
	 * @param loginForm
	 * @param location 位置标识
	 * @param loginBox 首页登录
	 * @param model
	 * @param flash
	 * @param request
	 * @return
	 */
	@RequestMapping(value="login", method=RequestMethod.POST)
	public RedirectView login(LoginForm loginForm, RedirectAttributes flash, HttpServletRequest request) {
		try {
			Subject subject = SecurityUtils.getSubject();
			CustomerToken token = new CustomerToken(loginForm, customerService, request);
			subject.login(token);
//			afterLoginProxy.afterLogin();
			return new RedirectView(request.getContextPath()+"/");
		} catch (AuthenticationException e) {
			flash.addFlashAttribute("alertType", "danger");
			flash.addFlashAttribute("alertMsg", e.getMessage());
			flash.addFlashAttribute("cusLogName", loginForm.getCusLogName());
			flash.addFlashAttribute("loginForm", loginForm);
			return new RedirectView("login");
		}
	}
	
	/**
	 * 微信登录
	 * @param loginForm
	 * @param flash
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "static-access", "rawtypes" })
	@RequestMapping(value="/wxlogin", method=RequestMethod.POST,produces = "text/html;charset=utf8")
	@ResponseBody
	public String wxlogin(LoginForm loginForm, HttpServletRequest request, HttpServletResponse response) {
		String openId="";
		if(session.getAttribute("openId")!=null){
			openId=(String) session.getAttribute("openId");
			if(StringUtils.isBlank(openId)){
				this.returnResult.setMsg("绑定账号失败！请重新绑定");
				this.returnResult.setError(true);
				return GsonUtil.toJSONString(returnResult);
			}
		}
		Subject subject = SecurityUtils.getSubject();
		CustomerToken token = new CustomerToken(loginForm, customerService, request);
		subject.login(token);
		Customer cus=token.loginCustomer();
		if(cus!=null){
			request.getSession().setAttribute("customer", cus);
			this.returnResult.setData(cus);
			if(StringUtils.isBlank(cus.getWxOpenId())){	
				cus.setWxOpenId(openId);
				this.commonService.update(cus);
				returnResult.setMsg("绑定成功！账号名："+cus.getCusLogName());
				returnResult.setError(false);
				String[] temData = {"您的微信已与跟踪宝系统绑定", cus.getCusPhone(), cus.getAccountType().title(), DateUtil.dateToStr(nowDate), "非本人请联系跟踪宝客服"};
                linkService.SendTemplateMessage(WeiXinConstant.BANGDING_SUCCESS_Templage, openId, "", CreatTemplate.createBindingInvite(temData));
				return resultVal();
			}
		}
		return resultVal();
	}
	
	@RequestMapping(value = "/getJSTicket", method = RequestMethod.GET, produces = "text/html;charset=utf8")
    @ResponseBody
    public String get() {
        String ticket = linkService.getJSTicket();
        System.out.println("jsapi_ticket===" + ticket);
        //获取完整的URL地址
        Map data = WeiXinUtil.getJSPTicket(ticket, WeiXinConstant.JSSDK_SAFE_WEB);
        return GsonUtil.toJSONString(data);
    }
	/**
	 * 获取用户微信openId
	 * @param code
	 * @param state
	 * @return
	 */
	@RequestMapping(value = "/getOpen", method = RequestMethod.GET, produces = "text/html;charset=utf8")
    public String get(String code, String state) {
		session.setAttribute("isBingding", true);
        if (code != null && !"".equals(code)) {
            String openId = linkService.getWebAccessToken(code);
            if(StringUtils.isBlank(openId)){
            	session.setAttribute("openIdResult", false);
            }else{
            	session.setAttribute("openIdResult", true);
            	session.setAttribute("openId", openId);   
            	if(this.customerService.CheckIsBinding(openId)>0){
            		return "auth/wxMessage";
            	}
            }
        }
        return "auth/login";
    }
	
	/**
	 * app登录
	 * @param loginForm
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "static-access", "unchecked"})
	@RequestMapping(value="app-login", method=RequestMethod.POST, produces = "text/html;charset=utf8")
	@ResponseBody
	public String appLogin(LoginForm loginForm, HttpServletRequest request, HttpServletResponse response) {
		Subject subject = SecurityUtils.getSubject();
		CustomerToken token = new CustomerToken(loginForm, customerService, request);
		subject.login(token);
		request.getSession().setAttribute(CustomerToken.SESSION_KEY, token.loginCustomer());
		returnResult.setData(token.loginCustomer());
		returnResult.setMsg("登录成功");
		return resultVal();
	}
	
	@RequestMapping(value="login", method=RequestMethod.GET)
	public ModelAndView loginPage(LoginForm loginForm, ModelMap model, RedirectAttributes flash, HttpServletRequest request) {
		return new ModelAndView("auth/login");
	}


//	/**
//	 * 忘记密码方法
//	 * @param customer  包含页面存储的数据
//	 * @param validcode 输入的手机验证
//	 * @param flash     重定向参数
//	 * @param request
//	 * @return RedirectView
//	 */
//	@RequestMapping(value="forget-password", method=RequestMethod.POST)
//	public RedirectView forgetPassword(Customer customer, String validcode,String weixin, RedirectAttributes flash,HttpServletRequest request) {
//		boolean passValidata = true;
//		Customer customerbyphone = customerService.queryCustomerByPhone(customer.getCusPhone());
//		try {
//			//通过就根据用户Id修改他的密码并跳转到登录页面
//			if(passValidata){
//				String newPwdMd5 = EncryptUtil.md5(customer.getCusPassword());
//				int result = customerService.updatePwd(customerbyphone.getCusId(),newPwdMd5);
//				if(result<=0){
//					//表示没有修改到
//					flash.addFlashAttribute("alertType", "danger");
//					flash.addFlashAttribute("alertMsg","密码修改失败");
//				}else{
//					flash.addFlashAttribute("alertType", "success");
//					flash.addFlashAttribute("alertMsg","密码修改成功,需在此重新登录");
//					SecurityUtils.getSubject().logout();
//					if(weixin!=null && weixin.equals("true")){
//						return new RedirectView("mobile-login");
//					}else{
//						return new RedirectView("login");
//					}
//					
//				}
//			}
//		} catch (Exception e) {
//			exceptionService.createWebSiteException("找回密码异常", e);
//			e.printStackTrace();
//		}
//		return new RedirectView("forget-password");
//	}

	/**
	 * 注册页面
	 * @param model
	 * @return ModelAndView
	 */
	@RequestMapping(value="register", method=RequestMethod.GET)
	public ModelAndView register(HttpServletRequest request, ModelMap model,Integer cusId, String path, String member) {
		Subject subject = SecurityUtils.getSubject();
		if(null != member && !member.equals("")){
			subject.getSession().setAttribute("registerNum",member);
		}
		if(cusId != null){
			subject.getSession().removeAttribute("register-cus-id");
			subject.getSession().removeAttribute("register-name");
			Customer customer = QueryObject.select(Customer.class).cond("CUS_ID").equ(cusId).firstResult(commonService);
			if(customer != null){
				model.put("name", customer.getCusLogName());
				model.put("referrerId", customer.getCusId());
				subject.getSession().setAttribute("register-cus-id",customer.getCusId());
				subject.getSession().setAttribute("register-name",customer.getCusLogName());
			} else {
				model.put("alertMsg", "推荐链接错误,请核对后注册,若继续注册则将视为无推荐人");
				model.put("alertType", "error");
			}
		}
		if (StringUtils.isNotEmpty(path)) {
			subject.getSession().setAttribute(WebUtils.SAVED_REQUEST_KEY, new ExSavedRequest(request, request.getContextPath() + path));
		}
		if(null != subject.getSession().getAttribute("register-name") && null != subject.getSession().getAttribute("register-cus-id")){
			model.put("name", subject.getSession().getAttribute("register-name").toString());
			model.put("referrerId", subject.getSession().getAttribute("register-cus-id").toString());
		}
		return new ModelAndView("/auth/register", null);
	}
	
	/**
	 * 企业注册页面
	 * @param model
	 * @return ModelAndView
	 */
	@RequestMapping(value="companyRegister", method=RequestMethod.GET)
	public ModelAndView companyRegister(HttpServletRequest request, ModelMap model,Integer cusId, String path, String member) {
		Subject subject = SecurityUtils.getSubject();
		if(null != member && !member.equals("")){
			subject.getSession().setAttribute("registerNum",member);
		}
		if(cusId != null){
			subject.getSession().removeAttribute("register-cus-id");
			subject.getSession().removeAttribute("register-name");
			Customer customer = QueryObject.select(Customer.class).cond("CUS_ID").equ(cusId).firstResult(commonService);
			if(customer != null){
				model.put("name", customer.getCusLogName());
				model.put("referrerId", customer.getCusId());
				subject.getSession().setAttribute("register-cus-id",customer.getCusId());
				subject.getSession().setAttribute("register-name",customer.getCusLogName());
			} else {
				model.put("alertMsg", "推荐链接错误,请核对后注册,若继续注册则将视为无推荐人");
				model.put("alertType", "error");
			}
		}
		if (StringUtils.isNotEmpty(path)) {
			subject.getSession().setAttribute(WebUtils.SAVED_REQUEST_KEY, new ExSavedRequest(request, request.getContextPath() + path));
		}
		if(null != subject.getSession().getAttribute("register-name") && null != subject.getSession().getAttribute("register-cus-id")){
			model.put("name", subject.getSession().getAttribute("register-name").toString());
			model.put("referrerId", subject.getSession().getAttribute("register-cus-id").toString());
		}
		return new ModelAndView("/auth/companyRegister", null);
	}
	/**
	 * 注册方法 
	 * @param customer
	 * @param validcode
	 * @param flash
	 * @param request
	 * @return RedirectView
	 * @throws Exception 
	 */
	@RequestMapping(value="register", method=RequestMethod.POST)
	public RedirectView register(Customer customer,String validcode, RedirectAttributes flash, HttpServletRequest request,ModelMap model) throws Exception {
		if(customer.getCusPhone() == null || customer.getCusPhone().length() != 11){
			flash.addFlashAttribute("alertMsg", "请填写正确的手机号。");
			flash.addFlashAttribute("alertType", "failed");
			return new RedirectView("register");
		}
		try {
			Double.parseDouble(customer.getCusPhone());
		} catch (Exception e) {
			flash.addFlashAttribute("alertMsg", "请填写正确的手机号。");
			flash.addFlashAttribute("alertType", "failed");
			return new RedirectView("register");
		}
		if(customerService.queryCustomerByLogName(customer.getCusLogName()) != null){
			flash.addFlashAttribute("alertMsg", "该用户昵称已被注册,请变更用户昵称后重新注册。");
			flash.addFlashAttribute("alertType", "failed");
			return new RedirectView("register");
		}
		Customer sysCustomer = customerService.queryCustomerByPhone(customer.getCusPhone());
		String pwd = customer.getCusPassword();
		if(sysCustomer != null){
			if(sysCustomer.getCusState() != CustomerState.TEMP){
				flash.addFlashAttribute("alertMsg", "该手机号码已被注册,请更换手机号码后重新注册。");
				flash.addFlashAttribute("alertType", "failed");
				return new RedirectView("register");
			} else {//临时账号
				customer.setCusRegDatetime(new Date());
				sysCustomer.setCusLogName(customer.getCusLogName());
				sysCustomer.setCusPassword(pwd);
				sysCustomer.setCusState(CustomerState.NORMAL);
				sysCustomer.setCusHeadImg("0.png");
				this.commonService.update(sysCustomer);
			}
		} else {
			customer.setCusRegDatetime(new Date());
			customer.setCusPassword(EncryptUtil.md5(pwd));
			customer.setCusState(CustomerState.NORMAL);
			customer.setCusHeadImg("0.png");
			this.commonService.insert(customer);
		}
		//模拟登录
		Subject subject = SecurityUtils.getSubject();
		CustomerToken token = new CustomerToken(customer.getCusPhone(), pwd, null, customerService, request);
		subject.login(token);
		return new RedirectView("../", true);
	}
	/**
	 * 注册
	 * @param customer   用户
	 * @param validcode  验证码
	 * @param flash		
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	@RequestMapping(value="app-register", method=RequestMethod.POST, produces = "text/html;charset=utf8")
	@ResponseBody
	public String appRegister(Customer customer,String validcode, RedirectAttributes flash, HttpServletRequest request,ModelMap model) throws Exception {
		Map map = new HashMap();
		if(customer.getCusPhone() == null || customer.getCusPhone().length() != 11){
			map.put("alertMsg", "请填写正确的手机号。");
			return GsonUtil.toJSONString(map);
		}
		try {
			Double.parseDouble(customer.getCusPhone());
		} catch (Exception e) {
			map.put("alertMsg", "请填写正确的手机号。");
			return GsonUtil.toJSONString(map);
		}
		if(customerService.queryCustomerByLogName(customer.getCusLogName()) != null){
			map.put("alertMsg", "该用户昵称已被注册,请变更用户昵称后重新注册。");
			return GsonUtil.toJSONString(map);
		}
		Customer sysCustomer = customerService.queryCustomerByPhone(customer.getCusPhone());
		String pwd = customer.getCusPassword();
		if(sysCustomer != null){
			if(sysCustomer.getCusState() != CustomerState.TEMP){
				map.put("alertMsg", "该手机号码已被注册,请更换手机号码后重新注册。");
				map.put("alertType", "failed");
				return GsonUtil.toJSONString(map);
			} else {//临时账号
				customer.setCusRegDatetime(new Date());
				sysCustomer.setCusLogName(customer.getCusLogName());
				sysCustomer.setCusPassword(pwd);
				sysCustomer.setCusPhone(customer.getCusPhone());
				sysCustomer.setCusState(CustomerState.NORMAL);
				sysCustomer.setCusHeadImg("0.png");
				this.commonService.update(sysCustomer);
			}
		} else {
			customer.setCusRegDatetime(new Date());
			customer.setCusPassword(EncryptUtil.md5(pwd));
			customer.setCusLogName(customer.getCusLogName());
			customer.setCusState(CustomerState.NORMAL);
			customer.setCusHeadImg("0.png");
			this.commonService.insert(customer);
		}
		//模拟登录
		Subject subject = SecurityUtils.getSubject();
		CustomerToken token = new CustomerToken(customer.getCusPhone(), pwd, null, customerService, request);
		subject.login(token);
		this.initCustomerService.initCustomerData(token.loginCustomer().getCusId());//2017年4月27日13:52:09
		map.put("customer", token.loginCustomer());
		return GsonUtil.toJSONString(map);
	}

	/**
	 * 检查手机号是否已经被注册过 
	 */
	@RequestMapping("checkPhone")
	@ResponseBody
	public String checkPhone(String cusPhone) {
		return Boolean.toString(customerService.queryCustomerByPhone(cusPhone) == null);
	}
	/**
	 * 检查用户名是否存在 
	 */
	@RequestMapping("checkLogName")
	@ResponseBody
	public String checkLogName(String cusLogName) {
		return Boolean.toString(customerService.queryCustomerByLogName(cusLogName) == null);
	}

	/**
	 * 检查验证码是否正确 
	 * @param cusPhone
	 * @param validcode
	 * @return Boolean
	 */
	@ResponseBody 
	@RequestMapping("checkValidcode")
	public String checkValidcode(String cusPhone, String validcode) {
		return Boolean.toString(customerService.checkValidcodeByPhone(cusPhone, validcode));
	}

	/**
	 * 检查电话号码是否存在
	 * @param cusPhone
	 * @param validcode
	 * @return Boolean
	 */
	@ResponseBody 
	@RequestMapping("checkCusPhone")
	public String checkCusPhone(String cusPhone) {
		return Boolean.toString(customerService.checkByPhone(cusPhone)!=null);
	}
	/**
	 * 发送验证码 --注册
	 * @param cusPhone
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value="sendValidcode",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String sendValidcode(String cusPhone,String num) throws ParseException {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
    	session.setAttribute("verify", "");
		subject.getSession().setAttribute(cusPhone, DateTimeUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		Validcode validcode = Validcode.generate(4, 5 * 60 * 1000,cusPhone);
		subject.getSession().setAttribute("validcode", validcode);
		SmsTool.sendSms(cusPhone,SmsTool.REGIST_TEMPLATE, "code", validcode.validcode,"product","跟踪宝");
		returnResult.setMsg("获取验证码成功");
		return resultVal();
	}
	
	

	/**
	 * 修改手机号码
	 * @param cusPhone
	 * @param validcode
	 * @return Boolean
	 */
	@ResponseBody 
	@RequestMapping("update-phone")
	public String updatePhone(String cusPhone) {
		Customer customer=CustomerToken.loginCustomer();
		String code = "";
		if(customer != null){
			int a = customerService.updatePhone(customer.getCusId(),cusPhone);
			if(a > 0){
				code = "1";
				SecurityUtils.getSubject().logout();
			}else{
				code = "0";
			}
		}
		return code;
	}
	
	/**
	 * 
	 * 检查图形验证码是否正确
	 * @param valicode 页面传过来的值
	 * @param request 
	 * @return Boolean的字符串值
	 */
	@ResponseBody
	@RequestMapping(value="check-verify",produces = "text/html;charset=UTF-8")
	public String checkverify(String valicode,HttpServletRequest request) {
		HttpSession session=request.getSession();
		if(valicode.equalsIgnoreCase((String) session.getAttribute("verify").toString().replace(" ", ""))){
			return Boolean.toString(true);
		}
		else{
			return Boolean.toString(false);
		}
	}
	
///*==============================================================================================*/
	/**
	 * 忘记密码页面
	 * @param request
	 * @param path
	 * @param model
	 * @return ModelAndView
	 */
	@RequestMapping(value="forget-password", method=RequestMethod.GET)
	public ModelAndView forgetPassword(HttpServletRequest request, String path, ModelMap model) {
		Validcode validcodeRe=(Validcode) session.getAttribute(request.getLocalAddr());
		if(validcodeRe!=null){
			model.addAttribute("validPhone",validcodeRe.validPhone);
			model.addAttribute("validTime",validcodeRe.expiredTime-(System.currentTimeMillis()+4*60*1000));
		}else{
			model.addAttribute("validTime",-1);
		}
		return new ModelAndView("/auth/forgetPassword");
	}
	
	/**
	 * 修改密码页面
	 * @param request
	 * @param path
	 * @param model
	 * @return ModelAndView
	 */
	@RequestMapping(value="editPassword", method=RequestMethod.POST)
	public ModelAndView editPassword(HttpServletRequest request, String cusPhone, String validcode) {
		
		return new ModelAndView("/auth/editPassword");
	}
	/**
	 * 修改密码
	 * @return
	 */
	@RequestMapping(value="editPassword-service", method=RequestMethod.POST,produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String editPassord(String firstPwd,String secondPwd){
		Customer cus=(Customer) session.getAttribute("editPassWordCus");
		if(cus!=null && StringUtils.isNotBlank(firstPwd)){
			String newPwdMd5 = EncryptUtil.md5(firstPwd);
			int result = customerService.updatePwd(cus.getCusId(),newPwdMd5);
			if(result>0){
				returnResult.setMsg("修改成功");
			}else{
				returnResult.setError(true);
				returnResult.setMsg("修改失败");
			}
			return resultVal();
		}
		returnResult.setError(true);
		returnResult.setMsg("修改失败！请通过身份验证后重置密码");
		return resultVal();
	}
	/**
	 * 更新别名
	 * @param alias
	 * @param cusId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="update-alias", method=RequestMethod.POST,produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateAlias(int cusId){
		String alias=UUIDTool.getUUID();
		if(this.customerService.updateAlias(cusId, alias)<1){
			returnResult.setError(true);
			returnResult.setMsg("保存失败");
			return resultVal();
		}
		returnResult.setData(alias);
		returnResult.setMsg("保存成功");
		return resultVal();
	}
	
	/**
	 * 检查验证码是否正确---修改密码
	 * @param cusPhone
	 * @param validcode
	 * @return Boolean
	 */
	@ResponseBody 
	@RequestMapping(value="checkValidcodeEditPwd",produces = "text/html;charset=UTF-8")
	public String checkValidcodeEditPwd(String cusPhone, String validcode) {
		String code=(String) session.getAttribute("validcode");
		if(validcode!=null && code!=null && validcode.equals(code)){
			returnResult.setError(true);
		}
		return resultVal();
	}
	/**
	 * 发送验证码 --忘记密码
	 * @param cusPhone
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value="sendValidcode-forget",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String sendValidcodeForget(String cusPhone,String num) throws ParseException {
		Customer cus=this.customerService.queryCustomerByPhone(cusPhone);
		if(cus==null){
			returnResult.setError(true);
			returnResult.setMsg("该账户不存在");
			return resultVal();
		}
		session.setAttribute("editPassWordCus", cus);
		session.setAttribute(cusPhone, DateTimeUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		Validcode validcode = Validcode.generate(4, 5 * 60 * 1000,cusPhone);//五分钟有效时间
		session.setAttribute("validcode", validcode);
		session.setAttribute(request.getLocalAddr(), validcode);
		System.err.println(validcode.validcode);
		SmsTool.sendSms(cusPhone,SmsTool.VALIDCODE_CHANGE_PWD, "code", validcode.validcode,"product","跟踪宝");
		returnResult.setMsg("获取验证码成功");
		return resultVal();
	}
	/**
	 * 检查验证码是否正确 DX
	 * @param cusPhone
	 * @param validcode
	 * @return Boolean
	 */
	@ResponseBody 
	@RequestMapping(value="checkValidcodeDX",produces = "text/html;charset=UTF-8")
	public String checkValidcodeDX(String cusPhone, String validcode) {
		return Boolean.toString(customerService.checkValidcodeByPhoneDX(cusPhone, validcode));
	}
	
	/**
	 * 退出登录
	 * @return
	 */
	@RequestMapping("logout")
	public RedirectView logout() {
		SecurityUtils.getSubject().logout();
		return new RedirectView("login");
	}
	
}

