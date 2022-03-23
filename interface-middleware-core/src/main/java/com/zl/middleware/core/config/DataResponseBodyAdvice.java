package com.zl.middleware.core.config;

import com.zl.middleware.core.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;
import java.util.Objects;

/**
 * 统一格式返回数据
 *
 * @author 郑龙
 * @since 2020/12/25 10:07
 */
@Slf4j
@ControllerAdvice
public class DataResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    /**
     * 拦截的请求包名
     */
    private static final String CURRENT_PACKAGE_NAME = "com.zl.middleware.core.controller";

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    /**
     * 对所有接口的返回参数进行统一标准格式封装
     *
     * @param body                  返回数据body
     * @param returnType            MethodParameter
     * @param selectedContentType   {@link MediaType#APPLICATION_JSON}
     * @param selectedConverterType selectedConverterType
     * @param request               ServerHttpRequest
     * @param response              ServerHttpResponse
     * @return 封装的统一格式的json
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //避免第三方系统参数被格式化 eg.swagger
        String packageName = Objects.requireNonNull(returnType.getMethod()).getDeclaringClass().getPackage().getName();
        if (!packageName.startsWith(CURRENT_PACKAGE_NAME)) {
            return body;
        }

        if (body instanceof Result) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return body;
        } else if (body instanceof byte[]) {
            return body;
        } else {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return Result.createSuccessResult(Result.DEFAULT_SUCCESS_MESSAGE, body);
        }

    }

    /**
     * 异常统一处理
     *
     * @param e 异常
     * @return 返回异常信息
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Object exceptionHandle(Exception e) {
        //log中记录异常详情
        log.error("异常拦截器检测到异常产生：", e);
        return Result.createErrorResult(Result.ERROR_CODE
                , e.getMessage()
                , null);
    }

    /**
     * 获取参数验证异常信息
     *
     * @param bindException 参数验证异常
     * @return error message
     */
    private String getErrorMessage(BindException bindException) {
        StringBuilder stringBuilder = new StringBuilder();
        List<FieldError> fieldErrors = bindException.getBindingResult().getFieldErrors();

        for (FieldError error : fieldErrors) {
            stringBuilder.append(error.getField());
            stringBuilder.append(":");
            stringBuilder.append(error.getDefaultMessage());
            stringBuilder.append(";");
        }
        return stringBuilder.toString();
    }
}
