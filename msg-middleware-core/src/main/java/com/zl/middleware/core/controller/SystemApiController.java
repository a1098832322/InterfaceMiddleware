package com.zl.middleware.core.controller;

import com.zl.middleware.core.entity.Interface;
import com.zl.middleware.core.entity.JsonField;
import com.zl.middleware.core.entity.SystemInfo;
import com.zl.middleware.core.service.SystemApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 目标对接接口查询Controller
 *
 * @author zl
 * @since 2021/11/18 16:14
 */
@RestController
@RequestMapping("/systemApi")
public class SystemApiController {

    @Autowired
    private SystemApiService systemApiService;

    /**
     * 根据名称查询系统信息数据分页
     *
     * @param nameLike    名称
     * @param currentPage 当前页码
     * @param pageSize    每页显示条数
     * @return 系统信息数据分页
     */
    @GetMapping(value = "/querySystemInfoPage", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<SystemInfo> querySystemInfoPage(String nameLike, Integer currentPage, Integer pageSize) {
        return systemApiService.querySystemInfoPage(nameLike, legalCurrentPage(currentPage), legalPageSize(pageSize));
    }

    /**
     * 根据名称查询接口数据分页
     *
     * @param currentPage  当前页码
     * @param pageSize     每页显示条数
     * @param nameLike     名称
     * @param systemInfoId 系统信息id
     * @return 接口数据分页
     */
    @GetMapping(value = "/queryInterfaceBySystemInfoIdPage", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Interface> queryInterfaceBySystemInfoIdPage(String nameLike, Long systemInfoId, Integer currentPage, Integer pageSize) {
        return systemInfoId == null || systemInfoId <= 0 ?
                systemApiService.queryInterfacePage(nameLike, legalCurrentPage(currentPage), legalPageSize(pageSize)) :
                systemApiService.queryInterfaceBySystemInfoIdPage(nameLike, systemInfoId, legalCurrentPage(currentPage), legalPageSize(pageSize));
    }

    /**
     * 根据接口id查询接口定义的字段
     *
     * @param interfaceId 接口id
     * @return 接口定义的字段
     */
    @GetMapping(value = "/queryDeclaredFieldsByInterfaceId", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JsonField> queryDeclaredFieldsByInterfaceId(Long interfaceId) {
        if (interfaceId == null || interfaceId <= 0) {
            throw new IllegalArgumentException("接口id不合法！");
        }
        return systemApiService.queryDeclaredFieldsByInterfaceId(interfaceId);
    }

    /**
     * 合法化页码
     *
     * @param currentPage 页码
     * @return 合法化的页码
     */
    private Integer legalCurrentPage(Integer currentPage) {
        if (currentPage == null || currentPage <= 0) {
            currentPage = 1;
        }

        return currentPage;
    }

    /**
     * 合法化每页显示条数
     *
     * @param pageSize 每页显示条数
     * @return 合法化的每页显示条数
     */
    private Integer legalPageSize(Integer pageSize) {
        if (pageSize == null || pageSize <= 0) {
            pageSize = 15;
        }

        return pageSize;
    }
}
