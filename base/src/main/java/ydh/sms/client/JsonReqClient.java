/**
 * @author Glan.duanyj
 * @date 2014-05-12
 * @project rest_demo
 */
package ydh.sms.client;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ydh.sms.models.AppBill;
import ydh.sms.models.Callback;
import ydh.sms.models.ClientBill;
import ydh.sms.models.TemplateSMS;
import ydh.sms.models.VoiceCode;
import ydh.sms.utils.DateUtil;
import ydh.sms.utils.EncryptUtil;
import ydh.utils.ConfigTool;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("deprecation")
public class JsonReqClient extends AbsRestClient {
	private static Logger logger=LoggerFactory.getLogger(JsonReqClient.class);
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public String findAccoutInfo(String accountSid, String authToken)
			throws NoSuchAlgorithmException, KeyManagementException {
		String result = "";
		DefaultHttpClient httpclient=getDefaultHttpClient();
		try {
			//MD5加密
			EncryptUtil encryptUtil = new EncryptUtil();
			//构造请求URL内容
			String timestamp = DateUtil.dateToStr(new Date(),DateUtil.DATE_TIME_NO_SLASH);
			String signature=getSignature(accountSid, authToken,timestamp,encryptUtil);
			String url = getStringBuffer().append("/").append(version).
					append("/Accounts/").append(accountSid).append("")
					.append("?sig=").append(signature).toString();
			logger.info(url);
			HttpResponse response=get("application/json",accountSid,authToken,timestamp,url,httpclient,encryptUtil);
			//获取响应实体信息
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			// 确保HTTP响应内容全部被读出或者内容流被关闭
			EntityUtils.consume(entity);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			// 关闭连接
		    httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
	@Override
	public String createClient(String accountSid, String authToken, 
			String appId,String clientName,String chargeType
			,String charge,String mobile) {
		String result = "";
		DefaultHttpClient httpclient=getDefaultHttpClient();
		try {
			//MD5加密
			EncryptUtil encryptUtil = new EncryptUtil();
			//构造请求URL内容
			String timestamp = DateUtil.dateToStr(new Date(), DateUtil.DATE_TIME_NO_SLASH);
			String signature=getSignature(accountSid, authToken,timestamp,encryptUtil);
			String url = getStringBuffer().append("/").append(version).
					append("/Accounts/").append(accountSid)
					.append("/Clients")
					.append("?sig=").append(signature).toString();
			Client client=new Client();
			client.setAppId(appId);
			client.setFriendlyName(clientName);
			client.setClientType(chargeType);
			client.setCharge(charge);
			client.setMobile(mobile);
			String body = objectMapper.writeValueAsString(client);
			body="{\"client\":"+body+"}";
			HttpResponse response=post("application/json",accountSid, authToken, timestamp, url, httpclient, encryptUtil, body);
			//获取响应实体信息
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			// 确保HTTP响应内容全部被读出或者内容流被关闭
			EntityUtils.consume(entity);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			// 关闭连接
		    httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
	@Override
	public String findClients(String accountSid, String authToken,
			String appId, String start, String limit) {
		String result = "";
		DefaultHttpClient httpclient=getDefaultHttpClient();
		try {
			//MD5加密
			EncryptUtil encryptUtil = new EncryptUtil();
			//构造请求URL内容
			String timestamp = DateUtil.dateToStr(new Date(), DateUtil.DATE_TIME_NO_SLASH);
			String signature=getSignature(accountSid, authToken,timestamp,encryptUtil);
			String url = getStringBuffer().append("/").append(version)
					.append("/Accounts/").append(accountSid)
					.append("/clientList")
					.append("?sig=").append(signature).toString();
			Client client=new Client();
			client.setAppId(appId);
			client.setStart(start);
			client.setLimit(limit);
			String body = objectMapper.writeValueAsString(client);
			body="{\"client\":"+body+"}";
			logger.info(body);
			HttpResponse response=post("application/json",accountSid, authToken, timestamp, url, httpclient, encryptUtil, body);
			//获取响应实体信息
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			// 确保HTTP响应内容全部被读出或者内容流被关闭
			EntityUtils.consume(entity);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			// 关闭连接
		    httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
	@Override
	public String findClientByNbr(String accountSid, String authToken,String clientNumber,String appId) {
		String result = "";
		DefaultHttpClient httpclient=getDefaultHttpClient();
		try {
			//MD5加密
			EncryptUtil encryptUtil = new EncryptUtil();
			//构造请求URL内容
			String timestamp = DateUtil.dateToStr(new Date(), DateUtil.DATE_TIME_NO_SLASH);
			String signature=getSignature(accountSid, authToken,timestamp,encryptUtil);
			String url = getStringBuffer().append("/").append(version)
					.append("/Accounts/").append(accountSid)
					.append("/Clients")
					.append("?sig=").append(signature)
					.append("&clientNumber=").append(clientNumber)
					.append("&appId=").append(appId)
					.toString();
			HttpResponse response=get("application/json",accountSid, authToken, timestamp, url, httpclient, encryptUtil);
			//获取响应实体信息
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			EntityUtils.consume(entity);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			// 关闭连接
		    httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
	@Override
	public String closeClient(String accountSid, String authToken, String clientId,String appId) {
		String result = "";
		DefaultHttpClient httpclient=getDefaultHttpClient();
		try {
			//MD5加密
			EncryptUtil encryptUtil = new EncryptUtil();
			// 构造请求URL内容
			String timestamp = DateUtil.dateToStr(new Date(),
					DateUtil.DATE_TIME_NO_SLASH);// 时间戳
			String signature =getSignature(accountSid,authToken,timestamp,encryptUtil);
			String url = getStringBuffer().append("/").append(version)
					.append("/Accounts/").append(accountSid)
					.append("/dropClient")
					.append("?sig=").append(signature).toString();
			Client client=new Client();
			client.setClientNumber(clientId);
			client.setAppId(appId);
			String body = objectMapper.writeValueAsString(client);
			body="{\"client\":"+body+"}";
			logger.info(body);
			HttpResponse response=post("application/json",accountSid, authToken, timestamp, url, httpclient, encryptUtil, body);
			//获取响应实体信息
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			// 确保HTTP响应内容全部被读出或者内容流被关闭
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			// 关闭连接
		    httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
	@Override
	public String charegeClient(String accountSid, String authToken,
			String clientNumber, String chargeType, String charge,String appId) {
		String result = "";
		DefaultHttpClient httpclient=getDefaultHttpClient();
		try {
			//MD5加密
			EncryptUtil encryptUtil = new EncryptUtil();
			// 构造请求URL内容
			String timestamp = DateUtil.dateToStr(new Date(),
					DateUtil.DATE_TIME_NO_SLASH);// 时间戳
			String signature =getSignature(accountSid,authToken,timestamp,encryptUtil);
			String url = getStringBuffer().append("/2014-06-30/Accounts/").append(accountSid)
					.append("/chargeClient")
					.append("?sig=").append(signature).toString();
			Client client=new Client();
			client.setClientNumber(clientNumber);
			client.setChargeType(chargeType);
			client.setCharge(charge);
			client.setAppId(appId);
			String body = objectMapper.writeValueAsString(client);
			body="{\"client\":"+body+"}";
			logger.info(body);
			HttpResponse response=post("application/json",accountSid, authToken, timestamp, url, httpclient, encryptUtil, body);
			//获取响应实体信息
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			// 确保HTTP响应内容全部被读出或者内容流被关闭
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			// 关闭连接
		    httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
	@Override
	public String billList(String accountSid, String authToken,
			String appId,String date) {
				String result = "";
				DefaultHttpClient httpclient=getDefaultHttpClient();
				try {
					//MD5加密
					EncryptUtil encryptUtil = new EncryptUtil();
					// 构造请求URL内容
					String timestamp = DateUtil.dateToStr(new Date(),
							DateUtil.DATE_TIME_NO_SLASH);// 时间戳
					String signature =getSignature(accountSid,authToken,timestamp,encryptUtil);
					String url = getStringBuffer().append("/").append(version)
							.append("/Accounts/").append(accountSid)
							.append("/billList")
							.append("?sig=").append(signature).toString();
					AppBill appBill=new AppBill();
					appBill.setAppId(appId);
					appBill.setDate(date);
					String body = objectMapper.writeValueAsString(appBill);
					body="{\"appBill\":"+body+"}";
					logger.info(body);
					HttpResponse response=post("application/json",accountSid, authToken, timestamp, url, httpclient, encryptUtil, body);
					//获取响应实体信息
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						result = EntityUtils.toString(entity, "UTF-8");
					}
					// 确保HTTP响应内容全部被读出或者内容流被关闭
					EntityUtils.consume(entity);
				} catch (Exception e) {
					e.printStackTrace();
				} finally{
					// 关闭连接
				    httpclient.getConnectionManager().shutdown();
				}
				return result;
	}
	@Override
	public String clientBillList(String accountSid, String authToken,
			String appId,String clientNumber,String date) {
				String result = "";
				DefaultHttpClient httpclient=getDefaultHttpClient();
				try {
					//MD5加密
					EncryptUtil encryptUtil = new EncryptUtil();
					// 构造请求URL内容
					String timestamp = DateUtil.dateToStr(new Date(),
							DateUtil.DATE_TIME_NO_SLASH);// 时间戳
					String signature =getSignature(accountSid,authToken,timestamp,encryptUtil);
					String url = getStringBuffer().append("/").append(version)
							.append("/Accounts/").append(accountSid)
							.append("/Clients/billList")
							.append("?sig=").append(signature).toString();
					ClientBill clientBill=new ClientBill();
					clientBill.setAppId(appId);
					clientBill.setClientNumber(clientNumber);
					clientBill.setDate(date);
					String body = objectMapper.writeValueAsString(clientBill);
					body="{\"clientBill\":"+body+"}";
					logger.info(body);
					HttpResponse response=post("application/json",accountSid, authToken, timestamp, url, httpclient, encryptUtil, body);
					//获取响应实体信息
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						result = EntityUtils.toString(entity, "UTF-8");
					}
					// 确保HTTP响应内容全部被读出或者内容流被关闭
					EntityUtils.consume(entity);
				} catch (Exception e) {
					e.printStackTrace();
				} finally{
					// 关闭连接
				    httpclient.getConnectionManager().shutdown();
				}
				return result;
	}
	@Override
	public String callback(String accountSid, String authToken, String appId,
			String fromClient, String to,String fromSerNum,String toSerNum) {
		String result = "";
		DefaultHttpClient httpclient=getDefaultHttpClient();
		try {
			//MD5加密
			EncryptUtil encryptUtil = new EncryptUtil();
			// 构造请求URL内容
			String timestamp = DateUtil.dateToStr(new Date(),DateUtil.DATE_TIME_NO_SLASH);// 时间戳
			String signature =getSignature(accountSid,authToken,timestamp,encryptUtil);
			String url = getStringBuffer().append("/").append(version)
					.append("/Accounts/").append(accountSid)
					.append("/Calls/callBack")
					.append("?sig=").append(signature).toString();
			Callback callback=new Callback();
			callback.setAppId(appId);
			callback.setFromClient(fromClient);
			callback.setTo(to);
			callback.setFromSerNum(fromSerNum);
			callback.setToSerNum(toSerNum);
			String body = objectMapper.writeValueAsString(callback);
			body="{\"callback\":"+body+"}";
			logger.info(body);
			HttpResponse response=post("application/json",accountSid, authToken, timestamp, url, httpclient, encryptUtil, body);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			// 关闭连接
		    httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
	@Override
	public String voiceCode(String accountSid, String authToken, String appId,
			String to, String verifyCode) {
		String result = "";
		DefaultHttpClient httpclient=getDefaultHttpClient();
		try {
			//MD5加密
			EncryptUtil encryptUtil = new EncryptUtil();
			// 构造请求URL内容
			String timestamp = DateUtil.dateToStr(new Date(),
					DateUtil.DATE_TIME_NO_SLASH);// 时间戳
			String signature =getSignature(accountSid,authToken,timestamp,encryptUtil);
			String url = getStringBuffer().append("/").append(version)
					.append("/Accounts/").append(accountSid)
					.append("/Calls/voiceCode")
					.append("?sig=").append(signature).toString();
			VoiceCode voiceCode=new VoiceCode();
			voiceCode.setAppId(appId);
			voiceCode.setVerifyCode(verifyCode);
			voiceCode.setTo(to);
			String body = objectMapper.writeValueAsString(voiceCode);
			body="{\"voiceCode\":"+body+"}";
			logger.info(body);
			HttpResponse response=post("application/json",accountSid, authToken, timestamp, url, httpclient, encryptUtil, body);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			// 关闭连接
		    httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
	@Override
	public String templateSMS(String templateId, String to, String param) {
		String result = "";
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ConfigTool.getString("AccessKeyID"), ConfigTool.getString("AccessKeySecret"));
		try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Sms", "sms.aliyuncs.com");
		} catch (ClientException e1) {
			e1.printStackTrace();
		}
		IAcsClient client = new DefaultAcsClient(profile);
		SingleSendSmsRequest request = new SingleSendSmsRequest();
		try {
			request.setSignName(ConfigTool.getString("signName"));
			request.setTemplateCode(templateId);
			request.setParamString("{\"name\" : \"gaojunyu\"}");
			request.setRecNum("15608021324");
			SingleSendSmsResponse httpResponse = client.getAcsResponse(request);
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
		DefaultHttpClient httpclient=getDefaultHttpClient();
		try {
			//MD5加密
			EncryptUtil encryptUtil = new EncryptUtil();
			// 构造请求URL内容
			String timestamp = DateUtil.dateToStr(new Date(),
					DateUtil.DATE_TIME_NO_SLASH);// 时间戳
			String signature =getSignature(sid,authToken,timestamp,encryptUtil);
			String url = getStringBuffer().append("/").append(version)
					.append("/Accounts/").append(sid)
					.append("/Messages/templateSMS")
					.append("?sig=").append(signature).toString();
			TemplateSMS templateSMS=new TemplateSMS();
			templateSMS.setAppId(appId);
			templateSMS.setTemplateId(templateId);
			templateSMS.setTo(to);
			templateSMS.setParam(param);
			String body = objectMapper.writeValueAsString(templateSMS);
			body="{\"templateSMS\":"+body+"}";
			logger.info(body);
			HttpResponse response=post("application/json",sid, authToken, timestamp, url, httpclient, encryptUtil, body);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			// 关闭连接
		    httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
	@Override
	public String templateSMSCall(String templateId, String to, String validcode) {
		String result = "";
		DefaultHttpClient httpclient=getDefaultHttpClient();
		try {
			//MD5加密
			EncryptUtil encryptUtil = new EncryptUtil();
			// 构造请求URL内容
			String timestamp = DateUtil.dateToStr(new Date(),
					DateUtil.DATE_TIME_NO_SLASH);// 时间戳
			String signature =getSignature(sid,authToken,timestamp,encryptUtil);
			String url = getStringBuffer().append("/").append(version)
					.append("/Accounts/").append(sid)
					.append("/Calls/voiceCode")
					.append("?sig=").append(signature).toString();
			TemplateSMS templateSMS=new TemplateSMS();
			templateSMS.setAppId(appId);
			templateSMS.setTemplateId(templateId);
			templateSMS.setTo(to);
			templateSMS.setVerifyCode(validcode);
			//templateSMS.setParam(param);
			String body = objectMapper.writeValueAsString(templateSMS);
			body="{\"voiceCode\":"+body+"}";
			logger.info(body);
			HttpResponse response=post("application/json",sid, authToken, timestamp, url, httpclient, encryptUtil, body);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			// 关闭连接
		    httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
	
	
	
	
	
	
	@Override
	public String findClientByMobile(String accountSid, String authToken,
			String mobile, String appId) {
		String result = "";
		DefaultHttpClient httpclient=getDefaultHttpClient();
		try {
			//MD5加密
			EncryptUtil encryptUtil = new EncryptUtil();
			//构造请求URL内容
			String timestamp = DateUtil.dateToStr(new Date(), DateUtil.DATE_TIME_NO_SLASH);
			String signature=getSignature(accountSid, authToken,timestamp,encryptUtil);
			String url = getStringBuffer().append("/").append(version)
					.append("/Accounts/").append(accountSid)
					.append("/ClientsByMobile")
					.append("?sig=").append(signature)
					.append("&mobile=").append(mobile)
					.append("&appId=").append(appId)
					.toString();
			HttpResponse response=get("application/json",accountSid, authToken, timestamp, url, httpclient, encryptUtil);
			//获取响应实体信息
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			EntityUtils.consume(entity);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			// 关闭连接
		    httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
	@Override
	public String dispalyNumber(String accountSid, String authToken,
			String appId, String clientNumber, String display) {
		String result = "";
		DefaultHttpClient httpclient=getDefaultHttpClient();
		try {
			//MD5加密
			EncryptUtil encryptUtil = new EncryptUtil();
			//构造请求URL内容
			String timestamp = DateUtil.dateToStr(new Date(), DateUtil.DATE_TIME_NO_SLASH);
			String signature=getSignature(accountSid, authToken,timestamp,encryptUtil);
			String url = getStringBuffer().append("/").append(version)
					.append("/Accounts/").append(accountSid)
					.append("/dispalyNumber")
					.append("?sig=").append(signature)
					.append("&appId=").append(appId)
					.append("&clientNumber=").append(clientNumber)
					.append("&display=").append(display)
					.toString();
			HttpResponse response=get("application/json",accountSid, authToken, timestamp, url, httpclient, encryptUtil);
			//获取响应实体信息
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			EntityUtils.consume(entity);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			// 关闭连接
		    httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
}
