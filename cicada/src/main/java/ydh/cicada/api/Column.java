/**
 * Cicada 1.0
 * 
 * @author Lizongxu Chengdu SBT Corp.
 * @author 李宗旭 成都盛比特信息技术有限公司
 * 
 * 
 */
package ydh.cicada.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> Calss Name:  Field.java</p>
 * <p> Description:   </p>
 * @version 1.0
 * @author lizx
 * @created  2015年1月14日
 */
@Target(value={ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Column {
	String name();
	int length() default 0;
	boolean nullable() default true;
	boolean unique() default false;
	int precision() default 0;
	int scale() default 0;
	String validate() default "";
}
