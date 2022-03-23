package com.zl.middleware.core.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 接口信息表
 */
@Data
@Accessors(chain = true)
public class InterfaceDto   {
    /**
     * 系统信息表id
     */
    private Long systemInfoId;

    /**
     * 给接口名起的唯一性名称(英文，可以带数字，但是首字母不能为数字)
     */
    private String key;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 请求方式枚举
     */
    private String method;

    /**
     * 请求头要求
     */
    private String header;

    /**
     * 是否启用（1：启用；0：禁用）
     */
    private Integer enable;

}
