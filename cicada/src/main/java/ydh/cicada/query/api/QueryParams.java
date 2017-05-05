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
 * 通常用于查询模式匹配字段，根据包含的QueryParam注解中的on值进行匹配
 * 字段传入值与on配置值相等时，采用该注解的配置
 * on配置为空代表默认配置，默认配置必须是最后一个配置，否则其后的配置将不生效
 * 如果传入值为null，则该配置无效
 * @version 1.0
 * @author lizx
 * @created  2013-3-25
 */
@Target(value={ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface QueryParams {
    /**
     * 包含的QueryParam配置
     */
    public QueryParam[] value();
    /**
     * 如果embed=true,该配置为嵌入式配置，仅用于其他字段stmt子句中作为嵌入语句使用
     */
    public boolean embed() default false;
}
