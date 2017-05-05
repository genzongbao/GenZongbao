package ydh.website.localization.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.jpush.api.common.resp.APIRequestException;
import ydh.mvc.BaseResult;
import ydh.mvc.ErrorCode;
import ydh.utils.GsonUtil;

/**
 * 
 * @author tearslee
 *
 */
@Controller
@RequestMapping("base")
public class BaseController {
	
	protected static Log log= LogFactory.getLog(BaseController.class);
	
    @Resource
    protected HttpServletRequest request;

    @Resource
    protected HttpServletResponse response;

    @Resource
    protected HttpSession session;
	
	@SuppressWarnings("rawtypes")
	protected BaseResult returnResult=null;
	
	
	protected Date nowDate=new Date();
	
	@SuppressWarnings("rawtypes")
	@ModelAttribute("returnResult")
	@ResponseBody
	public String setReturnResult(HttpServletRequest request){
//		returnResult=(BaseResult) request.getAttribute("result");
//		if(returnResult==null){
//			returnResult=new BaseResult();
//		}
//		if(returnResult!=null && returnResult.isError()){
//			returnResult=new BaseResult();
//		}
		returnResult=new BaseResult();
		return resultVal();
	}
	
    @SuppressWarnings("rawtypes")
	@ExceptionHandler
    @ResponseBody
    public String exception(HttpServletRequest request, Exception e) {  
    	returnResult=new BaseResult();
    	
    	if(e instanceof BadSqlGrammarException){    		
    		log.error("SQL错误："+((BadSqlGrammarException)e).getSql()+
    				"\n Cause:"+((BadSqlGrammarException)e).getCause());
    		returnResult.setErrorCode(ErrorCode.SERVER_EXCEPTION);
    	}else if(e instanceof UnauthorizedException || e instanceof AuthorizationException){
    		returnResult.setErrorCode(ErrorCode.NO_LOGIN);
    	}else if(e instanceof AuthenticationException){
    		log.error(((AuthenticationException)e).getMessage());
    		returnResult.setMsg(((AuthenticationException)e).getMessage());
    		returnResult.setErrorCode(ErrorCode.SERVER_EXCEPTION);
    	}else if(e instanceof APIRequestException){
    		log.error(((APIRequestException)e).getMessage());
    		returnResult.setMsg("发送push数据失败");
    		returnResult.setErrorCode(ErrorCode.SERVER_EXCEPTION);
    	}else{
    		e.printStackTrace();
    	}
        returnResult.setError(true);
		return resultVal();
    }  
    @RequestMapping("/difined")
    public String difined(HttpServletRequest request,HttpServletResponse response){
    	System.err.println("difined");
    	writeJson(response);
    	return null;
    }

    @RequestMapping("/reLogin")
    public String loginSessionFiled(HttpServletRequest request,HttpServletResponse response){
    	returnResult.setErrorCode(ErrorCode.NO_LOGIN);
    	returnResult.setError(true);
    	returnResult.setMsg("对不起，您的登录已失效，请重新登录。");
    	writeJson(response);
    	return null;
    }
    private void writeJson(HttpServletResponse response){
    	 PrintWriter out = null;
         try {
             response.setCharacterEncoding("UTF-8");
             response.setContentType("application/json; charset=utf-8");
             out = response.getWriter();
             out.write(resultVal()==null?"{}":resultVal());
         } catch (IOException e) {
             e.printStackTrace();
         } finally {
             if (out != null) {
                 out.close();
             }
         }
    }
    
	@SuppressWarnings("rawtypes")
	public BaseResult getReturnResult() {
		return returnResult;
	}

	@SuppressWarnings("rawtypes")
	public void setReturnResult(BaseResult returnResult) {
		this.returnResult = returnResult;
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

	
	
	
}
