package com.zl.middleware.core.config;

import lombok.Getter;
import org.springframework.http.HttpMethod;

import java.util.Map;

/**
 * 系统常量定义
 *
 * @author zl
 * @since 2021/11/18 10:17
 */
public class SystemConstant {
    /**
     * 字段类型
     */
    public enum FieldType {
        INTEGER(Integer.class),

        DOUBLE(Double.class),

        STRING(String.class),

        LONG(Long.class),

        BOOLEAN(Boolean.class),

        MAP(Map.class),

        OTHER(Object.class);

        @Getter
        final Class<?> clazz;

        FieldType(Class<?> clazz) {
            this.clazz = clazz;
        }
    }

    /**
     * 渲染策略
     */
    public enum RenderStrategy {
        /**
         * 所有需要的字段值都渲染，必须字段没有value的会抛出异常
         */
        ALL,

        /**
         * 只渲染必须字段，没有value的抛出异常<br>
         * 非必须的字段不渲染，有值也不渲染
         */
        IGNORE_NO_ESSENTIAL
    }

    /**
     * HTTP请求类型
     */
    public enum HttpMethodType {
        /**
         * get
         */
        GET(HttpMethod.GET),

        /**
         * post
         */
        POST(HttpMethod.POST),

        /**
         * put
         */
        PUT(HttpMethod.PUT),

        /**
         * delete
         */
        DELETE(HttpMethod.DELETE),

        /**
         * 询问支持的方法
         */
        OPTIONS(HttpMethod.OPTIONS),

        /**
         * 获取报文首部
         */
        HEAD(HttpMethod.HEAD),

        /**
         * 追踪路径
         */
        TRACE(HttpMethod.TRACE),

        /**
         * 用于资源的部分内容的更新
         */
        PATCH(HttpMethod.PATCH);

        @Getter
        final HttpMethod httpMethod;

        HttpMethodType(HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
        }
    }
}
