package ydh.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import ydh.cicada.service.CommonService;
import ydh.cicada.utils.RequestUtil;
import ydh.layout.MainLayout;
import ydh.user.entity.User;
import ydh.user.form.UserLoginForm;
import ydh.user.realm.UserLoginZone;
import ydh.user.realm.UserToken;
import ydh.user.service.AfterUserLogin;
import ydh.user.service.UserService;
import ydh.utils.ConfigTool;
import ydh.utils.EnvConfig;
import ydh.utils.GsonUtil;
import ydh.utils.ServiceProxy;
import ydh.utils.Validcode;
import ydh.website.localization.service.WebSiteExceptionService;

@Controller
@RequestMapping("admin")
public class UserController {

	@Autowired
	private MainLayout layout;

	@Autowired
	private UserLoginZone userLoginZone;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WebSiteExceptionService exceptionService;
	
	private AfterUserLogin afterUserLogin = ServiceProxy.proxy(AfterUserLogin.class);
	
	@RequestMapping(value="login", method=RequestMethod.GET)
	public ModelAndView login() {
		return new ModelAndView("admin/auth/login");
	}
	/***
	 * 登录
	 * @param loginForm
	 * @param flash
	 * @return
	 */
	@RequestMapping(value="login", method=RequestMethod.POST)
	public RedirectView login(UserLoginForm loginForm, RedirectAttributes flash) {
		try {
			Subject subject = SecurityUtils.getSubject();
			UserToken token = new UserToken(loginForm.getUsername(), loginForm.getPassword(), loginForm.getValidcode(), userLoginZone);
			subject.login(token);
			afterUserLogin.afterLogin();
			return new RedirectView("/admin/main", true);
		} catch (AuthenticationException e) {
			exceptionService.createWebSiteException("登录异常", e);
			flash.addFlashAttribute("alertType", "error");
			flash.addFlashAttribute("alertMsg", e.getMessage());
			flash.addFlashAttribute("loginForm", loginForm);
			return new RedirectView("login");
		}
	}
	
	/**
	 * 后台登录发送手机验证码 
	 * @param cusPhone
	 * @return
	 */
	@RequestMapping(value="sendValidcode", method=RequestMethod.POST,produces = "text/html; charset=UTF-8")
	public @ResponseBody String sendValidcode(String username) {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			User user = userLoginZone.getUserService().queryUserByLoginName(username);
			if (StringUtils.isEmpty(user)) {
				throw new Exception(username+"这个用户不存在");
			}else if(StringUtils.isEmpty(user.getMobile())){
				throw new Exception(username+"的手机号码为空");
			}
			Validcode validcode = Validcode.generate(6, 5 * 60 * 1000,user.getMobile());
			Subject subject = SecurityUtils.getSubject();
			subject.getSession().setAttribute("adminValidcode", validcode);
			//暂时不用发短信接口
			if ( ! ConfigTool.getBoolean(EnvConfig.test, false)) {
				map.put("data", "");
			} else {
				map.put("data", "000000");
			}
			//将对象转换成json字符串，true为格式化输出   测试用的
			map.put("state",true);
			return GsonUtil.toJSONString(map);
		} catch (Exception ex) {
			exceptionService.createWebSiteException("验证码发送异常", ex);
			//处理异常
			map.put("state",false);
			map.put("data", ex.getMessage());
			return GsonUtil.toJSONString(map);
		}
	}
	
	/**
	 * 发送验证码
	 * @param mobile
	 * @return
	 */
	@RequestMapping("sendPhonecode")
	public @ResponseBody String sendPhonecode(String mobile) {
		Validcode validcode = Validcode.generate(6, 5 * 60 * 1000,mobile);
		Subject subject = SecurityUtils.getSubject();
		subject.getSession().setAttribute("adminValidcode", validcode);
		return "";
	}
	/**
	 * 个人信息页面跳转
	 * @param model ModelMap
	 * @return ModelAndView
	 */
	@RequestMapping("user-info")
	public ModelAndView userInfo(ModelMap model) {
		model.put("user", this.putUser());
		return layout.layout("admin/auth/user-info");
	}

	/**
	 * 信息修改页面跳转
	 * @return ModelAndView
	 */
	@RequestMapping("user-change")
	public ModelAndView userChange() {
		return layout.layout("admin/auth/user-change");
	}

	/**
	 * 修改手机号
	 * @param model
	 * @param user
	 * @param attributes
	 * @return
	 */
	@RequestMapping("update-phone")
	public RedirectView updatePhone(ModelMap model,User user, RedirectAttributes attributes){
		User newUser=commonService.load(User.class, UserLoginZone.loginUser().getUserId());
		try {
			newUser.setMobile(user.getMobile());
			commonService.update(newUser);
			attributes.addFlashAttribute("alertType", "success");
			attributes.addFlashAttribute("alertMsg","手机号修改成功,需在此重新登录!");
			logout();
			return new RedirectView("/admin/login",true);
		} catch (Exception e) {
			exceptionService.createWebSiteException("修改手机号异常", e);
			attributes.addFlashAttribute("alertType", "danger");
			attributes.addFlashAttribute("alertMsg", "手机号修改失败！出现未知异常，请重新尝试或联系管理员");
			return new RedirectView("user-info");
		}
		
	}
	
	/**
	 * 查找当前登录用户 
	 * @return User
	 */
	public User putUser() {
		Subject subject = SecurityUtils.getSubject();
		return (User) subject.getSession().getAttribute("loginUser");
	}  

	/**
	 * 验证原始密码是否正确
	 * @param oldPassword String
	 * @param currentUser User
	 * @return boolean 
	 */
	public boolean passwordCheck(String oldPassword, User currentUser) {
		if(null != oldPassword && null != currentUser){
			if(currentUser.getPassword().equals(RequestUtil.MD5(oldPassword))){
				return true;
			}
		}
		return false;
	}
	/**
	 * 保存密码修改
	 * @param oldPassword String
	 * @param newPassword String
	 * @param reNewPassword String
	 * @param flash RedirectAttributes
	 * @return RedirectView
	 */
	@RequestMapping("password-change-save")
	public RedirectView passwordChange(String oldPassword, String newPassword, String reNewPassword, RedirectAttributes flash) {
		//新密码两次输入一致性验证
		if(!newPassword.equals(reNewPassword)){
			flash.addFlashAttribute("alertType", "error");
			flash.addFlashAttribute("alertMsg", "新密码两次输入不一致");
		} else {
			//获取当前登录用户信息
			User currentUser = this.putUser();
			//原密码正确性验证
			if(!this.passwordCheck(oldPassword,currentUser)){
				flash.addFlashAttribute("alertType", "error");
				flash.addFlashAttribute("alertMsg", "原密码输入错误");
			} else {
				//如果全部正确，则执行修改保存
				UserService userService = userLoginZone.getUserService();
				User user = userService.load(User.class, currentUser.getUserId());
				userService.updateUserPassword(user.getUserId(), RequestUtil.MD5(newPassword));
				flash.addFlashAttribute("alertType", "info");
				flash.addFlashAttribute("alertMsg", "修改成功");
				return new RedirectView("logout");
			}
		}
		return new RedirectView("user-change");
	}

	@RequestMapping("logout")
	public RedirectView logout() {
		SecurityUtils.getSubject().logout();
		return new RedirectView("login");
	}

	/**
	 * 检查手机号是否已经被注册过 
	 */
	@RequestMapping("checkMobile")
	@ResponseBody
	public String checkMobile(String mobile) {
		return Boolean.toString(userService.selectUserByMobile(mobile) == null);
	}
	
	/**
	 * 检查验证码是否正确 
	 * @param cusPhone
	 * @param validcode
	 * @return Boolean
	 */
	@ResponseBody 
	@RequestMapping("checkValidcode")
	public String checkValidcode(String mobile, String validcode) {
		return Boolean.toString(userService.checkValidcodeByPhone(mobile, validcode));
	}
	
	@RequestMapping("company-charge")
	public ModelAndView companyCharge() {
		return new ModelAndView("admin/auth/company-charge");
	}
}
