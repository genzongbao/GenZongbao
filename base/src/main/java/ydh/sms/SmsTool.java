package ydh.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import ydh.utils.ConfigTool;

public class SmsTool {
	/**
	 * 事件变更模板：${name}更新你加入的${eventName}事件动态。
	 */
	public static final String EVENT_CHANGE_TEMPLATE="SMS_49380016";	//事件变更模板：${name}更新你加入的${eventName}事件动态。
	/**
	 * 验证码模板：您的验证码为${code},30分钟内有效.
	 */
	public static final String VALIDCODE_TEMPLATE = "SMS_46250169";//验证码 ：您的验证码为${code},30分钟内有效.
	/**
	 * 验证码${code}，您正在进行${product}身份验证，打死不要告诉别人哦！
	 */
	public static final String VALIDCODE_IDENTITY = "SMS_46250173";
	/**
	 * 验证码${code}，您正在尝试修改${product}登录密码，请妥善保管账户信息。
	 */
	public static final String VALIDCODE_CHANGE_PWD = "SMS_46250167";
	
	/**
	 * 注册模板：验证码${code}，您正在注册成为${product}用户，感谢您的支持！
	 */
	public static final String REGIST_TEMPLATE = "SMS_46250169";//注册模板：验证码${code}，您正在注册成为${product}用户，感谢您的支持！
	private static final String ACCESS_KEY_ID = "LTAIWHZ5tmtqs7gP";
	private static final String ACCESS_KEY_SECRET = "g8sxcHdslcIn9pvqe1YhLpOxLFaSMZ";
	private static final String SIGN_NAME = "跟踪宝";
	
	@SuppressWarnings("unused")
	public static void sendSms(String mobile, String templateId, String... params) {
		getParamString(params);
		IClientProfile profile = DefaultProfile.getProfile("cn-shanghai", SmsTool.ACCESS_KEY_ID, SmsTool.ACCESS_KEY_SECRET);
		try {
			DefaultProfile.addEndpoint("cn-shanghai", "cn-shanghai", "Sms", "sms.aliyuncs.com");
		} catch (ClientException e1) {
			e1.printStackTrace();
		}
		IAcsClient client = new DefaultAcsClient(profile);
		SingleSendSmsRequest request = new SingleSendSmsRequest();
		try {
			request.setSignName(SIGN_NAME);
			request.setTemplateCode(templateId);
			request.setParamString(getParamString(params));
			request.setRecNum(mobile);
			SingleSendSmsResponse httpResponse = client.getAcsResponse(request);
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}
	
	private static String getParamString(String... params){
		StringBuffer sb = new StringBuffer("{\"");
		for (int i =0 ; i < params.length ; i++) {
			if(i > 0){
				sb.append(",\"");
			}
			sb.append(params[i]).append("\":\"").append(params[i+1]).append("\"");
			i++;
		}
		sb.append("}");
		return sb.toString();
	}
}
