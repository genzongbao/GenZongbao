/*
 * <p> Title:  Id.java</p>
 * <p>Copyright:   ChaoChuang (c) 2015</p>
 * <p>Company: 南宁超创信息工程有限公司</p>
 */
package ydh.cicada.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> Calss Name:  Id.java</p>
 * <p> Description:   </p>
 * @version 1.0
 * @author lizx
 * @created  2015年1月14日
 */
@Target(value={ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Id {
	boolean autoincrement() default false;
	String sequence() default "";
}
