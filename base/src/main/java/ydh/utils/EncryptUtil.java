package ydh.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class EncryptUtil {
	public final static String md5(String s) {
		final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * RSA加密
	 * 
	 * @param data
	 *            加密数据
	 * @param key
	 *            密钥
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws UnsupportedEncodingException 
	 * @throws Exception
	 */
	public static String encrypt(String content, RSAKey key)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, 
			UnsupportedEncodingException {
		byte[] data = content.getBytes("UTF-8");
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, (Key) key);
		// 模长
		int keyLen = key.getModulus().bitLength() / 8;
		byte[][] result = new byte[(data.length + 1) / keyLen][];
		int len = 0;
		for (int i = 0; i < result.length; i++) {
			result[i] = cipher.doFinal(data, i, Math.min(keyLen, data.length - i * keyLen));
			len += result[i].length;
		}
		byte[] encrypted = new byte[len];
		int pos = 0;
		for (int i = 0; i < result.length; i++) {
			System.arraycopy(result[i], 0, encrypted, pos, result[i].length);
			pos+=result[i].length;
		}
		return Base64.encodeBase64String(encrypted);
	}

	/**
	 * RSA解密
	 * 
	 * @param content   密文（BASE64编码)  
	 * @param key       密钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(String content, RSAKey key)
			throws Exception {
		byte[] data = Base64.decodeBase64(content);
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, (Key)key);
		// 模长
		int keyLen = key.getModulus().bitLength() / 8;
		byte[][] result = new byte[(data.length + 1) / keyLen][];
		int len = 0;
		for (int i = 0; i < result.length; i++) {
			result[i] = cipher.doFinal(data, i, Math.min(keyLen, data.length - i * keyLen));
			len += result[i].length;
		}
		byte[] decrypted = new byte[len];
		int pos = 0;
		for (int i = 0; i < result.length; i++) {
			System.arraycopy(result[i], 0, decrypted, pos, result[i].length);
			pos+=result[i].length;
		}
		return decrypted;
	}

	/**
	 * 对称加密
	 * 
	 * @param content 需要加密的内容
	 * @param key     加密密钥
	 * @return 加密结果
	 * @throws NoSuchAlgorithmException 
	 * @throws NoSuchPaddingException 
	 * @throws UnsupportedEncodingException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static String encrypt(String content, byte[] key, String algorithm) throws NoSuchAlgorithmException, 
			NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, 
			IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec keySpec = new SecretKeySpec(key, algorithm); 
		Cipher cipher = Cipher.getInstance(algorithm);// 创建密码器
		byte[] data = content.getBytes("UTF-8");
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);// 初始化
		byte[] result = cipher.doFinal(data);
		return Base64.encodeBase64String(result);
	}
	
	/**
	 * AES解密
	 * 
	 * @param content 需要解密的内容(BASE64编码)
	 * @param key     密钥
	 * @return 加密结果
	 * @throws NoSuchAlgorithmException 
	 * @throws NoSuchPaddingException 
	 * @throws UnsupportedEncodingException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws Base64DecodingException 
	 */
	public static String decrypt(String content, byte[] key, String algorithm) throws NoSuchAlgorithmException, 
			NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, 
			IllegalBlockSizeException, BadPaddingException {
		byte[] data = Base64.decodeBase64(content);
		SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
		Cipher cipher = Cipher.getInstance(algorithm);// 创建密码器
		cipher.init(Cipher.DECRYPT_MODE, keySpec);// 初始化
		byte[] result = cipher.doFinal(data);
		return new String(result, "UTF-8");
	}

	/**
	 * BCD转字符串
	 */
	public static String bcd2Str(byte[] bytes) {
		char temp[] = new char[bytes.length * 2], val;
		for (int i = 0; i < bytes.length; i++) {
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}

	/**
	 * ASCII码转BCD码
	 * 
	 */
	public static byte[] ascii2bcd(byte[] ascii, int asc_len) {
		byte[] bcd = new byte[asc_len / 2];
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / 2; i++) {
			bcd[i] = ascii2bcd(ascii[j++]);
			bcd[i] = (byte) (((j >= asc_len) ? 0x00 : ascii2bcd(ascii[j++])) + (bcd[i] << 4));
		}
		return bcd;
	}

	public static byte ascii2bcd(byte asc) {
		byte bcd;
		if ((asc >= '0') && (asc <= '9')) {
			bcd = (byte) (asc - '0');
		} else if ((asc >= 'A') && (asc <= 'F')) {
			bcd = (byte) (asc - 'A' + 10);
		} else if ((asc >= 'a') && (asc <= 'f')) {
			bcd = (byte) (asc - 'a' + 10);
		} else {
			bcd = (byte) (asc - 48);
		}
		return bcd;
	}
}
