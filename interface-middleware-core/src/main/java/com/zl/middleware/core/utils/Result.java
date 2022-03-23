package com.zl.middleware.core.utils;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * 返回结果模板类型
 *
 * @author 郑龙
 * @date 2020/12/25 10:06
 */
@Data
public class Result<T> {
    /**
     * 默认成功状态码
     */
    public static final int SUCCESS_CODE = HttpStatus.OK.value();

    /**
     * 默认失败状态码
     */
    public static final int ERROR_CODE = HttpStatus.INTERNAL_SERVER_ERROR.value();

    /**
     * 权限认证失败状态码
     */
    public static final int AUTHENTICATION_FAILED_CODE = HttpStatus.UNAUTHORIZED.value();

    /**
     * 默认响应成功消息
     */
    public static final String DEFAULT_SUCCESS_MESSAGE = "Success";

    /**
     * 默认响应成功消息
     */
    public static final String DEFAULT_ERROR_MESSAGE = "Error";

    /**
     * 返回信息码，详见各接口定义
     */
    private int code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回的数据体
     */
    private T data;

    /**
     * 私有化构造方法
     *
     * @param code    状态码
     * @param message 消息体
     * @param data    数据体
     */
    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 构造一个空的成功消息体
     *
     * @return 包装结果
     */
    public static Result<Object> createEmptySuccessResult() {
        return createSuccessResult(DEFAULT_SUCCESS_MESSAGE, null);
    }

    /**
     * 构造一个空的错误消息体
     *
     * @return 包装结果
     */
    public static Result<Object> createEmptyErrorResult() {
        return createErrorResult(DEFAULT_ERROR_MESSAGE, null);
    }

    /**
     * 构造一个成功消息体
     *
     * @param successMessage 成功消息
     * @param data           数据结果
     * @param <T>            参数类型
     * @return 包装结果
     */
    public static <T> Result<T> createSuccessResult(String successMessage, T data) {
        return createSuccessResult(SUCCESS_CODE, successMessage, data);
    }

    /**
     * 构造一个成功消息体
     *
     * @param code           状态码
     * @param successMessage 成功消息
     * @param data           数据结果
     * @param <T>            参数类型
     * @return 包装结果
     */
    public static <T> Result<T> createSuccessResult(int code, String successMessage, T data) {
        return new Result<>(code, successMessage, data);
    }


    /**
     * 构造一个错误消息体
     *
     * @param errorMessage 错误消息
     * @param data         数据结果
     * @param <T>          参数类型
     * @return 包装结果
     */
    public static <T> Result<T> createErrorResult(String errorMessage, T data) {
        return createErrorResult(ERROR_CODE, errorMessage, data);
    }

    /**
     * 构造一个错误消息体
     *
     * @param code         状态码
     * @param errorMessage 错误消息
     * @param data         数据结果
     * @param <T>          参数类型
     * @return 包装结果
     */
    public static <T> Result<T> createErrorResult(int code, String errorMessage, T data) {
        return new Result<>(code, errorMessage, data);
    }
}
