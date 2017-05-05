package ydh.message.push;


/**
 * 提取用户推送地址接口
 * @author lizx
 */
public interface PushAddressTranslator<U, T extends MessageType<U>> {
	
	public U loadUser(String userId);
	/**
	 * 提取用户短信地址
	 */
	public String[] addressForSms(U user);
	/**
	 * 提取用户微信地址
	 */
	public String[] addressForWx(U user);
	/**
	 * 提取用户email地址
	 */
	public String[] addressForEmail(U user);
	
	/**
	 * 查找用户姓名
	 * @param userId
	 * @return
	 */
	public String nameOfUser(U user);
	
	/**
	 * 提取用户Android设备地址
	 */
	public String[] addressForAndroid(U user);
	
	/**
	 * 提取所有用户的IOS设备地址
	 */
	public String[] addressForIos();
	
	/**
	 * 提取指定用户的IOS设备地址
	 */
	public String[] addressForIos(U user);
	
	/**
	 * 提取用户推送配置信息，如果返回空表示使用默认配置
	 * @param userId
	 * @param type
	 * @return
	 */
	public PushSetting loadPushSetting(String userId, T type);
	
}