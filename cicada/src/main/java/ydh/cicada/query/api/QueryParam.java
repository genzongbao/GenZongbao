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
 * 查询参数配置
 * @author lizx
 */
@Target(value={ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface QueryParam {
    /**
     * 当前查询参数对应的字段名，如果在QueryInfo中配置有表别名应加上别名前缀
     * 如果未设置本参数，则字段名与当前属性名相同
     */
    public String fieldName() default "";
    
    /**
     * 配置字段对应的操作符，默认为相等操作
     */
    public QueryOperator op() default QueryOperator.EQU;
    /**
     * 语句选择器， 与QueryParams配合使用
     * @see ydh.cicada.query.api.QueryParams
     */
    public String on() default "";
    /**
     * 查询语句，用于较复杂的查询，本参数的配置将直接翻译成hql的一部分
     * 在stmt配置中可以包含{xxx}占位符，
     * 如果xxx为数字，则该占位符将由consts中配置的常量代替
     * 如果xxx不是数字，则该占位符将由xxx所指向的字段的查询语句代替，通过这种方式可实现查询条件的多层次嵌套
     */
    public String stmt() default "";
    /**
     * 查询语句中使用到的常量，与stmt语句配合使用
     */
    public String[] consts() default {};
    /**
     * 是否嵌入语句，如果embed=true，改配置为嵌入语句，只能作为其他字段stmt语句中的嵌入语句使用，而不会作为普通查询语句解析
     */
    public boolean embed() default false;
}

