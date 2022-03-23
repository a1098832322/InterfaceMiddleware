package com.zl.middleware.core.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 字段详情表
 */
@Data
@Accessors(chain = true)
public class JsonFieldDto {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 接口字段名称
     */
    private String name;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 接口字段类型枚举
     */
    private String type;

    /**
     * 接口字段描述/备注
     */
    private String remark;

    /**
     * 是否是必须要传的字段（1：是；0：否）
     */
    private Integer essential = 0;
}
