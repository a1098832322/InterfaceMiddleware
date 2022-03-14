package com.zl.middleware.core.component;

import com.alibaba.fastjson.JSON;
import com.zl.middleware.core.config.SystemConstant;
import com.zl.middleware.core.entity.JsonField;
import com.zl.middleware.core.service.SystemApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * json渲染器，使用数据库中定义的字段模型对属性进行渲染
 *
 * @author zl
 * @since 2021/11/18 11:12
 */
public class JsonRender {

    @Autowired
    private SystemApiService systemApiService;

    @Autowired
    private ParameterRender parameterRender;

    /**
     * 渲染所有（必须非必须）的字段为json
     *
     * @param jsonFields  接口json属性
     * @param fieldValues 值map
     * @return json object
     * @see SystemConstant.RenderStrategy
     * @see #renderObject(long, List, Map, SystemConstant.RenderStrategy)  renderObject
     */
    public String renderAllFieldsObject(long interfaceId, List<JsonField> jsonFields, Map<String, Object> fieldValues) {
        return renderObject(interfaceId, jsonFields, fieldValues, SystemConstant.RenderStrategy.ALL);
    }


    /**
     * 只渲染接口必要的字段为json
     *
     * @param jsonFields  接口json属性
     * @param fieldValues 值map
     * @return json object
     * @see SystemConstant.RenderStrategy
     * @see #renderObject(long, List, Map, SystemConstant.RenderStrategy) renderObject
     */
    public String renderEssentialObject(long interfaceId, List<JsonField> jsonFields, Map<String, Object> fieldValues) {
        return renderObject(interfaceId, jsonFields, fieldValues, SystemConstant.RenderStrategy.IGNORE_NO_ESSENTIAL);
    }

    /**
     * 渲染所有（必须非必须）的字段为json
     *
     * @param jsonFields  接口json属性
     * @param fieldValues 值map
     * @return json array
     * @see SystemConstant.RenderStrategy
     * @see #renderArray(long, List, List, SystemConstant.RenderStrategy)  renderArray
     */
    public String renderAllFieldsArray(long interfaceId, List<JsonField> jsonFields, List<Map<String, Object>> fieldValues) {
        return renderArray(interfaceId, jsonFields, fieldValues, SystemConstant.RenderStrategy.ALL);
    }


    /**
     * 只渲染接口必要的字段为json
     *
     * @param jsonFields  接口json属性
     * @param fieldValues 值map
     * @return json array
     * @see SystemConstant.RenderStrategy
     * @see #renderArray(long, List, List, SystemConstant.RenderStrategy) renderArray
     */
    public String renderEssentialArray(long interfaceId, List<JsonField> jsonFields, List<Map<String, Object>> fieldValues) {
        return renderArray(interfaceId, jsonFields, fieldValues, SystemConstant.RenderStrategy.IGNORE_NO_ESSENTIAL);
    }

    /**
     * 将接口参数模板和值map整合渲染成JSON Array
     *
     * @param interfaceId 渲染接口id
     * @param jsonFields  接口json属性
     * @param fieldValues 值map
     * @param strategy    渲染策略
     * @return json array
     * @see SystemConstant.RenderStrategy
     */
    public String renderArray(long interfaceId, List<JsonField> jsonFields, List<Map<String, Object>> fieldValues, SystemConstant.RenderStrategy strategy) {
        if (strategy == null) {
            //默认渲染所有
            strategy = SystemConstant.RenderStrategy.ALL;
        }

        //集合中添加系统保留字段
        jsonFields.addAll(systemApiService.querySystemFields(strategy.equals(SystemConstant.RenderStrategy.ALL)));

        if (CollectionUtils.isEmpty(fieldValues)) {
            //数据集合为空时，返回空JSON Array
            return "[]";
        } else {
            List<Map<String, Object>> jsonMapList = new ArrayList<>();
            for (Map<String, Object> fieldValue : fieldValues) {
                jsonMapList.add(parameterRender.renderParameterMap(interfaceId, jsonFields, fieldValue, strategy));
            }

            return JSON.toJSONString(jsonMapList);
        }
    }

    /**
     * 将接口参数模板和值map整合渲染成JSON Object
     *
     * @param interfaceId 渲染接口id
     * @param jsonFields  接口json属性
     * @param fieldValues 值map
     * @param strategy    渲染策略
     * @return json object
     * @see SystemConstant.RenderStrategy
     */
    public String renderObject(long interfaceId, List<JsonField> jsonFields, Map<String, Object> fieldValues, SystemConstant.RenderStrategy strategy) {
        if (strategy == null) {
            //默认渲染所有
            strategy = SystemConstant.RenderStrategy.ALL;
        }

        //集合中添加系统保留字段
        jsonFields.addAll(systemApiService.querySystemFields(strategy.equals(SystemConstant.RenderStrategy.ALL)));

        //获得JSON Map
        Map<String, Object> resultValueMap = parameterRender.renderParameterMap(interfaceId, jsonFields, fieldValues, strategy);

        //渲染为jsonObject
        return JSON.toJSONString(resultValueMap);
    }
}
