package com.njustc.onlinebiz.doc;

import com.alibaba.fastjson2.JSON;
import com.njustc.onlinebiz.doc.domain.JS013;
import com.njustc.onlinebiz.doc.service.DocServiceJS013;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JS013Test {

    @Autowired
    DocServiceJS013 docServiceJS013;

    @Test
    void testFillJS005Success() {
        JS013 newJson = new JS013()
                .setInputRuanJianMingChen("江苏省某在线表格管理平台")
                .setInputBanBenHao("XXXX-2011")
                .setInputXiangMuBianHao("NST-04-JS013-2011")
                .setInputCeShiLeiBie("普通测试")
                .setInputTongGuo01("")
                .setInputTongGuo02("")
                .setInputTongGuo03("")
                .setInputTongGuo04("通过")
                .setInputTongGuo05("")
                .setInputTongGuo06("通过")
                .setInputTongGuo07("")
                .setInputTongGuo08("通过")
                .setInputBuTongGuo01("不通过，原因不明")
                .setInputBuTongGuo02("不通过，原因不明")
                .setInputBuTongGuo03("不通过，原因不明")
                .setInputBuTongGuo04("")
                .setInputBuTongGuo05("不通过，原因不明")
                .setInputBuTongGuo06("")
                .setInputBuTongGuo07("不通过，原因不明")
                .setInputBuTongGuo08("");

        System.out.println(JSON.toJSONString(newJson));
        Assertions.assertTrue(docServiceJS013.fill(newJson));
    }
}
