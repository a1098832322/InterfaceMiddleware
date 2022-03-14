package com.zl.middleware.core.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 接口信息表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "t_interface")
public class Interface implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 系统信息表id
     */
    @Column(name = "system_info_id", nullable = false)
    private Long systemInfoId;
    /**
     * 给接口名起的唯一性名称(英文，可以带数字，但是首字母不能为数字)
     */
    @Column(name = "key", nullable = false)
    private String key;

    /**
     * 接口名称
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 接口地址
     */
    @Column(name = "url", nullable = false)
    private String url;

    /**
     * 接口描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 请求方式枚举
     */
    @Column(name = "method", nullable = false)
    private String method;

    /**
     * 请求头要求
     */
    @Column(name = "header", nullable = false)
    private String header;

    /**
     * 是否启用（1：启用；0：禁用）
     */
    @Column(name = "enable", nullable = false)
    private Integer enable;

}
