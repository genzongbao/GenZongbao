package ydh.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.Notification;

public class JPushTool {
	private static final JPushClient jpushClient;

	static {
		jpushClient = new JPushClient(
				ConfigTool.getString(PushConfig.jpushSecretKey), 
				ConfigTool.getString(PushConfig.jpushAppKey));
	}

	/**
	 * 所有APP广播推送
	 * @param title
	 * @param type
	 * @param id
	 */
	public static void broadcast(String title, String content, String type, String id) {
		Map<String,String> extra = new HashMap<String,String>();
		extra.put("type", type);
		extra.put("id", id);
		extra.put("time", DateTimeUtil.DATETIME_FORMAT.format(new Date()));
//		 push android device
		Notification notifycation = Notification.newBuilder()
				.addPlatformNotification(
						AndroidNotification.newBuilder().setAlert(title).setTitle(content).addExtras(extra).build())
				.build();
	    PushPayload payload = PushPayload.newBuilder()
	    		.setPlatform(Platform.android())
	            .setAudience(Audience.all())
	            .setNotification(notifycation)
	            .setOptions(Options.newBuilder().setApnsProduction(true).build())
	            .build();
        try {
        	PushResult pushResult = jpushClient.sendPush(payload);
        	System.out.println(pushResult);
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	/**
	 * 指定设备APP广播推送
	 * @param title
	 * @param type
	 * @param id
	 */
	public static void push(String title, String content, String type, String id, String... deviceIds) {
		Map<String,String> extra = new HashMap<String,String>();
		extra.put("type", type);
		extra.put("id", id);
//		 push android device
		Notification notifycation = Notification.android(content, title, extra);
	    PushPayload payload = PushPayload.newBuilder()
	    		.setPlatform(Platform.android())
	            .setAudience(Audience.alias(deviceIds))
	            .setNotification(notifycation)
	            .build();
        try {
        	PushResult pushResult = jpushClient.sendPush(payload);
        	System.out.println(pushResult);
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	/**
	 * 跟踪宝推送
	 * @param title
	 * @param content
	 * @param ecid
	 */
	public static void pushAliasNoticeGzb(String title, String content,String conditionId,String url,List<String> key) {
		Map<String,String> extra = new HashMap<String,String>();
		extra.put("time", DateTimeUtil.formatDate(new Date()));
		extra.put("type", "notice");
		extra.put("id", conditionId);
		extra.put("url", url);
		extra.put("title", title);
		extra.put("content", content);
		PushPayload androidPayload = PushPayload.newBuilder()
				.setPlatform(Platform.android())
				.setAudience(Audience.alias(key))
				.setNotification(Notification.android(content, title, extra))
				.build();
		System.out.println(JsonUtil.toString(androidPayload));
		try {
			PushResult pushResult = jpushClient.sendPush(androidPayload);
			System.out.println(pushResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void pushAliasMessageGzb(String title, String content,Map<String,String> extra,String url,List<String> key) {
		PushPayload androidPayload = PushPayload.newBuilder()
				.setPlatform(Platform.android())
				.setAudience(Audience.alias(key))
				.setMessage(Message.newBuilder()
						.setMsgContent(content)
						.setTitle(title)
						.addExtras(extra)
						.build())
				.build();
		System.out.println(JsonUtil.toString(androidPayload));
		try {
			PushResult pushResult = jpushClient.sendPush(androidPayload);
			System.out.println(pushResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
