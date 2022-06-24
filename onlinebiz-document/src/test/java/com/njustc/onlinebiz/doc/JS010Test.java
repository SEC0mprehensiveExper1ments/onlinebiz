package com.njustc.onlinebiz.doc;

import com.njustc.onlinebiz.doc.model.JS010;
import com.njustc.onlinebiz.doc.service.DocServiceJS010;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JS010Test {

    @Autowired
    DocServiceJS010 docServiceJS010;

    @Test
    void testFillJS010Success() {
        JS010 newJson = new JS010()
                .setInputRuanJianMingChen("某在线表格管理平台")
                .setInputWeiTuoDanWei("江苏省某科技有限公司")
                .setInputJianChaJieGuo01("不合格")
                .setInputJianChaJieGuo02("不合格")
                .setInputJianChaJieGuo03("不合格")
                .setInputJianChaJieGuo04("不合格")
                .setInputJianChaJieGuo05("不合格")
                .setInputJianChaJieGuo06("不合格")
                .setInputJianChaJieGuo07("不合格")
                .setInputJianChaJieGuo08("不合格")
                .setInputJianChaJieGuo09("不合格")
                .setInputJianChaJieGuo010("不合格")
                .setInputJianChaJieGuo0111("不合格")
                .setInputJianChaJieGuo0112("不合格")
                .setInputJianChaJieGuo0113("不合格")
                .setInputJianChaJieGuo012("不合格");

        System.out.println(docServiceJS010.fill("1111", newJson));
    }
}
