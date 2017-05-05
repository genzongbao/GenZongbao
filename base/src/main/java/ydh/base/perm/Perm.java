package ydh.base.perm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Perm {
	/**
	 * 系统类型代码
	 */
	String type();

	/**
	 * 系统名称 
	 */
	String name();
}
