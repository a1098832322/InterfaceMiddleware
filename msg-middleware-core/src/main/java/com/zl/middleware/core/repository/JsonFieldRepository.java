package com.zl.middleware.core.repository;

import com.zl.middleware.core.entity.JsonField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = Exception.class)
public interface JsonFieldRepository extends JpaRepository<JsonField, Long>, JpaSpecificationExecutor<JsonField> {
    /**
     * 根据接口id查询接口所需字段
     *
     * @param pid 接口id
     * @return 对接接口所需字段定义
     */
    @Query(nativeQuery = true, value = "select * from t_field where id in (select field_id from t_scope where interface_id = ?1)")
    List<JsonField> findJsonFieldsByPid(long pid);

    /**
     * 查询系统保留/预定义的字段
     *
     * @return 系统保留/预定义的字段
     */
    @Query(nativeQuery = true, value = "select * from t_field where id in (select field_id from t_scope where interface_id = 0)")
    List<JsonField> findSystemJsonFields();
}