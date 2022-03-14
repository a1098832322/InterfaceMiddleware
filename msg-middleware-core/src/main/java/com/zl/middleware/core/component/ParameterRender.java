package com.zl.middleware.core.component;

import com.zl.middleware.core.config.SystemConstant;
import com.zl.middleware.core.entity.JsonField;
import com.zl.middleware.core.service.SystemApiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Form表单提交参数渲染器
 *
 * @author zl
 * @since 2021/12/21 14:54
 */
public class ParameterRender {

    @Autowired
    private SystemApiService systemApiService;

    /**
     * 装配JSON Map
     *
     * @param interfaceId 渲染接口id
     * @param jsonFields  预定义的json字段们
     * @param fieldValues 值map
     * @param strategy    渲染策略
     * @return JSON Map
     * @see SystemConstant.RenderStrategy
     */
    public Map<String, Object> renderParameterMap(long interfaceId, List<JsonField> jsonFields, Map<String, Object> fieldValues, SystemConstant.RenderStrategy strategy) {
        Objects.requireNonNull(fieldValues, "数据值Map不允许为空！");
        Map<String, Object> resultValueMap = new HashMap<>(fieldValues.size() * 2);

        for (JsonField jsonField : jsonFields) {
            //非系统字段
            if (isEssentialJsonField(interfaceId, jsonField)) {
                //必须字段
                Object value = isSystemDefJsonField(jsonField) ? jsonField.getDefaultValue()
                        : Objects.requireNonNull(getEffectiveValue(fieldValues.getOrDefault(jsonField.getName(), null), jsonField.getDefaultValue()),
                        jsonField.getName() + "为接口必要字段，不能设置为空值！");
                checkType(jsonField, value);
                resultValueMap.put(jsonField.getName(), value);
            } else {
                //对于非必须字段
                if (strategy.equals(SystemConstant.RenderStrategy.ALL)) {
                    //如果是渲染所有则添加
                    Optional.ofNullable(isSystemDefJsonField(jsonField) ? jsonField.getDefaultValue()
                            : getEffectiveValue(fieldValues.getOrDefault(jsonField.getName(), null), jsonField.getDefaultValue())).ifPresent(value -> {
                        checkType(jsonField, value);
                        resultValueMap.put(jsonField.getName(), value);
                    });
                }
            }
        }

        return resultValueMap;
    }



    /**
     * 获得有效数据值，当值A不为空时取A的值，否则取B的值
     *
     * @param valueA 值A
     * @param valueB 值B
     * @return 值A或值B
     */
    private Object getEffectiveValue(Object valueA, Object valueB) {
        return valueA == null ? valueB : valueA;
    }

    /**
     * 检查给字段设置的值是否合法
     *
     * @param jsonField   接口json属性
     * @param targetValue 目标值
     */
    private void checkType(JsonField jsonField, Object targetValue) {
        if (StringUtils.isNotBlank(jsonField.getType())) {
            SystemConstant.FieldType fieldType = SystemConstant.FieldType.valueOf(jsonField.getType());
            if (!fieldType.getClazz().isAssignableFrom(targetValue.getClass())) {
                throw new IllegalArgumentException(jsonField.getName() + "字段定义的字段类型为：" + jsonField.getType() + "\t给定值的类型为：" + targetValue.getClass());
            } else {
                return;
            }
        }

        throw new IllegalArgumentException(jsonField.getName() + "字段定义的字段类型为空!");
    }

    /**
     * 是否是系统保留字段
     *
     * @param jsonField 字段
     * @return true/false 是/否
     */
    private boolean isSystemDefJsonField(JsonField jsonField) {
        return systemApiService.isSystemDefJsonField(jsonField.getId());
    }

    /**
     * 是否是必须字段
     *
     * @param interfaceId 渲染接口id
     * @param jsonField   字段
     * @return true/false 是/否
     */
    private boolean isEssentialJsonField(long interfaceId, JsonField jsonField) {
        return systemApiService.isEssentialJsonField(jsonField.getId(), interfaceId);
    }
}
