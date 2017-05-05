package ydh.customer.form;

public class LoginForm {
	
	
	private String cusLogName;
	//现在已经改为手机号码登录
	private String cusPhone;
	private String cusPassword;
	private String validcode;
	
	public String getCusLogName() {
		return cusLogName;
	}
	public void setCusLogName(String cusLogName) {
		this.cusLogName = cusLogName;
	}
	public String getCusPhone() {
		return cusPhone;
	}
	public void setCusPhone(String cusPhone) {
		this.cusPhone = cusPhone;
	}
	public String getCusPassword() {
		return cusPassword;
	}
	public void setCusPassword(String cusPassword) {
		this.cusPassword = cusPassword;
	}
	public String getValidcode() {
		return validcode;
	}
	public void setValidcode(String validcode) {
		this.validcode = validcode;
	}
	
}
