/**
 * 
 */
package ydh.mvc.ex;


/**
 * @author Administrator
 *
 */
public class InterfaceException extends Exception {
//	private static final Logger logger = Logger.getLogger(InterfaceException.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;

	/**
	 * Service异常构造方法（不带参数）
	 */
	public InterfaceException() {
		super();
	}

	/**Service异常构造方法
	 * @param message DAO异常消息
	 */
	public InterfaceException(String message) {
		super(message);
	}

	/**Service异常构造方法
	 * @param code  异常代码<br>
	 * @param message  异常消息<br>
	 */
	public InterfaceException(String code, String message) {
		super(message);
		this.code = code;
	}

	/**Service异常构造方法
	 * @param cause  被封装异常类型	
	 */
	public InterfaceException(Throwable cause) {
		super(cause);
	}

	/**Service异常构造方法
	 * @param message 异常消息
	 * @param cause 被封装异常类型	
	 */
	public InterfaceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**Service异常构造方法
	 * @param code 异常代码
	 * @param message 异常消息
	 * @param cause 被封装异常类型	
	 */
	public InterfaceException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
