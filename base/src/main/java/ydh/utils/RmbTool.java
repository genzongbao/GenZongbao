package ydh.utils;

import java.math.BigDecimal;

public class RmbTool {
	public static final BigDecimal ZERO = BigDecimal.valueOf(0, 2);
	
	public static BigDecimal toMoney(Double value) {
		if (value == null) return ZERO;
		return BigDecimal.valueOf(Math.round(value * 100), 2);
	}
}
