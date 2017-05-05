package ydh.customer.service;

import ydh.mvc.BaseResult;


/**
 * 用户标签Service
 * @author tearslee
 *
 */
public interface CusTagService {

	/**
	 * 添加、修改用户标签
	 * @param CusTagName
	 * @param cusId
	 * @return
	 */
	public BaseResult addCusTag(String result);
	/**
	 * 删除用户标签
	 * @param CusTagName
	 * @param cusId
	 * @return
	 */
	public BaseResult deleteCusTag(String result);
	/**
	 * 从标签内删除联系人
	 * @param result
	 * @return
	 */
	public BaseResult deleteCusFromTag(String result, String cusTagId);
	
	
	/**
	 * 为标签添加联系人
	 * @param CusTagId  标签id
	 * @param cusNodes
	 * @return
	 */
	public BaseResult addCusToTag(String CusTagId, String result);
	
	/**
	 * 查询用户标签列表
	 * @param cusId
	 * @return
	 */
	public BaseResult getCusTags(String result);
	/**
	 * 查询标签下的用户列表
	 * @param baseResult
	 * @return
	 */
	public BaseResult getCusList(String baseResult);
	/**
	 * 标记好友列表是否选择
	 * @param result
	 * @return
	 */
	public BaseResult signCustomer(String result);
}
