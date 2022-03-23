package com.zl.middleware.core.controller;

import com.zl.middleware.core.component.DirectArrayMessageSender;
import com.zl.middleware.core.component.DirectSingleMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 消息相关服务
 *
 * @author zl
 * @since 2021/11/18 16:35
 */
@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {
    /**
     * JSON Object消息推送组件
     */
    @Autowired
    private DirectSingleMessageSender directMessageSender;

    /**
     * JSON Array消息推送组件
     */
    @Autowired
    private DirectArrayMessageSender directArrayMessageSender;

    /**
     * 向目标平台的接口推送数据
     *
     * @param params json参数
     * @return 推送结果集合
     */
    @PostMapping(value = "/pushObject", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Object> pushObject(@RequestBody Map<String, Object> params) {
        return directMessageSender.multithreadingSendMessage(params);
    }

    /**
     * 向目标平台的接口推送数据
     *
     * @param params json参数
     * @return 推送结果集合
     */
    @PostMapping(value = "/pushArray", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Object> pushArray(@RequestBody List<Map<String, Object>> params) {
        return directArrayMessageSender.multithreadingSendMessage(params);
    }

}
