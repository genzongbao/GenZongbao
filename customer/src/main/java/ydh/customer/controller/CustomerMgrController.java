package ydh.customer.controller;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ydh.customer.entity.Customer;
import ydh.customer.service.CusTagService;
import ydh.customer.service.CustomerService;
import ydh.mvc.ex.WorkException;
import ydh.utils.CheckTool;
import ydh.utils.GsonUtil;
import ydh.website.localization.controller.BaseController;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 用户管理，联系人添加
 * @author tearslee
 *
 */
@Controller
@RequestMapping("cus/mgr")
public class CustomerMgrController extends BaseController{
	@Autowired
	private CustomerService cusService;
	@Autowired
	private CusTagService cusTagService;
	/**
	 * 得到朋友列表
	 * @return
	 */
	@RequestMapping(value="friends-list", produces = "text/html;charset=utf8")
	public String getCusFriends(){
		
		return "";
	}
	/**
	 * 校验本地联系人列表
	 * 若为好友则friend=true
	 * 若为平台用户则cusId!=0
	 * @return
	 */
	@RequestMapping(value="check-local-people", produces = "text/html;charset=utf8")
	@ResponseBody
	public String checkLocalFriends(Integer cusId, @RequestBody String local){
		Type type = new TypeToken<List<Customer>>() {}.getType();
		List<Customer> localCus = GsonUtil.toObjectChange(local, type);
		returnResult.setData(cusService.checkListIsFriend(localCus, cusId));
		return resultVal();
	}
	
	/**
	 * 添加好友
	 * @param cusId
	 * @param customer
	 * @return
	 * @throws WorkException
	 */
	@ResponseBody
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="add-friend", produces = "text/html;charset=utf8")
	public String addFriend(int cusId,@RequestBody String customer) throws WorkException{
		Customer cus = GsonUtil.toObjectChange(customer, Customer.class);
		returnResult.setMsg(cusService.addFriends(cus,cusId));
		return resultVal();
	}
	/**
	 * 校验本地联系人列表
	 * @return
	 */
	@RequestMapping(value="check-local", produces = "text/html;charset=utf8")
	@ResponseBody
	public String checkLocal(Integer cusId, @RequestBody String local){
		Type type = new TypeToken<List<Customer>>() {}.getType();
		List<Customer> localCus = GsonUtil.toObjectChange(local, type);
		if(CheckTool.checkListIsNotNull(localCus)){
			for (Customer customer : localCus) {
				if(StringUtils.isNotBlank(customer.getCusPhone())){
					Customer tempCus=cusService.checkByPhone(customer.getCusPhone());
					if(cusService.checkByPhone(customer.getCusPhone())!=null){
						customer.setCusState(tempCus.getCusState());
						customer.setChecked(true);
					}
				}
			}
		}
		returnResult.setData(localCus);
		return resultVal();
	}
	/**
	 * 添加标签
	 * @param cusId
	 * @param cusTagNode
	 * @return
	 */
	@ResponseBody
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="add-custag", produces = "text/html;charset=utf8")
	public String addCusTag(@RequestBody String result){
		returnResult.setData(cusTagService.addCusTag(result));
		return resultVal();
	}
	/**
	 * 删除标签
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="delete-custag", produces = "text/html;charset=utf8")
	public String deleteCusTag(@RequestBody String result){		
		returnResult=cusTagService.deleteCusTag(result);
		return resultVal();
	}
	/**
	 * 获取标签列表
	 * @param cusId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="get-custags", produces = "text/html;charset=utf8")
	public String getCusTags(@RequestBody String result){
		returnResult=cusTagService.getCusTags(result);
		return resultVal();
	}
	
	/**
	 * 添加联系人到标签
	 * @param cusTagId
	 * @param cusId
	 * @param customers
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="add-cus-to-tag", produces = "text/html;charset=utf8")
	public String addCusToTag(String cusTagId,@RequestBody String result){
		returnResult=cusTagService.addCusToTag(cusTagId, result);
		return resultVal();
	}
	/**
	 * 获取标签下的联系人列表
	 * @param cusTagId
	 * @param cusId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="get-custag-cuslist", produces = "text/html;charset=utf8")
	public String getCusList(@RequestBody String result){
		returnResult=cusTagService.getCusList(result);
		return resultVal();
	}
	/**
	 * 获取标记过的好友列表
	 * 传一个联系人列表，拿来与当前用户的好友列表进行对比。若同时存在，则checked=true
	 * @param result 已存在的列表
	 * @return  对比后的列表
	 */
	@ResponseBody
	@RequestMapping(value="sign-cus-flag", produces = "text/html;charset=utf8")
	public String signCusFlag(@RequestBody String result){
		returnResult=cusTagService.signCustomer(result);
		return resultVal();
	}
	/**
	 * 从标签内删除联系人
	 * @param result
	 * @param cusTagId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="delete-from-tag", produces = "text/html;charset=utf8")
	public String deleteCusFromTag(@RequestBody String result,String cusTagId){
		returnResult=cusTagService.deleteCusFromTag(result,cusTagId);
		return resultVal();
	}
}
