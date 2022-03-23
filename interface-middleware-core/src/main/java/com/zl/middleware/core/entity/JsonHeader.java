package com.zl.middleware.core.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 配置的接口header表
 *
 * @author zl
 * @since 2021/11/24 15:14
 */
@Data
@Entity
@Table(name = "t_header")
@Accessors(chain = true)
public class JsonHeader implements Serializable {
    /**
     * 主键id
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * t_interface表id
     */
    @Column(name = "pid", nullable = false)
    private Long pid;

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
     * 接口字段描述/备注
     */
    @Column(name = "remark")
    private String remark;
}
