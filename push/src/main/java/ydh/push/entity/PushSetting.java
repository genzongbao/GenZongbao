package ydh.push.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;
import ydh.cicada.dict.YesNo;

/**
 * 推送设置
 * @author Administrator
 *
 */
@Entity(name = "PUSH_SETTING")
public class PushSetting {
	@Id
	@Column(name = "PUSH_SETTING_ID")
	private String pushSettingId;
	
	@Column(name = "CUS_ID")
	private Integer cusId;
	
	@Column(name = "EMAIL")
	private YesNo email=YesNo.NO;
	
	@Column(name = "TELEPHONE")
	private YesNo sms=YesNo.YES;
	
	@Column(name = "WECHAT")
	private YesNo wechat=YesNo.NO;
	
	@Column(name = "IN_STATION")
	private YesNo instation=YesNo.YES;

	public PushSetting() {
	}

	public String getPushSettingId() {
		return pushSettingId;
	}

	public void setPushSettingId(String pushSettingId) {
		this.pushSettingId = pushSettingId;
	}

	public Integer getCusId() {
		return cusId;
	}

	public void setCusId(Integer cusId) {
		this.cusId = cusId;
	}

	public YesNo getEmail() {
		return email;
	}

	public void setEmail(YesNo email) {
		this.email = email;
	}


	public YesNo getSms() {
		return sms;
	}

	public void setSms(YesNo sms) {
		this.sms = sms;
	}

	public YesNo getWechat() {
		return wechat;
	}

	public void setWechat(YesNo wechat) {
		this.wechat = wechat;
	}

	public YesNo getInstation() {
		return instation;
	}

	public void setInstation(YesNo instation) {
		this.instation = instation;
	}

	
}
