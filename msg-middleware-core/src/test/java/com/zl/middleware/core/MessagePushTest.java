package com.zl.middleware.core;

import com.alibaba.fastjson.JSON;
import com.zl.middleware.core.component.DirectArrayMessageSender;
import com.zl.middleware.core.component.DirectSingleMessageSender;
import com.zl.middleware.core.utils.HttpRequestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;

import java.util.*;

/**
 * 消息推送测试
 *
 * @author zl
 * @since 2021/11/18 16:48
 */
@SpringBootTest
public class MessagePushTest {

    @Autowired
    private HttpRequestUtil httpRequestUtil;

    @Autowired
    private DirectArrayMessageSender arrayMessageSender;

    @Autowired
    private DirectSingleMessageSender singleMessageSender;

    @Test
    void test() {
        String json = JSON.toJSONString(preparedValue());
        //测试消息推送Controller
        String url = "http://127.0.0.1:10030/message/push";

        String resp = httpRequestUtil.doExchange(url, json, null, HttpMethod.POST);
        System.out.println(resp);
    }

    /**
     * 推送JSON Object测试
     */
    @Test
    void pushObjectTest() {
        System.out.println("Push Object Response:");
        singleMessageSender.multithreadingSendMessage(preparedValue()).forEach(System.out::println);
    }

    /**
     * 推送JSON Array测试
     */
    @Test
    void pushArrayTest() {
        List<Map<String, Object>> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            list.add(preparedValue());
        }

        System.out.println("Push Array Response:");
        arrayMessageSender.multithreadingSendMessage(list).forEach(System.out::println);
    }

    private Map<String, Object> preparedValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("stuName", "張三");
        map.put("hasMoney", true);
        map.put("stuMoney", 65535.50D);
        map.put("currentTime", System.currentTimeMillis());

        //添加请求接口的key
        List<String> keys = new ArrayList<>();
        keys.add("TEST_INTERFACE");
        map.put("interfaceKeys", keys);

        return map;
    }
}
