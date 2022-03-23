package com.zl.middleware.core.service;

import com.zl.middleware.core.dto.JsonFieldDto;
import com.zl.middleware.core.entity.Interface;
import com.zl.middleware.core.entity.JsonField;
import com.zl.middleware.core.entity.JsonHeader;
import com.zl.middleware.core.entity.SystemInfo;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 目标对接接口查询Service
 *
 * @author zl
 * @since 2021/11/18 16:14
 */
public interface SystemApiService {
    /**
     * 根据名称查询系统信息数据分页
     *
     * @param nameLike    名称
     * @param currentPage 当前页码
     * @param pageSize    每页显示条数
     * @return 系统信息数据分页
     */
    Page<SystemInfo> querySystemInfoPage(String nameLike, int currentPage, int pageSize);

    /**
     * 根据名称查询接口数据分页
     *
     * @param currentPage  当前页码
     * @param pageSize     每页显示条数
     * @param nameLike     名称
     * @param systemInfoId 系统信息id
     * @return 接口数据分页
     */
    Page<Interface> queryInterfaceBySystemInfoIdPage(String nameLike, long systemInfoId, int currentPage, int pageSize);

    /**
     * 根据名称查询接口数据分页
     *
     * @param currentPage 当前页码
     * @param pageSize    每页显示条数
     * @param nameLike    名称
     * @return 接口数据分页
     */
    Page<Interface> queryInterfacePage(String nameLike, int currentPage, int pageSize);

    /**
     * 根据id插叙接口信息
     *
     * @param id 接口id
     * @return 接口信息
     */
    Interface queryInterfaceById(long id);

    /**
     * 根据自定义的唯一key查询接口信息
     *
     * @param key 自定义的唯一key
     * @return 接口信息
     */
    Interface queryInterfaceByKey(String key);

    /**
     * 根据自定义的唯一key查询系统信息
     *
     * @param key 自定义的唯一key
     * @return 系统信息
     */
    SystemInfo querySystemInfoByKey(String key);

    /**
     * 根据接口id查询接口定义的字段
     *
     * @param interfaceId 接口id
     * @return 接口定义的字段
     */
    List<JsonField> queryDeclaredFieldsByInterfaceId(long interfaceId);

    /**
     * 查询所有字段属性
     *
     * @return 所有字段属性list
     */
    List<JsonFieldDto> queryAllFields();

    /**
     * 根据接口id查询接口定义的header
     *
     * @param interfaceId 接口id
     * @return 接口定义的header
     */
    List<JsonHeader> queryDeclaredHeadersByInterfaceId(long interfaceId);

    /**
     * 查询系统保留/预定义字段
     *
     * @param isQueryAll 是否查询所有<br>
     *                   <ul>
     *                   <li>true: 查询所有</li>
     *                   <li>false: 只查询{@link com.zl.middleware.core.entity.JsonFieldScope#getEssential() essential} = 1 (即:必须的)字段</li>
     *                   </ul>
     * @return 系统保留/预定义字段
     */
    List<JsonField> querySystemFields(boolean isQueryAll);

    /**
     * 判断当前字段是否设置为了系统字段
     *
     * @param fieldId 字段id
     * @return true/false 是/否
     */
    boolean isSystemDefJsonField(long fieldId);

    /**
     * 判断当前字段在当前接口下是否是必须字段
     *
     * @param fieldId     字段id
     * @param interfaceId 接口id
     * @return true/false 是/否
     */
    boolean isEssentialJsonField(long fieldId, long interfaceId);
}
