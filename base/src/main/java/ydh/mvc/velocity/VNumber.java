package ydh.mvc.velocity;

import java.text.NumberFormat;

import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.config.DefaultKey;
import org.apache.velocity.tools.config.ValidScope;

@DefaultKey("vnumber")
@ValidScope(Scope.APPLICATION)
public class VNumber {
	
	public static NumberFormat format;
	
	/**
	 * 默认货币格式
	 * @param d
	 * @param i 精确度
	 * @return
	 */
	public static final String numberInstance(double d,int i) {
		format = NumberFormat.getNumberInstance();
		format.setMinimumFractionDigits(i);
		return format.format(d);
	}
	
	/**
	 * 默认货币格式
	 * @param d
	 * @return
	 */
	public static final String numberInstance(double d) {
		format = NumberFormat.getNumberInstance();
		return format.format(d);
	}
	
	/**
	 * 当前地区货币格式
	 * @param d
	 * @param i 精确度
	 * @return
	 */
	public static final String currencyInstance(double d,int i) {
		format = NumberFormat.getCurrencyInstance();
		format.setMinimumFractionDigits(i);
		return format.format(d);
	}
	
	/**
	 * 当前地区货币格式
	 * @param d
	 * @return
	 */
	public static final String currencyInstance(double d) {
		format = NumberFormat.getCurrencyInstance();
		return format.format(d);
	}
	
	/**
	 * 百分比加货币格式
	 * @param d
	 * @param i 精确度
	 * @return
	 */
	public static final String percentInstance(double d,int i) {
		format = NumberFormat.getPercentInstance();
		format.setMinimumFractionDigits(i);
		return format.format(d);
	}
	
	/**
	 * 百分比加货币格式,默认
	 * @param d
	 * @return
	 */
	public static final String percentInstance(double d) {
		format = NumberFormat.getPercentInstance();
		return format.format(d);
	}
	
}
