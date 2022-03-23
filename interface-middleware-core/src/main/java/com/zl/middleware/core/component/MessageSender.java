package com.zl.middleware.core.component;

import com.zl.middleware.core.config.SystemConstant;
import com.zl.middleware.core.entity.JsonField;
import com.zl.middleware.core.entity.JsonHeader;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

/**
 * 消息发送接口
 *
 * @author zl
 * @since 2021/12/7 11:10
 */
public interface MessageSender {

    /**
     * 向目标接口推送消息数据
     */
    String sendMessage() throws Exception;

    /**
     * 获得定义的接口参数集合
     *
     * @return 定义的接口参数集合
     */
    List<JsonField> getDeclaredJsonFields();

    /**
     * 获取对应接口id
     *
     * @return 对应接口id
     */
    Long getInterfaceId();

    /**
     * 获得json渲染策略
     *
     * @return json渲染策略
     */
    SystemConstant.RenderStrategy getRenderStrategy();

    /**
     * 获得HTTP请求方式
     *
     * @return HTTP请求方式
     */
    HttpMethod getHttpMethod();

    /**
     * 获得预定义的header
     *
     * @return 接口需要的header们
     */
    List<JsonHeader> getDeclaredHeaders();

    /**
     * 获得header的value
     *
     * @return header的value
     */
    Map<String, String> getHeadersValue();

    /**
     * 拿到请求URL地址
     *
     * @return 请求URL地址
     */
    String getHttpUrl();

    /**
     * 拿到http请求的header
     *
     * @return http请求的header
     */
    String getHttpHeader();
}
