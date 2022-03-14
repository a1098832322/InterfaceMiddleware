package com.zl.middleware.core;

import com.zl.middleware.core.component.JsonRender;
import com.zl.middleware.core.component.AbstractSingleMessageSender;
import com.zl.middleware.core.dto.MetaInfo;
import com.zl.middleware.core.entity.JsonField;
import com.zl.middleware.core.repository.InterfaceRepository;
import com.zl.middleware.core.repository.JsonFieldRepository;
import com.zl.middleware.core.service.SystemApiService;
import com.zl.middleware.core.utils.EntityToMapUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
class MiddlewareApplicationTests {

    @Autowired
    private JsonFieldRepository jsonFieldRepository;

    @Autowired
    private SystemApiService systemApiService;

    @Test
    void contextLoads() {
        long interfaceId = 13L;

        List<JsonField> fieldList = jsonFieldRepository.findJsonFieldsByPid(interfaceId);
        List<JsonField> essentialList = new ArrayList<>();

        String allFields = fieldList.stream().map(jsonField -> {
            if (systemApiService.isEssentialJsonField(jsonField.getId(), interfaceId)) {
                essentialList.add(jsonField);
            }

            return jsonField.getName();
        }).collect(Collectors.joining(","));

        System.out.println("All Fields: " + allFields);

        String essentialFields = essentialList.stream().map(JsonField::getName).collect(Collectors.joining(","));
        System.out.println("Essential Fields: " + essentialFields);
    }
}
