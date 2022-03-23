package com.zl.middleware.core.component;

import com.zl.middleware.core.entity.JsonHeader;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Http header 渲染器
 *
 * @author zl
 * @since 2021/11/24 15:33
 */
public class HeaderRender {
    /**
     * 渲染header
     *
     * @param headers      header模板定义
     * @param headersValue header值map
     * @return HttpHeaders
     */
    public HttpHeaders render(List<JsonHeader> headers, Map<String, String> headersValue) {
        HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> resultValue = new HashMap<>(headersValue.size() * 2);

        for (JsonHeader header : headers) {
            Optional.ofNullable(isSystemDefJsonField(header) ? header.getDefaultValue() : headersValue.getOrDefault(header.getName(), null)).ifPresent(value -> {
                resultValue.put(header.getName(), value);
            });
        }

        //设置header
        if (!resultValue.isEmpty()) {
            resultValue.forEach(httpHeaders::add);
            return httpHeaders;
        }

        return null;
    }

    /**
     * 是否是系统保留字段
     *
     * @param jsonHeader http header
     * @return true/false 是/否
     */
    private boolean isSystemDefJsonField(JsonHeader jsonHeader) {
        return 0 == jsonHeader.getPid();
    }
}
