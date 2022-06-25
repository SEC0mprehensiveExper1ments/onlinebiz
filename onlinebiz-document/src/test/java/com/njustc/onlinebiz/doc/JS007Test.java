package com.njustc.onlinebiz.doc;

import com.njustc.onlinebiz.common.model.test.report.SoftwareEnvironment;
import com.njustc.onlinebiz.common.model.test.report.TestContent;
import com.njustc.onlinebiz.common.model.test.testrecord.TestRecordList;
import com.njustc.onlinebiz.doc.model.JS007;
import com.njustc.onlinebiz.doc.service.DocServiceJS007;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class JS007Test {

    @Autowired
    DocServiceJS007 docServiceJS007;

    @Test
    void testFillJS007Success() {

        List<SoftwareEnvironment> softwareEnvironments = new ArrayList<>();
        softwareEnvironments.add(new SoftwareEnvironment("软件", "1", "1"));
        softwareEnvironments.add(new SoftwareEnvironment("软件", "2", "2"));
        softwareEnvironments.add(new SoftwareEnvironment("软件", "3", "3"));
        softwareEnvironments.add(new SoftwareEnvironment("辅助工具", "1", "1"));
        softwareEnvironments.add(new SoftwareEnvironment("开发工具", "1", "1"));
        softwareEnvironments.add(new SoftwareEnvironment("被测试样品", "1", "1"));

        List<String> test=new ArrayList<>();
        test.add("1");
        test.add("2");
        test.add("3");

        List<TestContent> testContents = new ArrayList<>();
        testContents.add(new TestContent("1","1","1"));
        testContents.add(new TestContent("2","2","2"));
        testContents.add(new TestContent("3","3","3"));

        List<TestRecordList.TestRecord> testRecords = new ArrayList<>();
        testRecords.add(new TestRecordList.TestRecord("你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1"));
        testRecords.add(new TestRecordList.TestRecord("你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1"));
        testRecords.add(new TestRecordList.TestRecord("你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1"));
        testRecords.add(new TestRecordList.TestRecord("你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1"));
        testRecords.add(new TestRecordList.TestRecord("你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1"));
        testRecords.add(new TestRecordList.TestRecord("你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1"));


        JS007 newJson=new JS007()
                .setInputBaoGaoBianHao("1111")
                .setInputRuanJianMingCheng("无1")
                .setInputBanBenHao("1.2")
                .setInputWeiTuoDanWei("无2")
                .setInputCeShiLeiBie("无3")
                .setInputBaoGaoRiQiNian("2022")
                .setInputBaoGaoRiQiYue("6")
                .setInputBaoGaoRiQiRi("25")
                .setInputXiangMuBianHao("无4")
                .setInputYangPinMingCheng("无5")
                .setInputBanBenXingHao("无6")
                .setInputLaiYangRiQiNian("2022")
                .setInputLaiYangRiQiYue("6")
                .setInputLaiYangRiQiRi("25")
                .setInputCeShiKaiShiRiQiNian("2022")
                .setInputCeShiKaiShiRiQiYue("6")
                .setInputCeShiKaiShiRiQiRi("25")
                .setInputCeShiJieShuRiQiNian("2022")
                .setInputCeShiJieShuRiQiYue("6")
                .setInputCeShiJieShuRiQiRi("25")
                .setInputYangPinZhuangTai("无7")
                .setInputCeShiYiJu("无8")
                .setInputYangPinQingDan("无9")
                .setInputCeShiJieLun("无10")
                .setInputZhuCeRen("无11")
                .setInputZhuCeRiQiNian("2022")
                .setInputZhuCeRiQiYue("6")
                .setInputZhuCeRiQiRi("25")
                .setInputShenHeRen("无12")
                .setInputShenHeRiQiNian("2022")
                .setInputShenHeRiQiYue("6")
                .setInputShenHeRiQiRi("25")
                .setInputPiZhunRen("无13")
                .setInputPiZhunRiQiNian("2022")
                .setInputPiZhunRiQiYue("6")
                .setInputPiZhunRiQiRi("25")
                .setInputDianHua("无14")
                .setInputChuanZhen("无15")
                .setInputDiZhi("无16")
                .setInputYouBian("无17")
                .setInputLianXiRen("无18")
                .setInputEmail("无19")
                .setInputYingJianLeiBie("无20")
                .setInputYingJianMingCheng("无21")
                .setInputYingJianPeiZhi("无22")
                .setInputYingJianShuLiang("无23")
                .setInputCaoZuoXiTongMingCheng("无24")
                .setInputCaoZuoXiTongBanBen("无25")
                .setInputRuanJianHuanJing(softwareEnvironments)
                .setInputWangLuoHuanJing("无26")
                .setInputTestBases(test)
                .setInputCanKaoZiLiao(test)
                .setInputGongNengXingCeShi(testContents)
                .setInputXiaoLvCeShi(testContents)
                .setInputKeYiZhiXingCeShi(testContents)
                .setInputYiYongXingCeShi(testContents)
                .setInputKeKaoXingCeShi(testContents)
                .setInputKeWeiHuXingCeShi(testContents)
                .setInputCeShiZhiXingJiLu(testRecords);
        System.out.println(docServiceJS007.fill("1111",newJson));
    }
}
