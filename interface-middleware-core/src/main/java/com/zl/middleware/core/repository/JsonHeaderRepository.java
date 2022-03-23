package com.zl.middleware.core.repository;

import com.zl.middleware.core.entity.JsonHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * header表Repository
 *
 * @author zl
 * @since 2021/11/24 15:15
 */
@Transactional(rollbackFor = Exception.class)
public interface JsonHeaderRepository extends JpaRepository<JsonHeader, Long>, JpaSpecificationExecutor<JsonHeader> {

    List<JsonHeader> findJsonHeadersByPid(long pid);
}
