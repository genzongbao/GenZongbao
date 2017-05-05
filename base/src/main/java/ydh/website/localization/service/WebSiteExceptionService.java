package ydh.website.localization.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ydh.cicada.dao.JdbcDao;
import ydh.website.localization.entity.WebSiteException;

/**
 * @author 
 */
@Service
public class WebSiteExceptionService {

	@Autowired
	private JdbcDao jdbcDao;

	/**
	 * 记录网站异常
	 * @param WebSiteExceptionService
	 */
	@Transactional
	public void createWebSiteException(WebSiteException exception) {
		jdbcDao.insert(exception);
	}
	@Transactional
	public void createWebSiteException(String title,Throwable e) {
		WebSiteException exception = new WebSiteException();
		exception.setExceptionTitle(title);
		exception.setExceptionMessage(e.getMessage());
		exception.setExceptionContent(printStackTraceToString(e));
		exception.setExceptionTime(new Date());
		jdbcDao.insert(exception);
	}	
	
	/**
	 * 打印异常信息写法一
	 * @param t
	 * @return
	 */
	public static String printStackTraceToString(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw, true));
		return sw.getBuffer().toString();
	}
	/**
	 * 打印异常信息写法二
	 * @param t
	 * @return
	 */
	/*
	@SuppressWarnings("unused")
	private static String getStackTrace(Throwable t) {  
		StringWriter sw = new StringWriter();  
		PrintWriter pw = new PrintWriter(sw);  
		try {
			t.printStackTrace(pw);  
			return sw.toString(); 
		} finally {
			pw.close(); 
		}
	}  
	*/
}
