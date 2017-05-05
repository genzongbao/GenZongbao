package ydh.utils;

import java.util.Random;

public class Validcode {
	public final String validcode;
	public final String validPhone;
	public final long expiredTime;
	
	
	private Validcode(int length, long timeout,String validPhone) {
		char[] codes = new char[length];
		Random rd=new Random();
		for (int i = 0; i < codes.length; i++) {
			codes[i] = (char)('0' + rd.nextInt(31) % 10);
		}
		this.validcode = new String(codes);
		this.expiredTime = System.currentTimeMillis() + timeout;
		this.validPhone = validPhone;
	}
	
	public static Validcode generate(int length, long timeout,String validPhone) {
		return new Validcode(length, timeout,validPhone);
	}
}
