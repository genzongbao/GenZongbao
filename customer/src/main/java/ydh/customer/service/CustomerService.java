package ydh.customer.service;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ydh.cicada.dao.JdbcDao;
import ydh.cicada.query.QueryObject;
import ydh.cicada.query.Updater;
import ydh.customer.dict.CustomerState;
import ydh.customer.entity.Account;
import ydh.customer.entity.Customer;
import ydh.customer.entity.CustomerFriendMapping;
import ydh.customer.query.CustomerQuery;
import ydh.mvc.ex.WorkException;
import ydh.utils.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lizx
 *
 */
@Service
public class CustomerService{
	@Autowired
	private JdbcDao jdbcDao;
	
	/**
	 * 判断是否有该手机号
	 * @param cusPhone
	 * @return
	 */
	@Transactional
	public boolean hasPhone(String cusPhone) {
		CustomerQuery query = new CustomerQuery();
		query.setCusPhone(cusPhone);
		if(jdbcDao.count(query) > 0) 
			return true;
		else
			return false;
	}
	/**
	 * 查询所有用户
	 * @return
	 */
	@Transactional
	public List<Customer> loadAll() {
		return QueryObject.select(Customer.class).list(jdbcDao);
	}
	
	/**
	 * 根据手机号查询用户
	 * @param id
	 * @return
	 */
	@Transactional
	public Customer loadReferrer(String cusPhone) {
		return QueryObject.select(Customer.class).condition("CUS_PHONE=?", cusPhone)
				.firstResult(jdbcDao);
	}

	/**
	 * 登录检查
	 * 
	 * @param cusLogName
	 * @param cusPassWd
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public Customer loginCheck(String cusPhone, String cusPassWd) {
		return QueryObject.select(Customer.class)
				.condition("CUS_PHONE=? or CUS_LOG_NAME=?", cusPhone, cusPhone)
				.condition("CUS_PASSWORD=?", cusPassWd)
				.firstResult(jdbcDao);
	}
	
	@Transactional
	public Customer loginCheckByNameOrPhone(String param) {
		return QueryObject.select(Customer.class)
				.condition("CUS_PHONE=? or CUS_LOG_NAME=?", param, param)
				.firstResult(jdbcDao);
	}
	
	/**
	 * 改变登录时间
	 * 
	 * @param cusId
	 * @throws ServiceException
	 */
	@Transactional
	public void updateLastLogTime(Integer cusId) {
		Updater.update(Customer.class)
		.set("CUS_LAST_LOG_DATETIME=?", new Date())
		.condition("CUS_ID=?", cusId)
		.exec(jdbcDao);
	}

	/**
	 * 修改电话号码
	 * 
	 * @param cusId
	 * @param cusPhone
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public int updatePhone(Integer cusId, String cusPhone) {
		return Updater.update(Customer.class)
				.set("CUS_PHONE=?", cusPhone)
				.condition("CUS_ID=?", cusId)
				.exec(jdbcDao);
	}
	/**
	 * 更新推送别名
	 * @param cusId
	 * @param alias
	 * @return
	 */
	@Transactional
	public int updateAlias(Integer cusId, String alias) {
		return Updater.update(Customer.class)
				.set("PUSH_ALIAS=?", alias)
				.condition("CUS_ID=?", cusId)
				.exec(jdbcDao);
	}
	
	/**
	 * 通过cusId查询customer
	 * 
	 * @param cusId
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public Customer loadCustomer(Integer cusId) {
		return jdbcDao.load(Customer.class, cusId);
	}

	/**
	 * 修改邮箱
	 * 
	 * @param cusId
	 * @param email
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public int updateEmail(Integer cusId, String email) {
		return Updater.update(Customer.class)
				.set("CUS_EMAIL=?", email)
				.condition("CUS_ID=?", cusId)
				.exec(jdbcDao);
	}
	
	/**
	 * 根据电话号码查询好友
	 * @param cusId
	 * @param phone
	 * @return
	 */
	@Transactional
	public Customer checkFriend(Integer cusId, String phone) {
		return QueryObject.select("C.*",Customer.class)
				.from("CUSTOMER_FRIEND_MAPPING M LEFT JOIN CUSTOMER C ON M.FRIEND_ID=C.CUS_ID")
				.cond("M.CUS_ID").equ(cusId)
				.cond("C.CUS_PHONE").equ(phone).firstResult(jdbcDao);
	}
	
	/**
	 * 根据电话号码查询用户
	 * @param cusId
	 * @param phone
	 * @return
	 */
	@Transactional
	public Customer checkIsReg(String phone) {
		return QueryObject.select(Customer.class)
				.cond("CUS_PHONE").equ(phone).firstResult(jdbcDao);
	}
	/**
	 * 标记用户列表是否为朋友
	 * @param list
	 * @param cusId
	 * @return
	 */
	public List<Customer> checkListIsFriend(List<Customer> list,int cusId){
		try {			
			if(CheckTool.checkListIsNotNull(list)){
				for (Customer customer : list) {
					
					Customer cus=checkIsReg(customer.getCusPhone());
					if(cus!=null){
						customer.setCusId(cus.getCusId());
						customer.setCusRegDatetime(cus.getCusRegDatetime());
						if(checkFriend(cusId,customer.getCusPhone())!=null){
							customer.setFriend(true);
						}
					}
				}
			}
		} catch (Exception e) {
			LogHelper.getLogger(this.getClass()).error(e.getMessage());
		}
		return list;
	}
	/**
	 * 通过cusId修改密码
	 * 
	 * @param cusId
	 * @param newPwd
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public int updatePwd(Integer cusId, String newPwdMd5) {
		return Updater.update(Customer.class)
				.set("CUS_PASSWORD=?", newPwdMd5)
				.condition("CUS_ID=?", cusId)
				.exec(jdbcDao);
	}

	/**
	 * 通过登录名查询Customer
	 * 
	 * @param cusLogName
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public Customer queryCustomerByLogName(String cusLogName) {
		return QueryObject.select(Customer.class)
				.condition("CUS_LOG_NAME=?", cusLogName)
				.firstResult(jdbcDao);
	}

	/**
	 * 通过电话号码查询非临时Customer
	 * @param amountOfPurchase
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public Customer queryCustomerByPhone(String cusPhone) {
		return QueryObject.select(Customer.class)
				.condition("CUS_PHONE=?", cusPhone)
				.firstResult(jdbcDao);
	}
	
	/**
	 * 通过身份证号查询Customer
	 * 
	 * @param amountOfPurchase
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public Customer queryCustomerByCusIdcard(String cusIdcard) {
		return QueryObject.select(Customer.class)
				.condition("CUS_IDCARD=?", cusIdcard)
				.firstResult(jdbcDao);
	}
	
	/**
	 * 通过邮箱查询Customer
	 * 
	 * @param amountOfPurchase
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public Customer queryCustomerByEmail(String email) {
		return QueryObject.select(Customer.class)
				.condition("CUS_EMAIL=?", email)
				.firstResult(jdbcDao);
	}
	
	/**
	 * 修改用户登录状态
	 * 
	 * @param cusId
	 * @param enabled
	 */
	@Transactional
	public int updateState(Integer cusId, CustomerState state) {
		return Updater.update(Customer.class)
				.set("CUS_STATE=?", state)
				.condition("CUS_ID=?", cusId)
				.exec(jdbcDao);
	}
	/**
	 * 根据cusId查询Account对象(包含CUSTOMER表中的字段)
	 * @param cusId  客户ID
	 * @return
	 */
	@Transactional
	public Account queryAccountByCusId(Integer cusId) {
		return QueryObject.select(Account.class)
				.from("ACCOUNT a join CUSTOMER c on a.CUS_ID = c.CUS_ID")
				.condition("a.CUS_ID=?", cusId)
				.uniqueResult(jdbcDao);
	}
	
	/***
	 * 校验电话号码是否存在
	 * @param cusPhone
	 * @return
	 */
	public Customer checkByPhone(String cusPhone){
		return QueryObject.select(Customer.class)
				.condition("CUS_PHONE=?", cusPhone)
				.firstResult(jdbcDao);
	}
	/**
	 * 判断手机验证码
	 * @param validcode
	 * @param phone
	 * @return boolean
	 */
	@Transactional
	public boolean checkValidcodeByPhone(String phone,String validcode) {
		if (validcode == null || phone == null) return false;
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession(false);
		if (session == null) return false;
		Validcode savedValidcode = (Validcode)session.getAttribute("validcode");
		if (savedValidcode == null) return false;
		if (savedValidcode.expiredTime < System.currentTimeMillis()) {
			session.removeAttribute("validcode");
			return false;
		}
		if(!phone.equals(savedValidcode.validPhone)) return false;
		session.removeAttribute("validcode");
		return validcode.equals(savedValidcode.validcode);
	}
	/**
	 * 判断手机验证码
	 * @param validcode
	 * @param phone
	 * @return boolean
	 */
	@Transactional
	public boolean checkValidcodeByPhoneDX(String phone,String validcode) {
		if (ConfigTool.getBoolean(EnvConfig.test, false)) {
			return true;
		}
		if (validcode == null || phone == null) return false;
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession(false);
		if (session == null) return false;
		Validcode savedValidcode = (Validcode)session.getAttribute("validcodeDX");
		if (savedValidcode == null) return false;
		if (savedValidcode.expiredTime < System.currentTimeMillis()) {
			session.removeAttribute("validcodeDX");
			return false;
		}
		if(!phone.equals(savedValidcode.validPhone)) return false;
		return validcode.equals(savedValidcode.validcode);
	}
	
	public Customer queryCustomerByCusId(Integer cusId){
		return QueryObject.select(Customer.class)
				.condition("CUS_ID=?", cusId)
				.firstResult(jdbcDao);
	}
	
	/**
	 * 注册用户按年统计
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	public List<Map> queryCustomerRegYearStats(){
		return QueryObject.select("DATE_FORMAT(CUS_REG_DATETIME,'%Y') as time,count(CUS_ID) as count",Map.class)
				.from("CUSTOMER")
				.groupBy("time")
				.asc("time")
				.list(jdbcDao);
	}
	
	/**
	 * 注册用户按季统计
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	public List<Map> queryCustomerRegJuduStats(String year){
		return QueryObject.select("QUARTER(CUS_REG_DATETIME) as time,count(CUS_ID) as count",Map.class)
				.from("CUSTOMER")
				.condition("DATE_FORMAT(CUS_REG_DATETIME,'%Y')='"+year+"'")
				.groupBy("time")
				.asc("time")
				.list(jdbcDao);
	}
	
	/**
	 * 注册用户按月统计
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	public List<Map> queryCustomerRegMonthStats(String year){
		return QueryObject.select("DATE_FORMAT(CUS_REG_DATETIME,'%m') as time,count(CUS_ID) as count",Map.class)
				.from("CUSTOMER")
				.condition("DATE_FORMAT(CUS_REG_DATETIME,'%Y')='"+year+"'")
				.groupBy("time")
				.asc("time")
				.list(jdbcDao);
	}
	
	/**
	 * 注册用户按日统计
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	public List<Map> queryCustomerRegDayStats(String year,String month){
		return QueryObject.select("DATE_FORMAT(CUS_REG_DATETIME,'%d') as time,count(CUS_ID) as count",Map.class)
				.from("CUSTOMER")
				.condition("DATE_FORMAT(CUS_REG_DATETIME,'%Y-%m')='"+year+"-"+month+"'")
				.groupBy("time")
				.asc("time")
				.list(jdbcDao);
	}
	/**
	 * 添加支付宝账号
	 * @param cusId
	 * @param enabled
	 */
	public int addZhiFuBao(Integer cusId,String zhifubao,String zhiFuBaoName){
		return Updater.update(Customer.class)
				.set("ZHI_FU_BAO=?,ZHI_FU_BAO_NAME=?", zhifubao,zhiFuBaoName)
				.condition("CUS_ID=?", cusId)
				.exec(jdbcDao);
	}
	
	@Transactional
	public Long queryRegDayCountByCusId(Integer cusId){
		Customer customer = this.jdbcDao.load(Customer.class, cusId);
		Date start = customer.getCusRegDatetime();
		Date end = new Date();
		Long count = (end.getTime() - start.getTime())/(24*60*60*1000);
		return count < 1 ? 1:count;
	}
	/**
	 * 获取用户好友列表
	 * @param cusId
	 * @return 好友列表
	 */
	@Transactional
	public List<Customer> selectFirends(int cusId){
		return QueryObject.select("C.*",Customer.class).from("CUSTOMER_FRIEND_MAPPING M LEFT JOIN CUSTOMER C ON M.FRIEND_ID=C.CUS_ID")
				.cond("M.CUS_ID").equ(cusId).list(jdbcDao);
	}
	
	/**
	 * 添加朋友
	 * @param cus
	 * @return
	 * @throws WorkException 
	 */
	@Transactional
	public String addFriends(Customer cus,int cusId) throws WorkException{
		if(cus==null){
			return "邀请失败！";
		}
		try {			
			if(cus.getCusId()!=null && cusId!=0){
				
				CustomerFriendMapping cusM=new CustomerFriendMapping();
				cusM.setCusFriendMappingId(UUIDTool.getUUID());
				cusM.setCusId(cusId);
				cusM.setFriendId(cus.getCusId());
				jdbcDao.insert(cusM);
				return "邀请成功！";
			}else if(StringUtils.isNotBlank(cus.getCusPhone())){
				Customer newCustomer = queryCustomerByPhone(cus.getCusPhone());
				
				if(newCustomer==null){
					newCustomer=new Customer();
					newCustomer.setCusRegDatetime(new Date());
					newCustomer.setCusLogName("临时用户"+cus.getCusPhone());//临时用户以电话号码为昵称
					newCustomer.setCusPhone(cus.getCusPhone());
					newCustomer.setCusState(CustomerState.TEMP);
					newCustomer.setCusHeadImg("0.png");
					jdbcDao.insert(newCustomer);
				}
				CustomerFriendMapping cf=QueryObject.select(CustomerFriendMapping.class)
						.cond("CUS_ID").equ(cusId)
						.cond("FRIEND_ID").equ(newCustomer.getCusId()).firstResult(jdbcDao);
				//判断是否已经是好友。（存在通过搜索查询电话号码添加好友的可能）
				if(cf==null){
					//得到当前用户
					newCustomer=QueryObject.select(Customer.class)
							.cond("CUS_PHONE").equ(cus.getCusPhone()).firstResult(jdbcDao);
					
					CustomerFriendMapping cusM=new CustomerFriendMapping();
					cusM.setCusFriendMappingId(UUIDTool.getUUID());
					cusM.setCusId(cusId);
					cusM.setFriendId(newCustomer.getCusId());
					jdbcDao.insert(cusM);
					return "邀请成功";
				}
				
			}
		} catch (Exception e) {
			LogHelper.getLogger(this.getClass()).error(e.getMessage());
			throw new WorkException("保存失败！");
		}
		return "他已经是你的好友";
	}
	/**
	 * 查询微信是否已绑定
	 * @param wxOpenId
	 * @return
	 */
	public int CheckIsBinding(String wxOpenId){
		String sql="select count(1) from customer where WX_OPEN_ID=?";
		return this.jdbcDao.getJdbcTemplate().queryForObject(sql, new Object[]{wxOpenId},Integer.class).intValue();
	}
}
