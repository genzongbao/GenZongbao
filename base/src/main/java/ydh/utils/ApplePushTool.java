package ydh.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServer;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotifications;

public class ApplePushTool {
	private static final AppleNotificationServer server;
	
	/**
	 * IOS推送
	 * @param server 推送服务器
	 * @param title  推送消息标题
	 * @param type   推送消息类型
	 * @param id     推送消息ID
	 * @param deviceIds  设备列表
	 */
	@SuppressWarnings("unused")
	public static void push(String title, String type, String id, String... deviceIds) {
		if (deviceIds.length == 0) return;
		try {
			PushNotificationPayload payload = PushNotificationPayload.alert(title);
			payload.addCustomDictionary("type", type);
			payload.addCustomDictionary("id", id);
			payload.addCustomDictionary("time", DateTimeUtil.DATETIME_FORMAT.format(new Date()));
			PushNotificationManager pushManager = new PushNotificationManager();
			pushManager.initializeConnection(server);
			List<Device> deviceList = new ArrayList<Device>();
			for (String deviceId : deviceIds) {
				Device device = new BasicDevice();
				device.setToken(deviceId);
				deviceList.add(device);
			}
			PushedNotifications notifications = pushManager.sendNotifications(payload, deviceList);
			pushManager.stopConnection();
		} catch (CommunicationException e) {
			throw new RuntimeException(e);
		} catch (KeystoreException e) {
			throw new RuntimeException(e);
		}
	}
	
	static {
		try {
			server = new AppleNotificationServerBasicImpl(
					ConfigTool.getString(PushConfig.appleKeystore), 
					ConfigTool.getString(PushConfig.applePassword),
					ConfigTool.getBoolean(PushConfig.appleProduction, false));
		} catch (KeystoreException e) {
			throw new Error(e);
		}
	}
}
