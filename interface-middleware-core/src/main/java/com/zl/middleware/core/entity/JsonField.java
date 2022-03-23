package com.zl.middleware.core.entity;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 字段详情表
 */
@Data
@Entity
@Table(name = "t_field")
@Accessors(chain = true)
public class JsonField implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 接口字段名称
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 默认值(如果有)
     */
    @Column(name = "default_value")
    private String defaultValue;

    /**
     * 接口字段类型枚举
     */
    @Column(name = "type", nullable = false)
    private String type;

    /**
     * 接口字段描述/备注
     */
    @Column(name = "remark")
    private String remark;
}
