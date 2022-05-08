package com.njustc.onlinebiz.doc;


import com.alibaba.fastjson2.JSON;
import com.njustc.onlinebiz.doc.domain.JS004;
import com.njustc.onlinebiz.doc.domain.JS005;
import com.njustc.onlinebiz.doc.service.DocServiceJS004;
import com.njustc.onlinebiz.doc.service.DocServiceJS005;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JS005Test {

    @Autowired
    DocServiceJS005 docServiceJS005;

    @Test
    void testFillJS005Success() {
        JS005 newJson = new JS005()
                .setInputJiaFang("江苏省某科技企业公司")
                .setInputWeiTuoXiangMu("江苏省在线某表格管理软件平台");

        System.out.println(JSON.toJSONString(newJson));
        Assertions.assertTrue(docServiceJS005.fill(newJson));
    }
}
