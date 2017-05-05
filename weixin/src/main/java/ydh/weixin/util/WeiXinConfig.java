package ydh.weixin.util;

import ydh.utils.ConfigTool;

public class WeiXinConfig {
	private static final String AppID = "weixin.AppID";
	private static final String AppSecret = "weixin.AppSecret";
	/**
	 * 模板消息ID
	 */
	/**消息通知模板*/
	public final static String MESSAGE_NOTICE = "ngmL7-nM5JSykpX3Q3FYCtF9JH5vO2VnnDYDMYqhRBE";
	/**任务进展通知模板*/
	public final static String TASK_PROGRESS_NOTICE = "TB9TFhzqUCyWjD2GRQE9-1dg4OGyJM16Se4UsAfkTUg";
	/**支付成功通知模板*/
	public final static String PAY_SUCCESS_NOTICE = "hVQ6H7Qc0BkudsRzXD-SVqvRZAeNvxKViqYBJJHO1LA";
	public static String appId() {
		return ConfigTool.getString(AppID);
	}
	public static String appSecret() {
		return ConfigTool.getString(AppSecret);
	}
}
