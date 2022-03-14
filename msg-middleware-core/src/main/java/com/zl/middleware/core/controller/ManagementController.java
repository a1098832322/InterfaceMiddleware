package com.zl.middleware.core.controller;

import com.zl.middleware.core.dto.InterfaceDto;
import com.zl.middleware.core.dto.JsonFieldDto;
import com.zl.middleware.core.dto.RelatePayload;
import com.zl.middleware.core.dto.SystemInfoDto;
import com.zl.middleware.core.entity.Interface;
import com.zl.middleware.core.entity.SystemInfo;
import com.zl.middleware.core.service.ManagementService;
import com.zl.middleware.core.service.SystemApiService;
import com.zl.middleware.core.utils.EntityDeepCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统数据管理服务Controller
 *
 * @author zl
 * @since 2021/11/22 16:31
 */
@RestController
@RequestMapping("/management")
public class ManagementController {

    private static final int DEFAULT_PAGE = 1;

    private static final int DEFAULT_PAGE_SIZE = 9999999;

    /**
     * 数据管理服务
     */
    @Autowired
    private ManagementService managementService;

    /**
     * 数据查询服务
     */
    @Autowired
    private SystemApiService systemApiService;

    /**
     * 新增系统信息
     *
     * @param systemInfoDto 系统信息dto
     * @return 新增数据id
     */
    @PostMapping("/addSystemInfo")
    public Long addSystemInfo(SystemInfoDto systemInfoDto) {
        return managementService.addSystemInfo(systemInfoDto);
    }

    /**
     * 批量更新系统信息
     *
     * @param systemInfoDtoList 系统信息list
     * @return 新增的数据id们
     */
    @PostMapping("/addSystemInfoList")
    public List<Long> addSystemInfoList(List<SystemInfoDto> systemInfoDtoList) {
        return managementService.addSystemInfo(systemInfoDtoList);
    }

    /**
     * 更新系统信息
     *
     * @param systemInfoDto 系统信息dto
     * @return 更新结果
     */
    @PutMapping("/updateSystemInfo")
    public boolean updateSystemInfo(SystemInfoDto systemInfoDto) {
        return managementService.updateSystemInfo(systemInfoDto);
    }

    /**
     * 根据id删除系统信息
     *
     * @param id 系统信息id
     * @return 删除结果
     */
    @DeleteMapping("/deleteSystemInfoById")
    public boolean deleteSystemInfoById(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return managementService.deleteSystemInfoById(id);
    }

    /**
     * 添加接口信息
     *
     * @param interfaceDto 接口信息
     * @return 接口信息id
     */
    @PostMapping("/addInterface")
    public Long addInterface(InterfaceDto interfaceDto) {
        return managementService.addInterface(interfaceDto);
    }

    /**
     * 批量添加接口信息
     *
     * @param interfaceDtoList 接口信息list
     * @return 接口信息id们
     */
    @PostMapping("/addInterfaceList")
    public List<Long> addInterfaceList(List<InterfaceDto> interfaceDtoList) {
        return managementService.addInterface(interfaceDtoList);
    }

    /**
     * 更新接口信息
     *
     * @param interfaceDto 接口信息
     * @return 更新结果
     */
    @PutMapping("/updateInterface")
    public boolean updateInterface(InterfaceDto interfaceDto) {
        return managementService.updateInterface(interfaceDto);
    }

    /**
     * 根据id删除接口信息
     *
     * @param id 接口信息id
     * @return 删除结果
     */
    @DeleteMapping("/deleteInterfaceById")
    public boolean deleteInterfaceById(Long id) {
        if (id == null || id <= 0) {
            return false;
        }

        return managementService.deleteInterfaceById(id);
    }

    /**
     * 批量新增接口字段属性
     *
     * @param jsonFieldDtoList 接口字段属性list
     * @return 接口字段属性id们
     */
    @PostMapping("/addJsonFields")
    public List<Long> addJsonFields(List<JsonFieldDto> jsonFieldDtoList) {
        return managementService.addJsonFields(jsonFieldDtoList);
    }

    /**
     * 批量删除接口字段
     *
     * @param idList 接口字段id们
     * @return 删除结果
     */
    @DeleteMapping("/deleteJsonFieldsById")
    public boolean deleteJsonFieldsById(List<Long> idList) {
        return managementService.deleteJsonFieldsById(idList);
    }

    /**
     * 查询系统模块信息List
     *
     * @param nameLike 系统模块名称，资瓷模糊查询
     * @return 系统模块信息List
     */
    @GetMapping("/querySystemInfoList")
    public List<SystemInfo> querySystemInfoList(String nameLike) {
        return systemApiService.querySystemInfoPage(nameLike, DEFAULT_PAGE, DEFAULT_PAGE_SIZE).getContent();
    }

    /**
     * 查询接口信息List
     *
     * @param nameLike     名称
     * @param systemInfoId 系统信息id
     * @return 接口信息List
     */
    @GetMapping("/queryInterfaceList")
    public List<Interface> queryInterfaceList(String nameLike, Long systemInfoId) {
        return systemInfoId == null || systemInfoId <= 0 ? systemApiService.queryInterfacePage(nameLike, DEFAULT_PAGE, DEFAULT_PAGE_SIZE).getContent() : systemApiService.queryInterfaceBySystemInfoIdPage(nameLike, systemInfoId, DEFAULT_PAGE, DEFAULT_PAGE_SIZE).getContent();
    }

    /**
     * 查询所有字段属性定义
     *
     * @return 所有字段属性定义
     */
    @GetMapping("/queryAllFields")
    public List<JsonFieldDto> queryAllFields() {
        return systemApiService.queryAllFields();
    }

    /**
     * 根据接口id查询接口定义的字段
     *
     * @param interfaceId 接口id
     * @return 接口定义的字段
     */
    @GetMapping(value = "/queryRelateFieldsByInterfaceId", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JsonFieldDto> queryRelateFieldsByInterfaceId(Long interfaceId) {
        if (interfaceId == null || interfaceId <= 0) {
            throw new IllegalArgumentException("接口id不合法！");
        }
        return systemApiService.queryDeclaredFieldsByInterfaceId(interfaceId).parallelStream()
                .map(jsonField -> {
                    JsonFieldDto dto = EntityDeepCopyUtils.copyEntityProperties(jsonField, new JsonFieldDto());
                    dto.setEssential(systemApiService.isEssentialJsonField(jsonField.getId(), interfaceId) ? 1 : 0);
                    return dto;
                }).collect(Collectors.toList());
    }

    /**
     * 更新接口与接口参数的关联关系
     *
     * @param payload payload
     * @return <ul>
     * <li>Success:操作成功无异常</li>
     * <li>Fail:执行操作有异常</li>
     * </ul>
     * @see RelatePayload
     */
    @PutMapping("/updateRelate")
    public String updateRelate(@RequestBody RelatePayload payload) {
        if (payload.getId() == null || payload.getId() <= 0) {
            throw new IllegalArgumentException("接口id不合法！");
        }

        //先清空关联关系
        managementService.deleteRelateByInterfaceId(payload.getId());

        //添加新的关联关系
        if (!CollectionUtils.isEmpty(payload.getFieldList())) {
            return managementService.insertRelateByInterfaceIdBatch(payload.getId(), payload.getFieldList()) ? "Success" : "Fail";
        }

        return "Success";
    }

    /**
     * 批量关联字段属性到模块下的所有接口
     *
     * @param payload payload
     * @return <ul>
     * <li>Success:操作成功无异常</li>
     * <li>Fail:执行操作有异常</li>
     * </ul>
     * @see #updateRelate(RelatePayload) 更新接口与接口参数的关联关系
     * @see RelatePayload
     */
    @PutMapping("/relateModule")
    public String relateModule(@RequestBody RelatePayload payload) {
        if (payload.getId() == null || payload.getId() <= 0) {
            throw new IllegalArgumentException("系统模块id不合法！");
        }

        //批量新增
        Set<String> batchRelateResult = systemApiService.queryInterfaceBySystemInfoIdPage(null, payload.getId(), DEFAULT_PAGE, DEFAULT_PAGE_SIZE)
                .stream().parallel().map(anInterface -> updateRelate(new RelatePayload(anInterface.getId(), payload.getFieldList()))).collect(Collectors.toSet());

        //判断批量新增结果
        return batchRelateResult.contains("Fail") ? "Success" : "Fail";
    }
}
