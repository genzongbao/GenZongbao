package ydh.weixin.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;

import java.io.Serializable;

/**
 * @author GenEntity
 * @version 1.0.1
 */
@Entity(name = WeixinUser.TABLE_NAME)
public class WeixinUser implements Serializable{

	private static final long serialVersionUID = -6072516157832001977L;

	public static final String TABLE_NAME = "WEIXIN_USER";

	/** userId */
	public static final String USER_ID = "USER_ID";
	@Id
	@Column(name = WeixinUser.USER_ID)
	private Integer userId;

	/** openId */
	public static final String OPEN_ID = "OPEN_ID";
	@Column(name = WeixinUser.OPEN_ID)
	private String openId;

	public WeixinUser() {
		super();
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getOpenId() {
		return this.openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

}