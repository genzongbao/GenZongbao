package ydh.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.chainsaw.Main;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class PushUtil {
	//以下是双赢地带的配置文件，以*****结束
	public static final String APP_KEY = "3f5797af8d01ef4734c1a4b4";
	public static final String SECRET_KEY = "85fffa85c79fbd95c93ce5d9";
	public static final String MESSAGE_TYPE_NEWS = "news";
	public static final String MESSAGE_TYPE_PROJECT = "project";
	private static final JPushClient jpushClient = new JPushClient(SECRET_KEY, APP_KEY);
	//***************

	//贷后宝配置文件，以*****结束
	public static final String DHB_APP_KEY = "30f1c29f1ef63f18daa2b30b";
	public static final String DHB_SECRET_KEY = "133d8907eea4419bd8c021c2";
	private static final JPushClient client = new JPushClient(DHB_SECRET_KEY, DHB_APP_KEY);
	private static Log log = LogFactory.getLog(Main.class.getName());  
	//***************
	/**
	 * 双赢地带推送
	 * @param title
	 * @param type
	 * @param id
	 */
	public static void push(String title, String type, String id) {
		Map<String,String> extra = new HashMap<String,String>();
		extra.put("type", type);
		extra.put("id", id);
		//		 push android device
		Notification notifycation = Notification.newBuilder()
				.addPlatformNotification(
						IosNotification.newBuilder().setAlert(title).setBadge(0).addExtras(extra).build())
						.addPlatformNotification(
								AndroidNotification.newBuilder().setAlert(title).setTitle("双赢地带").addExtras(extra).build())
								.build();
		PushPayload androidPayload = PushPayload.newBuilder()
				.setPlatform(Platform.android_ios())
				.setAudience(Audience.all())
				.setNotification(notifycation)
				.setOptions(Options.newBuilder().setApnsProduction(true).build())
				.build();
		try {
			PushResult pushResult = jpushClient.sendPush(androidPayload);
			System.out.println(pushResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 贷后宝安卓推送群发消息
	 * @param title
	 * @param content
	 */
	public static void pushDhb(String title, String content) {
		Map<String,String> extra = new HashMap<String,String>();
		extra.put("time", DateTimeUtil.formatDate(new Date()));
		extra.put("type", "notice");
		extra.put("url", "");
		PushPayload androidPayload = PushPayload.newBuilder()
				.setPlatform(Platform.android())
				.setAudience(Audience.all())
				.setNotification(Notification.android(content, title, extra))
				.build();
		try {
			PushResult pushResult = client.sendPush(androidPayload);
			System.out.println(pushResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 贷后宝安卓别名推送消息
	 * @param title
	 * @param content
	 * @param ecid
	 */
	public static void pushAliasDhb(String title, String content,String ecid) {
		Map<String,String> extra = new HashMap<String,String>();
		List<String> key = new ArrayList<String>();
		key.add(ecid);
		extra.put("time", DateTimeUtil.formatDate(new Date()));
		extra.put("type", "notice");
		extra.put("url", "");
		extra.put("title", title);
		extra.put("content", content);
		PushPayload androidPayload = PushPayload.newBuilder()
				.setPlatform(Platform.android())
				.setAudience(Audience.alias(key))
				.setNotification(Notification.android(content, title, extra))
				.build();
		System.out.println(JsonUtil.toString(androidPayload));
		try {
			PushResult pushResult = client.sendPush(androidPayload);
			System.out.println(pushResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**

	 *这是一个比较简单的推送方法，

	 * apple的推送方法

	 * @param tokens   iphone手机获取的token

	 * @param path 这里是一个.p12格式的文件路径，需要去apple官网申请一个 

	 * @param password  p12的密码 此处注意导出的证书密码不能为空因为空密码会报错

	 * @param message 推送消息的内容

	 * @param count 应用图标上小红圈上的数值

	 * @param sendCount 单发还是群发  true:单发 false:群发

	 */
	public static void sendpush(List<String> tokens,String path, String password, String message,Integer count,boolean sendCount) {
		try {  
			//message是一个json的字符串{“aps”:{“alert”:”iphone推送测试”}}  
			PushNotificationPayload payLoad =  PushNotificationPayload.fromJSON(message);  
			payLoad.addAlert("iphone推送测试 www.baidu.com"); // 消息内容  
			payLoad.addBadge(count); // iphone应用图标上小红圈上的数值  
			payLoad.addSound("default"); // 铃音 默认  
			payLoad.addCustomDictionary("系統消息", "我是新加的字段");
			PushNotificationManager pushManager = new PushNotificationManager();  
			//true:表示的是产品发布推送服务 false:表示的是产品测试推送服务  
			pushManager.initializeConnection(new AppleNotificationServerBasicImpl(path, password, false));  
			List<PushedNotification> notifications = new ArrayList<PushedNotification>();   
			// 发送push消息  
			if (sendCount) {  
				log.error("--------------------------apple 推送 单-------");  
				Device device = new BasicDevice();  
				device.setToken(tokens.get(0));  
				PushedNotification notification = pushManager.sendNotification(device, payLoad, true);  
				notifications.add(notification);  
			} else {  
				log.error("--------------------------apple 推送 群-------");  
				List<Device> device = new ArrayList<Device>();  
				for (String token : tokens) {  
					device.add(new BasicDevice(token));  
				}  
				notifications = pushManager.sendNotifications(payLoad, device);  
			}  
			List<PushedNotification> failedNotifications = PushedNotification.findFailedNotifications(notifications);  
			List<PushedNotification> successfulNotifications = PushedNotification.findSuccessfulNotifications(notifications);  
			int failed = failedNotifications.size();  
			int successful = successfulNotifications.size();  
			if (successful > 0 && failed == 0) {  
				log.error("-----All notifications pushed 成功 (" + successfulNotifications.size() + "):");  
			} else if (successful == 0 && failed > 0) {  
				log.error("-----All notifications 失败 (" + failedNotifications.size() + "):");  
			} else if (successful == 0 && failed == 0) {  
				System.out.println("No notifications could be sent, probably because of a critical error");  
			} else {  
				log.error("------Some notifications 失败 (" + failedNotifications.size() + "):");  
				log.error("------Others 成功 (" + successfulNotifications.size() + "):");  
			}  
			pushManager.stopConnection();  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
	}
	//-----------推送内容---------

	//A 公告

	//B 新标

	//-----------推送客户端---------

	//A PC-站内信

	//B IOS

	//C Android

	//-----------推送方式-----------

	//A JPush
	//B .....(短信、邮箱)
}
