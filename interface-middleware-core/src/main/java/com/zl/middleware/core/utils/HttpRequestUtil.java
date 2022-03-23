package com.zl.middleware.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

/**
 * 网络请求工具
 *
 * @author 郑龙
 * @since 2021/7/28 15:57
 */
public class HttpRequestUtil {

    private final RestTemplate restTemplate;

    public HttpRequestUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 发送复杂表单请求
     *
     * @param url     url
     * @param params  请求参数
     * @param headers 自定义的请求头<br>
     *                注意：手动设置<pre>Content-type会被覆盖掉。</pre>
     * @param method  http请求方法
     * @return 请求结果
     */
    public String doExchange(String url, Map<String, Object> params, HttpHeaders headers, HttpMethod method) {
        return doExchange(url, params, null, headers, method, false);
    }

    /**
     * 发送复杂json请求
     *
     * @param url     url
     * @param json    请求json
     * @param headers 自定义的请求头<br>
     *                注意：手动设置<pre>Content-type会被覆盖掉。</pre>
     * @param method  http请求方法
     * @return 请求结果
     */
    public String doExchange(String url, String json, HttpHeaders headers, HttpMethod method) {
        return doExchange(url, null, json, headers, method, true);
    }

    /**
     * 发送复杂请求
     *
     * @param url        url
     * @param params     请求参数
     * @param json       发送的json数据
     * @param headers    自定义的请求头<br>
     *                   注意：手动设置<pre>Content-type会被覆盖掉。</pre>
     * @param method     http请求方法，目前该方法中仅资瓷{@link HttpMethod#POST POST}和{@link HttpMethod#DELETE DELETE}两种方法<br>
     *                   也资瓷了{@link HttpMethod#PUT PUT}方法 —— 2021年11月10日
     * @param isSendJson 是否是发送json<br>
     *                   <ul>
     *                   <li>true: 发送json数据</li>
     *                   <li>false: 发送普通表单数据</li>
     *                   </ul>
     * @return 请求结果
     */
    private String doExchange(String url, Map<String, Object> params, String json, HttpHeaders headers, HttpMethod method, boolean isSendJson) {
        if (StringUtils.isNotBlank(url)) {
            MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();

            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (entry.getValue() instanceof String[]) {
                        //针对数组型对象进行特殊处理
                        requestParams.add(entry.getKey(), Optional.of(entry.getValue()).map(o -> (String[]) o).map(orgIds -> String.join(",", orgIds)).get());
                    } else {
                        requestParams.add(entry.getKey(), Optional.ofNullable(entry.getValue()).map(String::valueOf).orElse(""));
                    }
                }

            }

            //设置请求头和请求体
            if (headers == null) {
                headers = new HttpHeaders();
            }
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());

            HttpEntity<?> request;

            if (isSendJson) {
                headers.setContentType(MediaType.APPLICATION_JSON);
                request = new HttpEntity<>(json, headers);
            } else {
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                request = new HttpEntity<>(requestParams, headers);
            }

            ResponseEntity<String> responseEntity;

            switch (method) {
                case PUT:
                    responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
                    break;
                case DELETE:
                    responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
                    break;
                case POST:
                    responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
                    break;
                case GET:
                    //发送get请求
                    responseEntity = restTemplate.exchange(processGetMethodUrl(url, params), HttpMethod.GET, request, String.class);
                    break;
                default:
                    return null;
            }

            return responseEntity.getBody();
        }
        return null;
    }

    /**
     * 处理get方法的URL问题
     *
     * @param url    原始url
     * @param params 参数map
     * @return 附带有uri参数的url
     */
    private String processGetMethodUrl(String url, Map<String, Object> params) {
        if (params != null && !url.contains("?")) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (entry.getValue() instanceof String[]) {
                    //针对数组型对象进行特殊处理
                    sb.append(entry.getKey()).append("=").append(String.join(",", (String[]) entry.getValue())).append("&");
                } else {
                    sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
            }
            sb.append("timestamp=").append(System.currentTimeMillis());
            //get请求就直接拼接字符串
            url += "?" + sb.toString();
        }

        return url;
    }
}
