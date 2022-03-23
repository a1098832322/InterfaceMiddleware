package com.zl.middleware.core.repository;

import com.zl.middleware.core.entity.JsonFieldScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JsonFieldScopeRepository extends JpaRepository<JsonFieldScope, Long>, JpaSpecificationExecutor<JsonFieldScope> {
    /**
     * 根據字段id查询关联的接口id
     *
     * @param fieldId 字段id
     * @return 接口id们
     */
    @Query("select interfaceId from JsonFieldScope where fieldId = ?1")
    List<Long> findInterfaceIdByFieldId(long fieldId);

    /**
     * 查询当前字段在当前接口下是否是必须字段
     *
     * @param fieldId     字段id
     * @param interfaceId 接口id
     * @return true/false 是/否
     */
    @Query("select essential from JsonFieldScope where fieldId = ?1 and interfaceId = ?2")
    Integer queryEssentialByFieldIdAndInterfaceId(long fieldId, long interfaceId);

    /**
     * 根据接口id删除关联关系
     *
     * @param interfaceId 接口id
     */
    void deleteByInterfaceId(long interfaceId);

    /**
     * 根据字段id删除关联关系
     *
     * @param fieldId 字段id
     */
    void deleteByFieldId(long fieldId);
}