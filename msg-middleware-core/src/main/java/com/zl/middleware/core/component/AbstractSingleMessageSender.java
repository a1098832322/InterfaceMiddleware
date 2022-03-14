package com.zl.middleware.core.component;

import com.zl.middleware.core.config.SystemConstant;
import com.zl.middleware.core.dto.MetaInfo;
import com.zl.middleware.core.entity.Interface;
import com.zl.middleware.core.entity.JsonField;
import com.zl.middleware.core.entity.JsonHeader;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 单独的消息发送器<br>
 * 用于发送单个对象
 *
 * @author zl
 * @since 2021/11/18 10:30
 */
public abstract class AbstractSingleMessageSender implements MessageSender {
    /**
     * 元数据
     */
    @Setter
    @Getter
    private MetaInfo metaInfo;

    /**
     * 获得定义的接口参数集合
     *
     * @return 定义的接口参数集合
     */
    @Override
    public List<JsonField> getDeclaredJsonFields() {
        return Optional.of(metaInfo.getJsonFields()).orElse(null);
    }

    /**
     * 获取对应接口id
     *
     * @return 对应接口id
     */
    @Override
    public Long getInterfaceId() {
        return Optional.of(metaInfo.getInterfaceInfo()).map(Interface::getId).orElseThrow(() -> new NullPointerException("接口id不能为空！"));
    }

    /**
     * 获得元数据中的接口参数值
     *
     * @return 元数据中的接口参数值
     */
    public Map<String, Object> getFieldValues() {
        return Optional.of(metaInfo.getFieldValues()).orElse(null);
    }

    /**
     * 获得json渲染策略
     *
     * @return json渲染策略
     */
    @Override
    public SystemConstant.RenderStrategy getRenderStrategy() {
        return Optional.of(metaInfo.getRenderStrategy()).orElse(null);
    }

    /**
     * 获得HTTP请求方式
     *
     * @return HTTP请求方式
     */
    @Override
    public HttpMethod getHttpMethod() {
        return Optional.of(metaInfo.getInterfaceInfo())
                .filter(anInterface -> anInterface.getEnable() == 1).map(Interface::getMethod)
                .map(method -> {
                    HttpMethod httpMethod = null;
                    for (SystemConstant.HttpMethodType type : SystemConstant.HttpMethodType.values()) {
                        if (method.equals(type.name())) {
                            httpMethod = type.getHttpMethod();
                            break;
                        }
                    }

                    return httpMethod;
                }).orElse(null);
    }

    /**
     * 获得预定义的header
     *
     * @return 接口需要的header们
     */
    @Override
    public List<JsonHeader> getDeclaredHeaders() {
        return metaInfo.getJsonHeaders();
    }

    /**
     * 获得header的value
     *
     * @return header的value
     */
    @Override
    public Map<String, String> getHeadersValue() {
        return metaInfo.getHeaderValues();
    }

    /**
     * 拿到请求URL地址
     *
     * @return 请求URL地址
     */
    @Override
    public String getHttpUrl() {
        return Optional.of(metaInfo.getInterfaceInfo()).filter(anInterface -> anInterface.getEnable() == 1).map(Interface::getUrl).orElse(null);
    }

    /**
     * 拿到http请求的header
     *
     * @return http请求的header
     */
    @Override
    public String getHttpHeader() {
        return Optional.of(metaInfo.getInterfaceInfo()).filter(anInterface -> anInterface.getEnable() == 1).map(Interface::getHeader).orElse(null);
    }
}
