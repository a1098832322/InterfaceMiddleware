package com.zl.middleware.core.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 实体类转map
 *
 * @author 郑龙
 * @since 2021/1/26 10:23
 */
public class EntityToMapUtils {
    private EntityToMapUtils() {
    }

    /**
     * 将实体转换为mapList
     *
     * @param objectList 实体对象List
     * @param <T>        模板类型
     * @return 实体属性值map
     * @throws IllegalAccessException 反射取值异常
     */
    public static <T> List<HashMap<String, Object>> convertToHashMapList(List<T> objectList)
            throws IllegalAccessException {
        return convertToHashMapList(objectList, false);
    }

    /**
     * 将实体转换为mapList
     *
     * @param objectList            实体对象List
     * @param isShowSuperClassField 是否存储实体父类属性值
     * @param <T>                   模板类型
     * @return 实体属性值map
     * @throws IllegalAccessException 反射取值异常
     */
    public static <T> List<HashMap<String, Object>> convertToHashMapList(List<T> objectList,
                                                                                     boolean isShowSuperClassField)
            throws IllegalAccessException {
        List<HashMap<String, Object>> resultMapList = new ArrayList<HashMap<String, Object>>();
        if (!CollectionUtils.isEmpty(objectList)) {
            for (T obj : objectList) {
                resultMapList.add(convertToHashMap(obj, isShowSuperClassField));
            }
        }

        return resultMapList;
    }

    /**
     * 将实体转换为map(不取父类属性值)
     *
     * @param object 实体对象
     * @param <T>    模板类型
     * @return 实体属性值map
     * @throws IllegalAccessException 反射取值异常
     * @see #convertToHashMap(Object, boolean)  convertToHashMap(Object, false)
     */
    public static <T> HashMap<String, Object> convertToHashMap(T object) throws IllegalAccessException {
        return convertToHashMap(object, false);
    }

    /**
     * 将实体转换为map
     *
     * @param object                实体对象
     * @param isShowSuperClassField 是否存储实体父类属性值
     * @param <T>                   模板类型
     * @return 实体属性值map
     * @throws IllegalAccessException 反射取值异常
     */
    public static <T> HashMap<String, Object> convertToHashMap(T object, boolean isShowSuperClassField)
            throws IllegalAccessException {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(32);
        if (object != null) {
            //显示父类属性
            Class<?> clazz = object.getClass();
            if (isShowSuperClassField) {
                while (clazz != null) {
                    addEntry(object, clazz, resultMap);
                    clazz = clazz.getSuperclass();
                }
            } else {
                addEntry(object, clazz, resultMap);
            }
        }

        return resultMap;
    }

    /**
     * 将属性结果集添加进map
     *
     * @param targetObject 目标对象
     * @param clazz        对象类型
     * @param resultMap    结果集map
     * @param <T>          模板类型
     * @throws IllegalAccessException 反射取值异常
     */
    private static <T> void addEntry(T targetObject, Class<?> clazz, HashMap<String, Object> resultMap) throws IllegalAccessException {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            //支持部分jackson注解属性
            JsonIgnore jsonIgnore = field.getAnnotation(JsonIgnore.class);
            //当没有被注解或该注解属性为false时才进行值转储
            if (jsonIgnore == null || !jsonIgnore.value()) {
                //取值存入map
                resultMap.put(field.getName(), field.get(targetObject));
            }
        }
    }
}
