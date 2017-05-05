package ydh.mvc.ex;

/**
 * 系统预料中的工作异常，工作异常的标准处理方式为返回原页面并显示提示消息
 * @author lizx
 */
public class WorkException extends Exception {
	private static final long serialVersionUID = 1488299955762200642L;
	
	private String fieldName;
	
	public WorkException(String message) {
		super(message);
	}
	
	public WorkException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public WorkException(String fieldName, String message) {
		super(message);
	}
	
	public WorkException(String fieldName, String message, Throwable cause) {
		super(message, cause);
	}
	
	public String getFieldName() {
		return this.fieldName;
	}
}
