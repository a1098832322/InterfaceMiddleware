package com.zl.middleware.core.dto;

import com.zl.middleware.core.config.SystemConstant;
import com.zl.middleware.core.entity.Interface;
import com.zl.middleware.core.entity.JsonField;
import com.zl.middleware.core.entity.JsonHeader;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * 记录接口信息的元数据
 *
 * @author zl
 * @since 2021/11/18 10:35
 */
@Data
@Accessors(chain = true)
public class MetaInfo {
    /**
     * 接口定义
     */
    private Interface interfaceInfo;

    /**
     * 可接受的接口参数们
     */
    private List<JsonField> jsonFields;

    /**
     * 可接受的接口header们
     */
    private List<JsonHeader> jsonHeaders;

    /**
     * 接口参数的值Map
     */
    private Map<String, Object> fieldValues;

    /**
     * 接口中的header值map
     */
    private Map<String, String> headerValues;

    /**
     * 数据渲染类型
     *
     * @see SystemConstant.RenderStrategy
     */
    private SystemConstant.RenderStrategy renderStrategy;
}
