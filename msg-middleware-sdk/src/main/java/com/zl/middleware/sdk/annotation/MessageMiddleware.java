package com.zl.middleware.sdk.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 消息中间件注解，用于类上，标注是对接那个系统的哪个接口
 *
 * @author zl
 * @since 2021/11/19 9:34
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageMiddleware {
    /**
     * 对应的接口key名称
     *
     * @return 接口key名称
     */
    String[] interfaceKeys();

    /**
     * 对应的系统key名称
     *
     * @return 系统key名称
     */
    String[] systemKeys() default {};
}
