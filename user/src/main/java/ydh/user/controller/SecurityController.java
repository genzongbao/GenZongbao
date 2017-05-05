package ydh.user.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import ydh.base.perm.PermManager;
import ydh.cicada.dao.Page;
import ydh.cicada.query.QueryObject;
import ydh.cicada.service.CommonService;
import ydh.cicada.utils.RequestUtil;
import ydh.layout.MainLayout;
import ydh.user.dict.UserState;
import ydh.user.entity.Role;
import ydh.user.entity.RolePermission;
import ydh.user.entity.User;
import ydh.user.query.RoleQuery;
import ydh.user.query.UserQuery;
import ydh.user.service.AfterUserFreezed;
import ydh.user.service.SecurityService;
import ydh.user.service.UserService;
import ydh.user.utils.UserConfig;
import ydh.utils.ConfigTool;
import ydh.utils.ServiceProxy;
import ydh.website.localization.service.WebSiteExceptionService;

/**
 * 权限管理
 * @author chenkailun
 *
 */

@Controller
@RequestMapping("admin/security")
public class SecurityController {
	
	@Autowired
	private MainLayout layout;
	@Autowired
	private CommonService commonService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private UserService userService;
	@Autowired
	private PermManager permManager;
	@Autowired
	private WebSiteExceptionService exceptionService;
	private AfterUserFreezed afterUserFreezed = ServiceProxy.proxy(AfterUserFreezed.class);
	/**
	 * user列表
	 * @param query
	 * @param model
	 * @return
	 */
	@RequestMapping("user-list")
	public ModelAndView userList(UserQuery query, ModelMap model) {
		Page<User> page = commonService.find(User.class, query);
		List<Role> roleList = commonService.list(Role.class, new RoleQuery());
		model.put("page", page);
		model.put("query", query);
		model.put("roleList", roleList);
		return layout.layout("admin/security/user-list");
	}
	
	/**
	 * 修改user状态
	 * @param userId
	 * @param state
	 * @param flash
	 * @return
	 */
	@RequestMapping("edit-state")
	public RedirectView editState(Integer userId, UserState state, RedirectAttributes flash) {
		try {
			if(userId==ConfigTool.getInteger(UserConfig.admin_id)){
				flash.addFlashAttribute("alertType", "warning");
				flash.addFlashAttribute("alertMsg", "该账户不可更改状态");
				return new RedirectView("user-list");
			}
			User user = commonService.load(User.class, userId);
			user.setUserState(state);
			commonService.update(user);
			if(UserState.FREEZED == state){
				afterUserFreezed.afterUserFreezed(user);
			}
			flash.addFlashAttribute("alertType", "info");
			flash.addFlashAttribute("alertMsg", "操作成功");
		} catch (Exception e) {
			exceptionService.createWebSiteException("修改用户状态异常", e);
			e.printStackTrace();
			flash.addFlashAttribute("alertType", "error");
			flash.addFlashAttribute("alertMsg", "出现未知异常，请重新尝试或联系管理员");
		}
		return new RedirectView("user-list");
	}
	
	/**
	 * 修改用户角色
	 * @param userId
	 * @param roleIds
	 * @param flash
	 * @return
	 */
	@RequestMapping("edit-users-role")
	public RedirectView editUsersRole(Integer userId, String roleIds, RedirectAttributes flash) {
		try {
			if(userId==ConfigTool.getInteger(UserConfig.admin_id)){
				flash.addFlashAttribute("alertType", "warning");
				flash.addFlashAttribute("alertMsg", "该角色不可更改及分配");
				return new RedirectView("user-list");
			}
			securityService.editUsersRole(userId, roleIds);
			flash.addFlashAttribute("alertType", "info");
			flash.addFlashAttribute("alertMsg", "操作成功");
		} catch (Exception e) {
			exceptionService.createWebSiteException("修改用户角色异常", e);
			e.printStackTrace();
			flash.addFlashAttribute("alertType", "error");
			flash.addFlashAttribute("alertMsg", "出现未知异常，请重新尝试或联系管理员");
		}
		return new RedirectView("user-list");
	}
	
	/**
	 * 角色列表
	 * @param query
	 * @param model
	 * @return
	 */
	@RequestMapping("role-list")
	public ModelAndView roleList(RoleQuery query, ModelMap model) {
		Page<Role> page = commonService.find(Role.class, query);
		model.put("page", page);
		model.put("query", query);
		model.put("adminId",UserConfig.admin_id );
		return layout.layout("admin/security/role-list");
	}
	
	/**
	 * 添加角色页面
	 * @return
	 */
	@RequestMapping("role-add-page")
	public ModelAndView roleAddPage() {
		return layout.layout("admin/security/role-add");
	}
	
	
	
	
	/**
	 * 角色添加
	 * @param role
	 * @param flash
	 * @return
	 */
	@RequestMapping("role-add")
	public RedirectView roleAdd(Role role, RedirectAttributes flash) {
		try {
			if(role.getRoleId()==null){
				role.setCreateDate(new Date());
				commonService.insert(role);
				flash.addFlashAttribute("alertType", "info");
				flash.addFlashAttribute("alertMsg", "添加成功");
			}else{
				Role newRole=commonService.load(Role.class, role.getRoleId());
				newRole.setRoleDesc(role.getRoleDesc());
				newRole.setRoleName(role.getRoleName());
				flash.addFlashAttribute("alertType", "info");
				flash.addFlashAttribute("alertMsg", "修改成功");
				commonService.update(newRole);
			}
			return new RedirectView("role-list");
		} catch (Exception e) {
			exceptionService.createWebSiteException("新增角色异常", e);
			e.printStackTrace();
			flash.addFlashAttribute("alertType", "error");
			flash.addFlashAttribute("alertMsg", "出现未知异常，请重新尝试或联系管理员");
			flash.addFlashAttribute("role", role);
			return new RedirectView("role-add-page");
		}
	}
	/**
	 * 进入修改页面
	 * @param roleId
	 * @param model
	 * @return
	 */
	@RequestMapping("role-update")
	public ModelAndView roleUpdate(Integer roleId,ModelMap model){
		Role role=commonService.load(Role.class, roleId);
		model.put("role", role);
		return layout.layout("admin/security/role-add");
	}
	/**
	 * 检测角色名是否存在
	 * @param roleId
	 * @param roleName
	 * @return
	 */
	@RequestMapping("check-roleName")
	@ResponseBody
	public String checkRoleNmae(Integer roleId,String roleName){
		if(roleId==null){
			return Boolean.toString(securityService.selectRoleByroleName(roleName)==null);
		}else{
			Role role=securityService.selectRoleByIdAndroleName(roleId, roleName);
			if(role!=null){
				return "true";
			}else{
				return Boolean.toString(securityService.selectRoleByroleName(roleName)==null);
			}
		}
		
	}
	
	/**
	 * 角色删除
	 * @param roleId
	 * @param flash
	 * @return
	 */
	@RequestMapping("role-delete")
	public RedirectView roleDelete(Integer roleId, RedirectAttributes flash) {
		try {
			if(roleId.toString().equals(ConfigTool.getString(UserConfig.role_id) )){
				flash.addFlashAttribute("alertType", "warning");
				flash.addFlashAttribute("alertMsg", "系统管理员不能删除");
				return new RedirectView("role-list");
			}
			securityService.removeRole(roleId);
			flash.addFlashAttribute("alertType", "info");
			flash.addFlashAttribute("alertMsg", "操作成功");
		} catch (Exception e) {
			exceptionService.createWebSiteException("删除角色异常", e);
			e.printStackTrace();
			flash.addFlashAttribute("alertType", "error");
			flash.addFlashAttribute("alertMsg", "出现未知异常，请重新尝试或联系管理员");
		}
		return new RedirectView("role-list");
	}
	
	/**
	 * 编辑角色权限页面
	 * @param roleId
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("role-edit-permission-page")
	public ModelAndView roleEditPermissionPage(Integer roleId, ModelMap model) throws Exception {
		Role role = commonService.load(Role.class, roleId);
		List<RolePermission> permissions = QueryObject.select(RolePermission.class)
														.cond(RolePermission.ROLE_ID).equ(roleId)
														.list(commonService);
		model.put("role", role);
		model.put("pm", permManager);
		model.put("permissions", permissions);
		return layout.layout("admin/security/role-edit-permission");
	}
	
	/**
	 * 重新保存角色权限
	 * @param roleId
	 * @param permissions
	 * @param flash
	 * @return
	 */
	@RequestMapping("role-permission-edit")
	public RedirectView rolePermissionEdit(Integer roleId,String permissions, RedirectAttributes flash) {
		try {
			if(roleId != ConfigTool.getInteger(UserConfig.role_id)){//如果修改权限的角色id为系统默认的角色 ，则不能修改
				if(permissions != null)
					this.securityService.updateRolePermissions(roleId, permissions.split(","));
				else
					this.securityService.updateRolePermissions(roleId, new String[]{});
				flash.addAttribute("roleId", roleId);
				flash.addFlashAttribute("alertType", "info");
				flash.addFlashAttribute("alertMsg", "操作成功");
			}else{
				flash.addAttribute("roleId", roleId);
				flash.addFlashAttribute("alertType", "warnings");
				flash.addFlashAttribute("alertMsg", "系统默认管理员不能修改权限");
				return new RedirectView("role-edit-permission-page");
			}
		} catch (Exception e) {
			exceptionService.createWebSiteException("修改权限异常", e);
			e.printStackTrace();
			flash.addAttribute("roleId", roleId);
			flash.addFlashAttribute("alertType", "error");
			flash.addFlashAttribute("alertMsg", "出现未知异常，请重新尝试或联系管理员");
		}
		return new RedirectView("role-edit-permission-page");
	}
	
	/***
	 * 跳转添加管理员页面
	 * @param user
	 * @param model
	 * @param msg
	 * @return
	 */
	@RequestMapping("show-user-add")
	public ModelAndView showUserAdd() {
		return layout.layout("security/user-add");
	}
	
	/**
	 * 添加管理员
	 * @param user
	 * @param model
	 * @param flash
	 * @return
	 */
	@RequestMapping("user-add")
	public RedirectView userAdd(User user,ModelMap model, RedirectAttributes flash) {
		user.setRegisterTime(new Date());
		user.setPassword(RequestUtil.MD5(user.getPassword()));
		user.setUserState(UserState.VALID);
		try {
			commonService.insert(user);
			flash.addFlashAttribute("alertType", "success");
			flash.addFlashAttribute("alertMsg", "操作成功");
		} catch (Exception e) {
			exceptionService.createWebSiteException("添加用户异常", e);
			e.printStackTrace();
			flash.addFlashAttribute("alertType", "error");
			flash.addFlashAttribute("alertMsg", "出现未知异常，请重新尝试或联系管理员");
		}
		return new RedirectView("user-list");
	}
	
	/***
	 * 验证登录用户名是否存在
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value= "check-log-name" ,produces = "text/html;charset=UTF-8")
	public String chackLogName(User user,ModelMap model) {
		return Boolean.toString(userService.queryUserByLoginName(user.getLoginName()) == null);
	}
	/***
	 * 验证登录手机号是否存在
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value= "check-mobile" ,produces = "text/html;charset=UTF-8")
	public String chackMobile(User user,ModelMap model) {
		return Boolean.toString(userService.selectUserByMobile(user.getMobile()) == null);
	}
	/**
	 * 验证身份证号是否存在
	 * @param user
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value= "check-idCard" ,produces = "text/html;charset=UTF-8")
	public String chackIdCard(User user,ModelMap model) {
		return Boolean.toString(userService.selectUserByIdCardNo(user.getIdCardNo()) == null);
	}
}
