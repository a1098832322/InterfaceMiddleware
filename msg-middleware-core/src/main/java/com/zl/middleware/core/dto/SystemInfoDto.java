package com.zl.middleware.core.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统信息Model
 */
@Data
@Accessors(chain = true)
public class SystemInfoDto {

    /**
     * 给当前单位起的唯一名称(英文，可以带数字。但首字母不能为数字)
     */
    private String key;

    /**
     * 需要对接的系统名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 企业的AppID(可能有)
     */
    private String appId;

    /**
     * 企业的密钥(可能有)
     */
    private String appSecretKey;

}
