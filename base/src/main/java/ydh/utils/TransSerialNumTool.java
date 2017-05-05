package ydh.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransSerialNumTool {
	
	private static String timeTicket; 
	private static int currNum;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	/**
	 * 生成流水号
	 * @return
	 */
	public synchronized static String createSerialNum(){
		String ticket = sdf.format(new Date());
		if (! ticket.equals(timeTicket)) {
			timeTicket = ticket;
			currNum = 0;
		}
		currNum++;
		return String.format("%s%04d", timeTicket, currNum);
	}
}
