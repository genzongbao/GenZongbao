package ydh.utils;

import java.math.BigDecimal;

public class MoneyTool {
	public static final BigDecimal toMoney(double value) {
		return BigDecimal.valueOf((long)Math.round(value * 100.0), 2);
	}
	
	public static final double round(double value) {
		return Math.round(value * 100.0) / 100.0;
	}
}
