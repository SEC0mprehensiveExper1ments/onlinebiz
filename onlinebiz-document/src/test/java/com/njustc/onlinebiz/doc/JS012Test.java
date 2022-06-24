package com.njustc.onlinebiz.doc;

import com.njustc.onlinebiz.doc.model.JS012;
import com.njustc.onlinebiz.doc.service.DocServiceJS012;
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



        System.out.println(docServiceJS012.fill("1111", newJson));
    }

}
