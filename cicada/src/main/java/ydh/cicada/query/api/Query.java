/*
 * Copyright: SBT (c) 2015
 * Company: 成都盛比特信息技术有限公司
 */
package ydh.cicada.query.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> Calss Name:  Query.java</p>
 * <p> Description:   </p>
 * @version 1.0
 * @author lizx
 * @created  2015年1月15日
 */
@Target(value={ElementType.TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Query {
	
	/**
     * select子句
     */
    String select() default "*";
    
    /**
     * 是否distinct
     */
    boolean distince() default false;
    /**
     * from子句
     */
    String from();
    /**
     * 默认where子句，与字段上的QueryParam配置一起形成最终的where子句
     * where子句中可以使用{x}占位符，x为数字，对应consts中的常量
     */
    String where() default "";
    /**
     * group by 子句
     */
    String groupBy() default "";
    /**
     * having 子句
     */
    String having() default "";
    /**
     * order by 子句
     */
    String orderBy() default "";
    /**
     * 默认where子句中用到的常量
     */
    String[] consts() default {};
    
    /**
     * 查询条件的连接方式，true使用or进行连接，false使用and进行连接
     */
    boolean or() default false;
    
    /**
     * 锁定查询结果
     * @return
     */
    boolean forUpdate() default false;
    
}
