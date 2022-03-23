package com.zl.middleware.core.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 系统信息表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "t_system_info")
public class SystemInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 给当前单位起的唯一名称(英文，可以带数字。但首字母不能为数字)
     */
    @Column(name = "key", nullable = false)
    private String key;

    /**
     * 需要对接的系统名称
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
    /**
     * 企业的AppID(可能有)
     */
    @Column(name = "app_id")
    private String appId;
    /**
     * 企业的密钥(可能有)
     */
    @Column(name = "app_secret_key")
    private String appSecretKey;

}
