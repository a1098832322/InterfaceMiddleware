package com.zl.middleware.core.service.impl;

import com.zl.middleware.core.dto.InterfaceDto;
import com.zl.middleware.core.dto.JsonFieldDto;
import com.zl.middleware.core.dto.SystemInfoDto;
import com.zl.middleware.core.entity.Interface;
import com.zl.middleware.core.entity.JsonField;
import com.zl.middleware.core.entity.JsonFieldScope;
import com.zl.middleware.core.entity.SystemInfo;
import com.zl.middleware.core.repository.InterfaceRepository;
import com.zl.middleware.core.repository.JsonFieldRepository;
import com.zl.middleware.core.repository.JsonFieldScopeRepository;
import com.zl.middleware.core.repository.SystemInfoRepository;
import com.zl.middleware.core.service.ManagementService;
import com.zl.middleware.core.utils.EntityDeepCopyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 系统信息管理服务实现类
 *
 * @author zl
 * @since 2021/11/22 15:55
 */
@Slf4j
@Service
@Transactional(value = "jpaTransactionManager", rollbackFor = Exception.class)
public class ManagementServiceImpl implements ManagementService {

    @Autowired
    private SystemInfoRepository systemInfoRepository;

    @Autowired
    private InterfaceRepository interfaceRepository;

    @Autowired
    private JsonFieldRepository jsonFieldRepository;

    @Autowired
    private JsonFieldScopeRepository scopeRepository;

    /**
     * 新增系统信息
     *
     * @param systemInfoDto 系统信息dto
     * @return 新增数据id
     */
    @Override
    public Long addSystemInfo(SystemInfoDto systemInfoDto) {
        Objects.requireNonNull(systemInfoDto, "系统信息实体对象不能为空！");
        SystemInfo entity = EntityDeepCopyUtils.copyEntityProperties(systemInfoDto, new SystemInfo());
        systemInfoRepository.save(entity);
        return entity.getId();
    }

    /**
     * 批量更新系统信息
     *
     * @param systemInfoDtoList 系统信息list
     * @return 新增的数据id们
     */
    @Override
    public List<Long> addSystemInfo(List<SystemInfoDto> systemInfoDtoList) {
        if (!CollectionUtils.isEmpty(systemInfoDtoList)) {
            return systemInfoDtoList.stream().map(this::addSystemInfo).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 更新系统信息
     *
     * @param systemInfoDto 系统信息dto
     * @return 更新结果
     */
    @Override
    public boolean updateSystemInfo(SystemInfoDto systemInfoDto) {
        Objects.requireNonNull(systemInfoDto, "系统信息实体对象不能为空！");
        SystemInfo entity = EntityDeepCopyUtils.copyEntityProperties(systemInfoDto, new SystemInfo());
        systemInfoRepository.save(entity);
        return true;
    }

    /**
     * 根据id删除系统信息
     *
     * @param id 系统信息id
     * @return 删除结果
     */
    @Override
    public boolean deleteSystemInfoById(long id) {
        systemInfoRepository.deleteById(id);
        return true;
    }

    /**
     * 添加接口信息
     *
     * @param interfaceDto 接口信息
     * @return 接口信息id
     */
    @Override
    public Long addInterface(InterfaceDto interfaceDto) {
        Objects.requireNonNull(interfaceDto, "接口信息实体对象不能为空！");
        Interface entity = EntityDeepCopyUtils.copyEntityProperties(interfaceDto, new Interface());
        interfaceRepository.save(entity);
        return entity.getId();
    }

    /**
     * 批量添加接口信息
     *
     * @param interfaceDtoList 接口信息list
     * @return 接口信息id们
     */
    @Override
    public List<Long> addInterface(List<InterfaceDto> interfaceDtoList) {
        if (!CollectionUtils.isEmpty(interfaceDtoList)) {
            return interfaceDtoList.stream().map(this::addInterface).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 更新接口信息
     *
     * @param interfaceDto 接口信息
     * @return 更新结果
     */
    @Override
    public boolean updateInterface(InterfaceDto interfaceDto) {
        Objects.requireNonNull(interfaceDto, "接口信息实体对象不能为空！");
        Interface entity = EntityDeepCopyUtils.copyEntityProperties(interfaceDto, new Interface());
        interfaceRepository.save(entity);
        return true;
    }

    /**
     * 根据id删除接口信息
     *
     * @param id 接口信息id
     * @return 删除结果
     */
    @Override
    public boolean deleteInterfaceById(long id) {
        interfaceRepository.deleteById(id);
        return true;
    }

    /**
     * 批量新增接口字段属性
     *
     * @param jsonFieldDtoList 接口字段属性list
     * @return 接口字段属性id们
     */
    @Override
    public List<Long> addJsonFields(List<JsonFieldDto> jsonFieldDtoList) {
        if (!CollectionUtils.isEmpty(jsonFieldDtoList)) {
            return jsonFieldDtoList.stream().map(jsonFieldDto -> {
                Objects.requireNonNull(jsonFieldDto, "接口字段实体对象不能为空！");
                JsonField entity = EntityDeepCopyUtils.copyEntityProperties(jsonFieldDto, new JsonField());
                jsonFieldRepository.save(entity);

                return entity.getId();
            }).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 批量删除接口字段
     *
     * @param idList 接口字段id们
     * @return 删除结果
     */
    @Override
    public boolean deleteJsonFieldsById(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        } else {
            idList.forEach(id -> jsonFieldRepository.deleteById(id));
            return true;
        }
    }

    /**
     * 清除接口与字段的关联关系
     *
     * @param interfaceId 接口id
     */
    @Override
    public boolean deleteRelateByInterfaceId(long interfaceId) {
        try {
            scopeRepository.deleteByInterfaceId(interfaceId);
            return true;
        } catch (Exception e) {
            log.warn("根据接口id:{}删除关联关系失败！异常：", interfaceId, e);
            return false;
        }
    }

    /**
     * 根据接口id批量新增关联关系
     *
     * @param interfaceId 接口id
     * @param fieldList   字段list
     * @return true/false 成功/失败
     */
    @Override
    public boolean insertRelateByInterfaceIdBatch(long interfaceId, List<JsonFieldDto> fieldList) {
        try {
            Iterable<JsonFieldScope> scopes = fieldList.stream().map(field -> {
                JsonFieldScope scope = new JsonFieldScope();
                scope.setFieldId(field.getId()).setInterfaceId(interfaceId).setEssential(field.getEssential());
                return scope;
            }).collect(Collectors.toList());

            //批量新增
            scopeRepository.saveAll(scopes);
            return true;
        } catch (Exception e) {
            log.warn("批量新增接口:{}与字段的关联关系发生异常！异常:", interfaceId, e);
        }

        return false;
    }
}
