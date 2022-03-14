package com.zl.middleware.sdk.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解于字段上，用于表示该字段和接口所需字段的映射关系
 *
 * @author zl
 * @since 2021/11/19 9:38
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Alias {
    /**
     * 可以映射多个和接口中所需字段对应的名称
     *
     * @return 和接口中所需字段对应的名称
     */
    String[] value();
}
