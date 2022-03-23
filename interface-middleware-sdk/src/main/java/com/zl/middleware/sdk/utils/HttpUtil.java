package com.zl.middleware.sdk.utils;

import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 网络请求工具类
 *
 * @author 郑龙
 * @since 2021/6/10 16:18
 */
public class HttpUtil {
    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60L, TimeUnit.SECONDS)
            .callTimeout(60L, TimeUnit.SECONDS).build();

    private HttpUtil() {
    }

    /**
     * http請求方式
     */
    public enum HttpMethod {
        GET,
        POST
    }

    /**
     * 发起异步post请求
     *
     * @param fullUrl             请求的全路径
     * @param requestBody         请求体
     * @param authorizationHeader 认证头
     * @param callback            回调方法
     */
    public static void doPostAsync(String fullUrl, RequestBody requestBody, String authorizationHeader, Callback callback) {
        doAsync(fullUrl, requestBody, authorizationHeader, HttpMethod.POST, callback);
    }

    /**
     * 发起异步get请求
     *
     * @param fullUrl             请求的全路径
     * @param authorizationHeader 认证头
     * @param callback            回调方法
     */
    public static void doGetAsync(String fullUrl, String authorizationHeader, Callback callback) {
        doAsync(fullUrl, new FormBody.Builder().build(), authorizationHeader, HttpMethod.GET, callback);
    }

    /**
     * 发起同步post请求
     *
     * @param fullUrl             请求路径
     * @param requestBody         请求体
     * @param authorizationHeader 认证头
     * @return {@link Response response}
     * @throws IOException io异常
     */
    public static Response doPostSync(String fullUrl, RequestBody requestBody, String authorizationHeader) throws IOException {
        return doSync(fullUrl, requestBody, authorizationHeader, HttpMethod.POST);
    }

    /**
     * 发起同步GET请求
     *
     * @param fullUrl             完整路径
     * @param authorizationHeader 认证头
     * @return {@link Response response}
     * @throws IOException io异常
     */
    public static Response doGetSync(String fullUrl, String authorizationHeader) throws IOException {
        return doSync(fullUrl, new FormBody.Builder().build(), authorizationHeader, HttpMethod.GET);
    }

    /**
     * 发送同步请求
     *
     * @param fullUrl             URL
     * @param requestBody         请求体
     * @param authorizationHeader 认证头
     * @param method              POST、GET
     * @return {@link Response response}
     * @throws IOException io异常
     */
    private static Response doSync(String fullUrl, RequestBody requestBody, String authorizationHeader, HttpMethod method) throws IOException {
        isUrlLegal(fullUrl);

        //构造HTTP请求
        Request.Builder requestBuilder = new Request.Builder().url(fullUrl);
        switch (method) {
            case POST:
                requestBuilder.post(requestBody);
                break;
            case GET:
                requestBuilder.get();
                break;
            default:
                throw new IllegalArgumentException("目标HTTP请求方式不支持！");
        }

        if (StringUtils.isNotBlank(authorizationHeader)) {
            requestBuilder.addHeader("Authorization", authorizationHeader);
        }

        Request request = requestBuilder.build();
        return OK_HTTP_CLIENT.newCall(request).execute();
    }

    /**
     * 发起异步请求
     *
     * @param fullUrl             请求的全路径
     * @param requestBody         请求体
     * @param authorizationHeader 认证头
     * @param method              POST、GET
     * @param callback            回调方法
     */
    private static void doAsync(String fullUrl, RequestBody requestBody, String authorizationHeader, HttpMethod method, Callback callback) {
        isUrlLegal(fullUrl);

        //构造HTTP请求
        Request.Builder requestBuilder = new Request.Builder().url(fullUrl);
        switch (method) {
            case POST:
                requestBuilder.post(requestBody);
                break;
            case GET:
                requestBuilder.get();
                break;
            default:
                throw new IllegalArgumentException("目标HTTP请求方式不支持！");
        }

        if (StringUtils.isNotBlank(authorizationHeader)) {
            requestBuilder.addHeader("Authorization", authorizationHeader);
        }

        Request request = requestBuilder.build();
        OK_HTTP_CLIENT.newCall(request).enqueue(callback);
    }

    /**
     * 判断URL是否合法
     *
     * @param url url
     */
    private static void isUrlLegal(String url) {
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("请求URL不能为空！");
        }
    }
}
