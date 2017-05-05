package ydh.captcha.verification;

/**
 * GeetestWeb配置文件
 * 
 *
 */
public class GeetestConfig {

	// 填入自己的captcha_id和private_key
	private static final String geetest_id = "616bbaadf42b1a611ab3048fdbfc51b3";
	private static final String geetest_key = "e01b38d6260cb52b8f7bd2ada52cbf2f";

	public static final String getGeetest_id() {
		return geetest_id;
	}

	public static final String getGeetest_key() {
		return geetest_key;
	}

}
