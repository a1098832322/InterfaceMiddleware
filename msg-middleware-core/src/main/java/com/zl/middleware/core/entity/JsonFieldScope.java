package com.zl.middleware.core.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 字段作用域表
 */
@Data
@Entity
@Table(name = "t_scope")
@Accessors(chain = true)
public class JsonFieldScope implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 字段id
     */
    @Column(name = "field_id", nullable = false)
    private Long fieldId;

    /**
     * 接口id
     */
    @Column(name = "interface_id", nullable = false)
    private Long interfaceId;
    /**
     * 是否是必须要传的字段（1：是；0：否）
     */
    @Column(name = "essential", nullable = false)
    private Integer essential;

}
