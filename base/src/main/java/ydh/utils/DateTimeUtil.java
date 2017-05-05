/**
 * 
 */
package ydh.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author 刘晓曦
 * 
 */
public class DateTimeUtil {

	static final Log logger = LogFactory.getLog(DateTimeUtil.class);

	public static final SimpleDateFormat DATETIME_FORMAT  = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 格式化日期/时间
	 * @param format 日期格式
	 * @param date   日期
	 * @return
	 */
	public static String format(Date date, String format) {
		SimpleDateFormat simple = new SimpleDateFormat(format);
		return simple.format(date);		
	}

	/** 
     * 得到几天后的时间 
     * @param d 
     * @param day 
     * @return 
     */  
    public static Date getDateAfter(Date d, int day) {  
        Calendar now = Calendar.getInstance();  
        now.setTime(d);  
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);  
        return now.getTime();  
    }
    
	/**
	 * 字符转时间
	 * 
	 * @param dateStr
	 * @param formatStr
	 * @return 转换结果
	 * @throws ParseException 日期格式解析错误
	 */
	public static Date parse(String dateStr, String formatStr) throws ParseException {
		DateFormat format = new SimpleDateFormat(formatStr);
		return format.parse(dateStr);
	}


	/**
	 * 两个日期之间相差
	 * 计算相差时以各自单位为准，例如2014年12月31日与2015年1月1日之间相差的年数是1
	 * @param fromDate 开始日期
	 * @param toDate   结束日期
	 * @param unit     单位:Calendar.YEAR,MONTH,DATE,HOUR,MINUTE,SECOND,MILLISECOND
	 * @return 根据单位计算得到相差的数量，如果开始日期晚于结束日期返回负数
	 */
	public static int between(Calendar from, Calendar to, int unit) {
		int fromInt, toInt;
		switch(unit) {
		case Calendar.YEAR:
			return to.get(Calendar.YEAR) - from.get(Calendar.YEAR); 
		case Calendar.MONTH:
			int year = to.get(Calendar.YEAR) - from.get(Calendar.YEAR);
			int month = to.get(Calendar.MONTH) - from.get(Calendar.MONTH);
			return year * 12 + month;
		case Calendar.DATE:
			fromInt = (int)(from.getTimeInMillis() / (24 * 60 * 60 * 1000L));
			toInt = (int)(to.getTimeInMillis() / (24 * 60 * 60 * 1000L));
			return toInt - fromInt;
		case Calendar.HOUR:
		case Calendar.HOUR_OF_DAY:
			fromInt = (int)(from.getTimeInMillis() / (60 * 60 * 1000L));
			toInt = (int)(to.getTimeInMillis() / (60 * 60 * 1000L));
			return toInt - fromInt;			
		case Calendar.MINUTE:
			fromInt = (int)(from.getTimeInMillis() / (60 * 1000L));
			toInt = (int)(to.getTimeInMillis() / (60 * 1000L));
			return toInt - fromInt;
		case Calendar.SECOND:
			fromInt = (int)(from.getTimeInMillis() / (60 * 1000L));
			toInt = (int)(to.getTimeInMillis() / (60 * 1000L));
			return toInt - fromInt;
		case Calendar.MILLISECOND:
			return (int)(to.getTimeInMillis() - from.getTimeInMillis());
		default:
			throw new IllegalArgumentException("Unknown Date Unit: "+ unit);
		}
	}
	
	/**
	 * 两个日期之间相差
	 * 计算相差时以各自单位为准，例如2014年12月31日与2015年1月1日之间相差的年数是1
	 * @param fromDate 开始日期
	 * @param toDate   结束日期
	 * @param unit     单位:Calendar.YEAR,MONTH,DATE,HOUR,MINUTE,SECOND,MILLISECOND
	 * @return 根据单位计算得到相差的数量，如果开始日期晚于结束日期返回负数
	 */
	public static int between(Date fromDate, Date toDate, int unit) {
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.setTime(fromDate);
		to.setTime(toDate);
		return between(from, to, unit);
	}

	/**
	 * 两个日期之间相差
	 * 计算相差时以各自单位为准，例如2014年12月31日与2015年1月1日之间相差的年数是1
	 * @param from     开始日期
	 * @param toDate   结束日期
	 * @param unit     单位:Calendar.YEAR,MONTH,DATE,HOUR,MINUTE,SECOND,MILLISECOND
	 * @return 根据单位计算得到相差的数量，如果开始日期晚于结束日期返回负数
	 */
	public static int between(Calendar from, Date toDate, int unit) {
		Calendar to = Calendar.getInstance();
		to.setTime(toDate);
		return between(from, to, unit);
	}
	
	/**
	 * 两个日期之间相差
	 * 计算相差时以各自单位为准，例如2014年12月31日与2015年1月1日之间相差的年数是1
	 * @param fromDate 开始日期
	 * @param to       结束日期
	 * @param unit     单位:Calendar.YEAR,MONTH,DATE,HOUR,MINUTE,SECOND,MILLISECOND
	 * @return 根据单位计算得到相差的数量，如果开始日期晚于结束日期返回负数
	 */
	public static int between(Date fromDate, Calendar to, int unit) {
		Calendar from = Calendar.getInstance();
		from.setTime(fromDate);
		return between(from, to, unit);
	}
	
	/**
	 * 转换Date为Calendar
	 */
	public static Calendar calendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * 将calendar的时间部分置零
	 */
	public static void truncDate(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);		
	}
	
	/**
	 * 获得当天0:00:00的日期对象
	 */
	public static Calendar today() {
		Calendar calendar = Calendar.getInstance();
		truncDate(calendar);
		return calendar;
	}
	
	/**
	 * 取本周某日的日期
	 * @param dayOfWeek 周日至周六，使用Calendar.SUNDAY~SATURDAY常量
	 */
	public static Calendar thisWeek(int dayOfWeek) {
		Calendar calendar = Calendar.getInstance();
		int todayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.DATE, dayOfWeek - todayOfWeek);
		return calendar;
	}
	/**
	 * 日期转换成字符串
	 * 
	 * @param date
	 * @return str
	 */
	public static String formatDate(Date date) {
		return DATE_FORMAT.format(date);
	}
	
	/**
	 * 获得本月1日0:00:00的日期对象
	 * @return
	 */
	public static Calendar thisMonthFirst() {
		return thisMonth(1);
	}
	
	/**
	 * 获得本月1日0:00:00的日期对象
	 * @return
	 */
	public static Calendar thisMonthLast() {
		Calendar calendar = thisMonth(1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar;
	}
	
	/**
	 * 获得本月指定日的日期对象
	 * @param day  日(1~31)
	 * @return
	 */
	public static Calendar thisMonth(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, day);
		truncDate(calendar);
		return calendar;
	}
	
	/**
	 * 获取当季的第一天
	 */
	public static Date thisQuarterFirst() {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		month = month - month % 3;
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	/**
	 * 获取当季的最后一天
	 * 
	 * @param year
	 * @return
	 */
	public static Date thisQuarterLast() {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		month = month - month % 3 + 2;
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	/**
	 * 获取当前半年的第一天
	 * 
	 * @param year
	 * @return
	 */
	public static Date thisHalfYearFirst() {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		month = month - month % 6;
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	/**
	 * 获取当前半年的最后一天
	 * 
	 * @param year
	 * @return
	 */
	public static Date thisHalfYearLast() {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		if (month < 6) {
			calendar.set(Calendar.MONTH, 5);
			calendar.set(Calendar.DAY_OF_MONTH, 30);
		} else {
			calendar.set(Calendar.MONTH, 11);
			calendar.set(Calendar.DAY_OF_MONTH, 31);
		}
		return calendar.getTime();
	}
	
	/**
	 * 获取当年的第一天
	 * 
	 * @param year
	 * @return
	 */
	public static Date thisYearFirst() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	/**
	 * 获取当年的最后一天
	 * 
	 * @param year
	 * @return
	 */
	public static Date thisYearLast() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		return calendar.getTime();
	}

	/**
	 * 获取本年指定日期
	 * @param month  月（0～11）
	 * @param day    日（1～31）
	 */
	public static Calendar thisYear(int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		truncDate(calendar);
		return calendar;
	}
	
	private static final long ONE_MINUTE = 60;
	private static final long ONE_HOUR = 3600;
	private static final long ONE_DAY = 86400;
	private static final long ONE_MONTH = 2592000;
	private static final long ONE_YEAR = 31104000;
	
	/**
	 * 距离今天多久
	 * @param date
	 * @return
	 */
	public static String fromToday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		long time = date.getTime() / 1000;
		long now = new Date().getTime() / 1000;
		long ago = now - time;
		if (ago <= ONE_HOUR) {
			return ago / ONE_MINUTE + "分钟前";
		} else if (ago <= ONE_DAY) {
			return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE)
					+ "分钟前";
		} else if (ago <= ONE_DAY * 2) {
			return "昨天" + calendar.get(Calendar.HOUR_OF_DAY) + "点"
					+ calendar.get(Calendar.MINUTE) + "分";
		} else if (ago <= ONE_DAY * 3) {
			return "前天" + calendar.get(Calendar.HOUR_OF_DAY) + "点"
					+ calendar.get(Calendar.MINUTE) + "分";
		} else if (ago <= ONE_MONTH) {
			long day = ago / ONE_DAY;
			return day + "天前" + calendar.get(Calendar.HOUR_OF_DAY) + "点"
					+ calendar.get(Calendar.MINUTE) + "分";
		} else if (ago <= ONE_YEAR) {
			long month = ago / ONE_MONTH;
			long day = ago % ONE_MONTH / ONE_DAY;
			return month + "个月" + day + "天前"
					+ calendar.get(Calendar.HOUR_OF_DAY) + "点"
					+ calendar.get(Calendar.MINUTE) + "分";
		} else {
			long year = ago / ONE_YEAR;
			int month = calendar.get(Calendar.MONTH) + 1;// JANUARY which is 0
			// so month+1
			return year + "年" + month + "月" + calendar.get(Calendar.DATE)
					+ "日前";
		}

	}
	
	/**
	 * 距离截止日期还有多长时间
	 * 
	 * @param date
	 * @return
	 */
	public static String fromDeadline(Date date) {
		long deadline = date.getTime() / 1000;
		long now = (new Date().getTime()) / 1000;
		long remain = deadline - now;
		if (remain <= ONE_HOUR)
			return remain / ONE_MINUTE + "分钟";
		else if (remain <= ONE_DAY)
			return  remain / ONE_HOUR + "小时"
					+ (remain % ONE_HOUR / ONE_MINUTE) + "分钟";
		else {
			long day = remain / ONE_DAY;
			long hour = remain % ONE_DAY / ONE_HOUR;
			long minute = remain % ONE_DAY % ONE_HOUR / ONE_MINUTE;
			return  day + "天" + hour + "小时" + minute + "分钟";
		}

	}
	
	/**
	 * 距离今天的绝对时间
	 * 
	 * @param date
	 * @return
	 */
	public static String toToday(Date date) {
		long time = date.getTime() / 1000;
		long now = (new Date().getTime()) / 1000;
		long ago = now - time;
		if (ago <= ONE_HOUR)
			return ago / ONE_MINUTE + "分钟";
		else if (ago <= ONE_DAY)
			return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE) + "分钟";
		else if (ago <= ONE_DAY * 2)
			return "昨天" + (ago - ONE_DAY) / ONE_HOUR + "点" + (ago - ONE_DAY)
					% ONE_HOUR / ONE_MINUTE + "分";
		else if (ago <= ONE_DAY * 3) {
			long hour = ago - ONE_DAY * 2;
			return "前天" + hour / ONE_HOUR + "点" + hour % ONE_HOUR / ONE_MINUTE
					+ "分";
		} else if (ago <= ONE_MONTH) {
			long day = ago / ONE_DAY;
			long hour = ago % ONE_DAY / ONE_HOUR;
			long minute = ago % ONE_DAY % ONE_HOUR / ONE_MINUTE;
			return day + "天前" + hour + "点" + minute + "分";
		} else if (ago <= ONE_YEAR) {
			long month = ago / ONE_MONTH;
			long day = ago % ONE_MONTH / ONE_DAY;
			long hour = ago % ONE_MONTH % ONE_DAY / ONE_HOUR;
			long minute = ago % ONE_MONTH % ONE_DAY % ONE_HOUR / ONE_MINUTE;
			return month + "个月" + day + "天" + hour + "点" + minute + "分前";
		} else {
			long year = ago / ONE_YEAR;
			long month = ago % ONE_YEAR / ONE_MONTH;
			long day = ago % ONE_YEAR % ONE_MONTH / ONE_DAY;
			return year + "年前" + month + "月" + day + "天";
		}
	}
	/**
	 * yyyy-MM-dd 后获取距离截止日期还有多天
	 * 
	 * @param date
	 * @return
	 */
	public static int fromDead(Date date) {
		long deadline = date.getTime();
		Date nowDate = DateTimeUtil.today().getTime();
		long now = nowDate.getTime();
		long remain = deadline - now;
		return (int) (remain / (24 * 60 * 60 * 1000));
	}

}
