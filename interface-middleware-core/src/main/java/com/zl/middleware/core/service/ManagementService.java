package com.zl.middleware.core.service;

import com.zl.middleware.core.dto.InterfaceDto;
import com.zl.middleware.core.dto.JsonFieldDto;
import com.zl.middleware.core.dto.SystemInfoDto;

import java.util.List;

/**
 * 系统信息管理服务
 *
 * @author zl
 * @since 2021/11/22 15:55
 */
public interface ManagementService {
    /**
     * 新增系统信息
     *
     * @param systemInfoDto 系统信息dto
     * @return 新增数据id
     */
    Long addSystemInfo(SystemInfoDto systemInfoDto);

    /**
     * 批量更新系统信息
     *
     * @param systemInfoDtoList 系统信息list
     * @return 新增的数据id们
     */
    List<Long> addSystemInfo(List<SystemInfoDto> systemInfoDtoList);

    /**
     * 更新系统信息
     *
     * @param systemInfoDto 系统信息dto
     * @return 更新结果
     */
    boolean updateSystemInfo(SystemInfoDto systemInfoDto);

    /**
     * 根据id删除系统信息
     *
     * @param id 系统信息id
     * @return 删除结果
     */
    boolean deleteSystemInfoById(long id);

    /**
     * 添加接口信息
     *
     * @param interfaceDto 接口信息
     * @return 接口信息id
     */
    Long addInterface(InterfaceDto interfaceDto);

    /**
     * 批量添加接口信息
     *
     * @param interfaceDtoList 接口信息list
     * @return 接口信息id们
     */
    List<Long> addInterface(List<InterfaceDto> interfaceDtoList);

    /**
     * 更新接口信息
     *
     * @param interfaceDto 接口信息
     * @return 更新结果
     */
    boolean updateInterface(InterfaceDto interfaceDto);

    /**
     * 根据id删除接口信息
     *
     * @param id 接口信息id
     * @return 删除结果
     */
    boolean deleteInterfaceById(long id);

    /**
     * 批量新增接口字段属性
     *
     * @param jsonFieldDtoList 接口字段属性list
     * @return 接口字段属性id们
     */
    List<Long> addJsonFields(List<JsonFieldDto> jsonFieldDtoList);

    /**
     * 批量删除接口字段
     *
     * @param idList 接口字段id们
     * @return 删除结果
     */
    boolean deleteJsonFieldsById(List<Long> idList);

    /**
     * 清除接口与字段的关联关系
     *
     * @return true/false 成功/失败
     */
    boolean deleteRelateByInterfaceId(long interfaceId);

    /**
     * 根据接口id批量新增关联关系
     *
     * @param interfaceId 接口id
     * @param fieldList   字段list
     * @return true/false 成功/失败
     */
    boolean insertRelateByInterfaceIdBatch(long interfaceId, List<JsonFieldDto> fieldList);
}
