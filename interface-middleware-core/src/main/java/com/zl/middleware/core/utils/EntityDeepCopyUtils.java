package com.zl.middleware.core.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 实体深拷贝工具类
 *
 * @author 郑龙
 * @since 2021/5/18 14:45
 */
public class EntityDeepCopyUtils {
    private EntityDeepCopyUtils() {
    }

    /**
     * gson object
     */
    private static final Gson gson = new Gson();

    /**
     * 深拷贝对象，新对象需要与原始对象中属性同名且同类型
     *
     * @param origin 原始对象
     * @param copy   深拷贝返回对象
     * @param <T>    原始对象类型
     * @param <R>    深拷贝对象类型
     * @return 深拷贝对象
     */
    public static <T, R> R copyEntityProperties(@NonNull T origin, @NonNull R copy) {
        BeanUtils.copyProperties(origin, copy);
        return copy;
    }

    /**
     * 深拷贝对象List，新对象需要与原始对象中属性同名
     *
     * @param origin        原始对象list
     * @param copyTypeToken 新对象类型的{@link TypeToken TypeToken}实例
     * @param <T>           原始对象类型
     * @param <R>           深拷贝对象类型
     * @return 深拷贝对象List
     */
    public static <T, R> List<R> copyListProperties(@NonNull List<T> origin, @NonNull TypeToken<List<R>> copyTypeToken) {
        Type type = copyTypeToken.getType();
        return gson.fromJson(gson.toJson(origin), type);
    }

}
