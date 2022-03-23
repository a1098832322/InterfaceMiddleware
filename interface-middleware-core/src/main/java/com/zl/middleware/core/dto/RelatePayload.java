package com.zl.middleware.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 消息接口关联关系Payload
 *
 * @author zl
 * @since 2021/12/16 16:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatePayload {

    private Long id;

    private List<JsonFieldDto> fieldList;
}
