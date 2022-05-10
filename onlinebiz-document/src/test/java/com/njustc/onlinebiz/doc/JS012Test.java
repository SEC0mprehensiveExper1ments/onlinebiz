package com.njustc.onlinebiz.doc;

import com.alibaba.fastjson2.JSON;
import com.njustc.onlinebiz.doc.domain.JS012;
import com.njustc.onlinebiz.doc.service.DocServiceJS012;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class JS012Test {

    @Autowired
    DocServiceJS012 docServiceJS012;

    @Test
    void testFillJS012Success() {
        JS012 newJson = new JS012()
                .setInputRuanJianMingChen("某在线表格管理平台")
                .setInputBanBenHao("XXXX-2012")
                .setInputShenBaoDanWei("江苏省某高科技公司")
                .setInputQiShiShiJian0Nian("2022")
                .setInputQiShiShiJian0Yue("12")
                .setInputQiShiShiJian0Ri("30")
                .setInputYuJiShiJian0Nian("2022")
                .setInputYuJiShiJian0Yue("12")
                .setInputYuJiShiJian0Ri("30")
                .setInputZhuCeRen("高校评审组")
                .setInputShiJiShiJian0Nian("2022")
                .setInputShiJiShiJian0Yue("12")
                .setInputShiJiShiJian0Ri("20")
                .setInputQueRen011("确认")
                .setInputQueRen012("确认")
                .setInputQueRen013("确认")
                .setInputQueRen021("确认")
                .setInputQueRen022("确认")
                .setInputQueRen031("确认")
                .setInputQueRen032("确认")
                .setInputQueRen041("确认")
                .setInputQueRen051("")
                .setInputQueRen052("")
                .setInputQueRen053("")
                .setInputQueRen054("")
                .setInputQueRen055("")
                .setInputQueRen061("")
                .setInputQueRen071("")
                .setInputQueRen072("")
                .setInputQueRen073("")
                .setInputQueRen081("")
                .setInputQueRen082("")
                .setInputQueRen083("")
                .setInputQueRen084("")
                .setInputQueRen091("")
                .setInputQueRen092("")
                .setInputQueRen093("");
//                .setInputQueRen0101("")
//                .setInputQueRen0102("")
//                .setInputQueRen0103("")
//                .setInputQueRen0104("")
//                .setInputQueRen0105("")
//                .setInputQueRen0106("")
//                .setInputQueRen0107("")
//                .setInputQueRen0108("");

        System.out.println(JSON.toJSONString(newJson));
        Assertions.assertTrue(docServiceJS012.fill(newJson));
    }

}
