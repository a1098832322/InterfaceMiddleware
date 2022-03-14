package com.zl.middleware.core.component;

import com.zl.middleware.core.config.SystemConstant;
import com.zl.middleware.core.dto.MetaInfo;
import com.zl.middleware.core.entity.Interface;
import com.zl.middleware.core.entity.JsonField;
import com.zl.middleware.core.entity.JsonHeader;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 数组消息发送器<br>
 * 区别于{@link  AbstractSingleMessageSender 单条消息发送器}，用于发送一个消息集合(JSON Array形式)<br>
 * 该消息发送器要求数组内所有元素具有同一特征（即：类型相同，header、renderStrategy、httpMethod等预定义值相同）
 *
 * @author zl
 * @since 2021/12/7 11:03
 */
public abstract class AbstractArrayMessageSender implements MessageSender {
    /**
     * 元数据组
     */
    @Getter
    @Setter
    private List<MetaInfo> metaInfoList;

    /**
     * 获得数组中数据的典型值，用于获取header、renderStrategy、httpMethod等预定义值信息
     *
     * @return 数组中数据的典型值
     */
    private MetaInfo getTypicalValue() {
        if (CollectionUtils.isEmpty(this.metaInfoList)) {
            return null;
        } else {
            return metaInfoList.get(0);
        }
    }

    /**
     * 获得定义的接口参数集合
     *
     * @return 定义的接口参数集合
     */
    @Override
    public List<JsonField> getDeclaredJsonFields() {
        return Optional.ofNullable(getTypicalValue()).flatMap(metaInfo -> Optional.of(metaInfo.getJsonFields())).orElse(null);
    }

    /**
     * 获取对应接口id
     *
     * @return 对应接口id
     */
    @Override
    public Long getInterfaceId() {
        return Optional.ofNullable(getTypicalValue()).flatMap(metaInfo -> Optional.of(metaInfo.getInterfaceInfo()))
                .map(Interface::getId).orElseThrow(() -> new NullPointerException("接口id不能为空！"));
    }

    /**
     * 获得元数据中的接口参数值
     *
     * @return 元数据中的接口参数值
     */
    public List<Map<String, Object>> getFieldValues() {
        final List<Map<String, Object>> fieldValues = new ArrayList<>();
        Optional.of(metaInfoList).ifPresent(metaInfos -> {
            if (!CollectionUtils.isEmpty(metaInfos)) {
                metaInfos.forEach(metaInfo -> fieldValues.add(metaInfo.getFieldValues()));
            }
        });

        return fieldValues;
    }

    /**
     * 获得json渲染策略
     *
     * @return json渲染策略
     */
    @Override
    public SystemConstant.RenderStrategy getRenderStrategy() {
        return Optional.ofNullable(getTypicalValue()).flatMap(metaInfo -> Optional.of(metaInfo.getRenderStrategy())).orElse(null);
    }

    /**
     * 获得HTTP请求方式
     *
     * @return HTTP请求方式
     */
    @Override
    public HttpMethod getHttpMethod() {
        return Optional.ofNullable(getTypicalValue())
                .flatMap(metaInfo -> Optional.of(metaInfo.getInterfaceInfo()))
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
        return Optional.ofNullable(getTypicalValue()).map(MetaInfo::getJsonHeaders).orElse(null);
    }

    /**
     * 获得header的value
     *
     * @return header的value
     */
    @Override
    public Map<String, String> getHeadersValue() {
        return Optional.ofNullable(getTypicalValue()).map(MetaInfo::getHeaderValues).orElse(null);
    }

    /**
     * 拿到请求URL地址
     *
     * @return 请求URL地址
     */
    @Override
    public String getHttpUrl() {
        return Optional.ofNullable(getTypicalValue()).flatMap(metaInfo -> Optional.of(metaInfo.getInterfaceInfo()))
                .filter(anInterface -> anInterface.getEnable() == 1).map(Interface::getUrl).orElse(null);
    }

    /**
     * 拿到http请求的header
     *
     * @return http请求的header
     */
    @Override
    public String getHttpHeader() {
        return Optional.ofNullable(getTypicalValue()).flatMap(metaInfo -> Optional.of(metaInfo.getInterfaceInfo()))
                .filter(anInterface -> anInterface.getEnable() == 1).map(Interface::getHeader).orElse(null);
    }
}
