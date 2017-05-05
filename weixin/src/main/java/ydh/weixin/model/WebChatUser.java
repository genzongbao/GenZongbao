package ydh.weixin.model;

/**
 * 微信用户信息模型
 * @author LXL
 *
 */
public class WebChatUser {

    private String subscribe;
    private String openid;
    private String nickname;
    private String sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String subscribe_time;
    private String remark;
    private String groupid;
    private String[] tagid_list;
    private String errcode;
    private String errmsg;
    
    
	public String getSubscribe() {
		return subscribe;
	}
	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public String getSubscribe_time() {
		return subscribe_time;
	}
	public void setSubscribe_time(String subscribe_time) {
		this.subscribe_time = subscribe_time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public String[] getTagid_list() {
		return tagid_list;
	}
	public void setTagid_list(String[] tagid_list) {
		this.tagid_list = tagid_list;
	}
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
//	"subscribe":1,
//	"openid":"oX-S_vvIkbZaO0HNVY-GiRVeR-uQ",
//	"nickname":"零灵妖",
//	"sex":1,
//	"language":"zh_CN",
//	"city":"成都",
//	"province":"四川",
//	"country":"中国",
//	"headimgurl":"http:\/\/wx.qlogo.cn\/mmopen\/AiaaLotC35ALKq6S52XOkfuYhcyVGQTDvY69RPsfnkUwSs8wPztibVv1nr5qjtpEoCW9kowzWMVrzTa098PnqTZALDNFNibCApM\/0",
//	"subscribe_time":1452089628,
//	"remark":"",
//	"groupid":0}

}
