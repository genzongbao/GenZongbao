package ydh.message.push;

import ydh.cicada.dict.YesNo;

public class PushSetting {

	/** 是否允许APP推送:0=否，1=是*/
	private YesNo allowApp;

	/** 是否允许微信推送:0=否，1=是*/
	private YesNo allowWx;

	/** 是否允许电子邮件推送:0=否，1=是*/
	private YesNo allowEmail;

	/** 是否允许短信推送:0=否，1=是*/
	private YesNo allowSms;
	
	/**
	 * 
	 * @param allowApp  是否开启app推送
	 * @param allowWx	是否开启微信推送
	 * @param allowEmail是否开启Email推送
	 * @param allowSms	是否开启短信推送
	 */
	public PushSetting(boolean allowApp,boolean allowWx,boolean allowEmail,boolean allowSms) {
		this.allowApp = allowApp ? YesNo.YES : YesNo.NO;
		this.allowWx = allowWx ? YesNo.YES : YesNo.NO;
		this.allowEmail = allowEmail ? YesNo.YES : YesNo.NO;
		this.allowSms = allowSms ? YesNo.YES : YesNo.NO;
	}
	
	public PushSetting() {}

	public YesNo getAllowSms() {
		return allowSms;
	}

	public void setAllowSms(YesNo allowSms) {
		this.allowSms = allowSms;
	}

	public YesNo getAllowApp() {
		return allowApp;
	}

	public void setAllowApp(YesNo allowApp) {
		this.allowApp = allowApp;
	}

	public YesNo getAllowWx() {
		return allowWx;
	}

	public void setAllowWx(YesNo allowWx) {
		this.allowWx = allowWx;
	}

	public YesNo getAllowEmail() {
		return allowEmail;
	}

	public void setAllowEmail(YesNo allowEmail) {
		this.allowEmail = allowEmail;
	}

}
