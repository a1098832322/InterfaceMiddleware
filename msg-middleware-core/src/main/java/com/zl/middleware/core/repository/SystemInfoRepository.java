package com.zl.middleware.core.repository;

import com.zl.middleware.core.entity.SystemInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
public interface SystemInfoRepository extends JpaRepository<SystemInfo, Long>, JpaSpecificationExecutor<SystemInfo> {

    Page<SystemInfo> findSystemInfosByNameLike(String nameLike, Pageable pageable);

    SystemInfo findSystemInfoByKey(String key);
}