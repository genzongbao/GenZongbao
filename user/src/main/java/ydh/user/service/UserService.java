package ydh.user.service;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ydh.cicada.dao.JdbcDao;
import ydh.cicada.query.QueryObject;
import ydh.cicada.query.Updater;
import ydh.cicada.service.CommonService;
import ydh.user.entity.Menu;
import ydh.user.entity.User;
import ydh.utils.Validcode;

@Service
public class UserService  extends CommonService {
	
	@Autowired
	private JdbcDao dao;
	
	/**
	 * 获取user的角色集合
	 * @param userId
	 * @return
	 */
	public List<String> queryActiveRoles(int userId) {
		return QueryObject.select("R.ROLE_NAME", String.class)
				.from("USER_ROLE UR LEFT JOIN ROLES R ON UR.ROLE_ID=R.ROLE_ID")
				.condition("UR.USER_ID=?", userId)
				.list(dao);
	}
	
	/**
	 * 获取user的权限集合
	 * @param userId
	 * @return
	 */
	public List<String> queryActivePermissions(int userId) {
		return QueryObject.select("RP.PERMISSION_CODES", String.class)
				.from("USER_ROLE UR LEFT JOIN ROLES R ON UR.ROLE_ID = R.ROLE_ID LEFT JOIN ROLE_PERMISSION RP ON RP.ROLE_ID = UR.ROLE_ID")
				.condition("UR.USER_ID=?", userId)
				.list(dao);
	}

	/**
	 * 通过ID查询User
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public User queryUserById(String loginId) {
		return QueryObject.select(User.class)
				.condition("USER_ID=?", loginId)
				.firstResult(dao);
	}

	/**
	 * 通过登录名查询User
	 * @param userLogName
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public User queryUserByLoginName(String loginName) {
		return QueryObject.select(User.class)
				.condition("LOGIN_NAME=?", loginName)
				.firstResult(dao);
	}

	/**
	 * 查询菜单
	 * @return
	 */
	@Transactional
	public List<Menu> listMenu() {
		return QueryObject.select(Menu.class)
				.asc("SORT")
				.list(dao);
	}

	/**
	 * 修改手机号码
	 * @param mobile
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public int updateUserMobile(String mobile,Integer userId){
		return Updater.update(User.class)
				.set("MOBILE=?", mobile)
				.condition("USER_ID=?", userId)
				.exec(dao);
	}
	
	/**
	 * 修改用户密码
	 * @param userId
	 * @param password  MD5加密后的密码
	 */
	@Transactional
	public void updateUserPassword(Integer userId,String password) {
		Updater.update(User.class)
		.set("PASSWORD=?", password)
		.condition("USER_ID=?", userId)
		.exec(dao);
	}
	
	/**
	 * 改变登录时间
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public void updateLastLogTime(Integer userId) {
		Updater.update(User.class)
		.set("LAST_LOGIN_TIME=?", new Date())
		.condition("USER_ID=?", userId)
		.exec(dao);
	}

	/**
	 * 判断手机验证码
	 * @param validcode
	 * @param phone
	 * @return boolean
	 */
	public boolean checkValidcodeByPhone(String phone, String validcode) {
		if (validcode == null || phone == null) return false;
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession(false);
		if (session == null) return false;
		Validcode savedValidcode = (Validcode)session.getAttribute("adminValidcode");
		if (savedValidcode == null) return false;
		if (savedValidcode.expiredTime < System.currentTimeMillis()) {
			session.removeAttribute("adminValidcode");
			return false;
		}
		if(!phone.equals(savedValidcode.validPhone)) return false;
		return validcode.equals(savedValidcode.validcode);
	}
	/**
	 * 通过身份证查询user 
	 * @param idCardNo
	 * @return
	 */
	@Transactional
	public User selectUserByIdCardNo(String idCardNo){
		return QueryObject.select(User.class)
				.condition("ID_CARD_NO=?", idCardNo)
				.firstResult(dao);
		
	}
	
	/**
	 * 通过电话查询user
	 * @param mobile
	 * @return
	 */
	@Transactional
	public User selectUserByMobile(String mobile){
		return QueryObject.select(User.class)
				.condition("MOBILE=?", mobile)
				.firstResult(dao);
		
	}
}
