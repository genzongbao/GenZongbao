package ydh.customer.controller;

import ydh.customer.entity.BaseResult;
import ydh.customer.entity.Customer;
import ydh.utils.GsonUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 
 * @author tearslee
 *
 */
public class BaseController {
	
    @Resource
    protected HttpServletRequest request;

    @Resource
    protected HttpServletResponse response;

    @Resource
    protected HttpSession session;
	
	protected BaseResult returnResult=new BaseResult();
	
	protected Customer customer=new Customer();
	
	protected Date nowDate=new Date();
	

	public BaseResult getReturnResult() {
		return returnResult;
	}

	public void setReturnResult(BaseResult returnResult) {
		this.returnResult = returnResult;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public String resultVal(){
		return GsonUtil.toJSONString(this.returnResult);
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}
	
	
	
}
