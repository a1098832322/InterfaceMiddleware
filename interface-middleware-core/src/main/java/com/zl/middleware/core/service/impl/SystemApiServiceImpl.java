package com.zl.middleware.core.service.impl;

import com.google.gson.reflect.TypeToken;
import com.zl.middleware.core.dto.JsonFieldDto;
import com.zl.middleware.core.entity.*;
import com.zl.middleware.core.repository.*;
import com.zl.middleware.core.service.SystemApiService;
import com.zl.middleware.core.utils.EntityDeepCopyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 目标对接接口查询Service实现类
 *
 * @author zl
 * @since 2021/11/18 16:14
 */
@Service
public class SystemApiServiceImpl implements SystemApiService {

    @Autowired
    private SystemInfoRepository systemInfoRepository;

    @Autowired
    private InterfaceRepository interfaceRepository;

    @Autowired
    private JsonFieldRepository jsonFieldRepository;

    @Autowired
    private JsonHeaderRepository jsonHeaderRepository;

    @Autowired
    private JsonFieldScopeRepository jsonFieldScopeRepository;

    /**
     * 根据名称查询系统信息数据分页
     *
     * @param nameLike    名称
     * @param currentPage 当前页码
     * @param pageSize    每页显示条数
     * @return 系统信息数据分页
     */
    @Override
    public Page<SystemInfo> querySystemInfoPage(String nameLike, int currentPage, int pageSize) {
        Pageable pageable = generatePageable(currentPage, pageSize);

        if (StringUtils.isNotBlank(nameLike)) {
            return systemInfoRepository.findSystemInfosByNameLike("%" + nameLike + "%", pageable);
        }

        return systemInfoRepository.findAll(pageable);
    }

    /**
     * 根据名称查询接口数据分页
     *
     * @param nameLike     名称
     * @param systemInfoId 系统信息id
     * @param currentPage  当前页码
     * @param pageSize     每页显示条数
     * @return 接口数据分页
     */
    @Override
    public Page<Interface> queryInterfaceBySystemInfoIdPage(String nameLike, long systemInfoId, int currentPage, int pageSize) {
        Pageable pageable = generatePageable(currentPage, pageSize);
        if (StringUtils.isNotBlank(nameLike)) {
            return interfaceRepository.findInterfacesByNameLikeAndSystemInfoIdEquals("%" + nameLike + "%", systemInfoId, pageable);
        }
        return interfaceRepository.findInterfacesBySystemInfoIdEquals(systemInfoId, pageable);
    }

    /**
     * 根据名称查询接口数据分页
     *
     * @param nameLike    名称
     * @param currentPage 当前页码
     * @param pageSize    每页显示条数
     * @return 接口数据分页
     */
    @Override
    public Page<Interface> queryInterfacePage(String nameLike, int currentPage, int pageSize) {
        Pageable pageable = generatePageable(currentPage, pageSize);
        if (StringUtils.isNotBlank(nameLike)) {
            return interfaceRepository.findInterfacesByNameLike("%" + nameLike + "%", pageable);
        }
        return interfaceRepository.findAll(pageable);
    }

    /**
     * 根据id插叙接口信息
     *
     * @param id 接口id
     * @return 接口信息
     */
    @Override
    public Interface queryInterfaceById(long id) {
        return interfaceRepository.findById(id).orElse(null);
    }

    /**
     * 根据自定义的唯一key查询接口信息
     *
     * @param key 自定义的唯一key
     * @return 接口信息
     */
    @Override
    public Interface queryInterfaceByKey(String key) {
        return interfaceRepository.findInterfaceByKey(key);
    }

    /**
     * 根据自定义的唯一key查询系统信息
     *
     * @param key 自定义的唯一key
     * @return 系统信息
     */
    @Override
    public SystemInfo querySystemInfoByKey(String key) {
        return systemInfoRepository.findSystemInfoByKey(key);
    }

    /**
     * 根据接口id查询接口定义的字段
     *
     * @param interfaceId 接口id
     * @return 接口定义的字段
     */
    @Override
    public List<JsonField> queryDeclaredFieldsByInterfaceId(long interfaceId) {
        return jsonFieldRepository.findJsonFieldsByPid(interfaceId);
    }

    /**
     * 查询所有字段属性
     *
     * @return 所有字段属性list
     */
    @Override
    public List<JsonFieldDto> queryAllFields() {
        return EntityDeepCopyUtils.copyListProperties(jsonFieldRepository.findAll(), new TypeToken<List<JsonFieldDto>>() {});
    }

    /**
     * 根据接口id查询接口定义的header
     *
     * @param interfaceId 接口id
     * @return 接口定义的header
     */
    @Override
    public List<JsonHeader> queryDeclaredHeadersByInterfaceId(long interfaceId) {
        return jsonHeaderRepository.findJsonHeadersByPid(interfaceId);
    }

    /**
     * 查询系统保留/预定义字段
     *
     * @param isQueryAll 是否查询所有<br>
     *                   <ul>
     *                   <li>true: 查询所有</li>
     *                   <li>false: 只查询{@link JsonFieldScope#getEssential() essential} = 1 (即:必须的)字段</li>
     *                   </ul>
     * @return 系统保留/预定义字段
     */
    @Override
    public List<JsonField> querySystemFields(boolean isQueryAll) {
        return jsonFieldRepository.findSystemJsonFields().stream()
                .filter(jsonField -> {
                    if (isQueryAll) {
                        return true;
                    } else {
                        return isEssentialJsonField(jsonField.getId(), 0L);
                    }
                }).collect(Collectors.toList());
    }

    /**
     * 判断当前字段是否设置为了系统字段
     *
     * @param fieldId 字段id
     * @return true/false 是/否
     */
    @Override
    public boolean isSystemDefJsonField(long fieldId) {
        List<Long> interfaceIdList = jsonFieldScopeRepository.findInterfaceIdByFieldId(fieldId);
        if (!CollectionUtils.isEmpty(interfaceIdList)) {
            return interfaceIdList.contains(0L);
        }
        return false;
    }

    /**
     * 判断当前字段在当前接口下是否是必须字段
     *
     * @param fieldId     字段id
     * @param interfaceId 接口id
     * @return true/false 是/否
     */
    @Override
    public boolean isEssentialJsonField(long fieldId, long interfaceId) {
        Integer isEssential = jsonFieldScopeRepository.queryEssentialByFieldIdAndInterfaceId(fieldId, interfaceId);
        return isEssential != null && isEssential == 1;
    }

    /**
     * 构造分页参数
     *
     * @param currentPage 当前页
     * @param pageSize    每页显示条数
     * @return Pageable
     */
    private Pageable generatePageable(int currentPage, int pageSize) {
        return PageRequest.of((currentPage - 1) * pageSize, pageSize);
    }
}
