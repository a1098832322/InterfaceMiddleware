package com.zl.middleware.sdk.def;

/**
 * 渲染策略
 *
 * @author zl
 * @since 2021/11/22 15:48
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
