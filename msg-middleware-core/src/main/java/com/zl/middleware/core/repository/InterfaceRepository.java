package com.zl.middleware.core.repository;

import com.zl.middleware.core.entity.Interface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
public interface InterfaceRepository extends JpaRepository<Interface, Long>, JpaSpecificationExecutor<Interface> {

    Page<Interface> findInterfacesByNameLike(String nameLike, Pageable pageable);

    Page<Interface> findInterfacesByNameLikeAndSystemInfoIdEquals(String nameLike, long systemInfoId, Pageable pageable);

    Page<Interface> findInterfacesBySystemInfoIdEquals(long systemInfoId, Pageable pageable);

    Interface findInterfaceByKey(String key);
}