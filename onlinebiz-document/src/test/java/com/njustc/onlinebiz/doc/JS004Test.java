package com.njustc.onlinebiz.doc;


import com.alibaba.fastjson2.JSON;
import com.njustc.onlinebiz.doc.model.JS004;
import com.njustc.onlinebiz.doc.service.DocServiceJS004;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JS004Test {

    @Autowired
    DocServiceJS004 docServiceJS004;

    @Test
    void testFillJS004Success() {
        JS004 newJson = new JS004()
                .setInputXiangMuMingChen("南京大学国家计算机重点实验室科研项目——针对计算机软件理论的研究")
                .setInputJiaFang("南京大学计算机软件新技术国家重点实验室下属X实验室X实验室X实验室")
                .setInputYiFang("南京大学国家计算机软件新技术国家重点X实验室")
                .setInputQianDingDiDian("江苏省南京市栖霞区仙林街道南京大学仙林校区计算机系楼")
                .setInputQianDingRiQi0Nian("2022")
                .setInputQianDingRiQi0Yue("4")
                .setInputQianDingRiQi0Ri("29")
                .setInputShouCeRuanJian("南京大学计算机软件新技术国家重点项目软件")
                .setInputCeShiFeiYong("三万元整")
                .setInputCeShiFeiYong0Yuan("30000")
                .setInputLvXingQiXian("100")
                .setInputZhengGaiCishu("100")
                .setInputZhengGaiTianShu("100")
                .setInputJiaFang0ShouQuanDaiBiao("负责人")
                .setInputJiaFang0QianZhangRiQi("2022年4月30日")
                .setInputJiaFang0LianXiRen("南京大学计算机软件新技术国家重点实验室负责人\n")
                .setInputJiaFang0TongXunDiZhi("江苏省南京市栖霞区仙林大道169号南京大学仙林校区计算机系楼")
                .setInputJiaFang0DianHua("18822226666")
                .setInputJiaFang0ChuanZhen("6261627")
                .setInputJiaFang0KaiHuYinHang("中国工商股份有限公司汉口路分理处")
                .setInputJiaFang0ZhangHao("4301011309001041656")
                .setInputJiaFang0YouBian("037000")
                .setInputYiFang0ShouQuanDaiBiao("负责人")
                .setInputYiFang0QianZhangRiQi("2022年4月30日")
                .setInputYiFang0LianXiRen("南京大学计算机软件新技术国家重点实验室负责人")
                .setInputYiFang0TongXunDiZhi("江苏省南京市栖霞区仙林大道169号南京大学仙林校区计算机系楼")
                .setInputYiFang0DianHua("18822226666")
                .setInputYiFang0ChuanZhen("6261627")
                .setInputYiFang0HuMing("南京大学")
                .setInputYiFang0KaiHuYinHang("中国工商股份有限公司汉口路分理处")
                .setInputYiFang0ZhangHao("4301011309001041656")
                .setInputYiFang0YouBian("037000");

        System.out.println(JSON.toJSONString(newJson));
        String JS004URL = docServiceJS004.fill("1111", newJson);
        System.out.println(JS004URL);
        Assertions.assertTrue(JS004URL.startsWith("https://"));
    }
}
