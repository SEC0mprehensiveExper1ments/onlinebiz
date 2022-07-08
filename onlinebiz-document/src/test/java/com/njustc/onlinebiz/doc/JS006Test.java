package com.njustc.onlinebiz.doc;

import com.njustc.onlinebiz.common.model.test.scheme.Modification;
import com.njustc.onlinebiz.common.model.test.scheme.Schedule;
import com.njustc.onlinebiz.doc.model.JS006;
import com.njustc.onlinebiz.doc.service.DocServiceJS006;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class JS006Test {

    @Autowired
    DocServiceJS006 docServiceJS006;
    @Test
    void testFillJS006Success() {
        Date date1 = new Date();
        Date date2 = new Date();
        Date date3 = new Date();
        List<Modification> modifications = new ArrayList<>();
        modifications.add(new Modification("1.0", date1.toString(), Modification.Method.A, "1", "1"));
        modifications.add(new Modification("1.1", date2.toString(), Modification.Method.M, "2", "2"));
        modifications.add(new Modification("1.2", date3.toString(), Modification.Method.D, "3", "3"));
        Schedule zhiDingJiHua= new Schedule("1", date1.toString(), date2.toString());
        Schedule sheJiCeShi= new Schedule("2", date1.toString(), date2.toString());
        Schedule zhiXingCeShi= new Schedule("3", date1.toString(), date2.toString());
        Schedule pingGuCeShi= new Schedule("4", date1.toString(), date2.toString());
        JS006 newJson=new JS006()
                .setInputBanBenHao("1.2")
                .setWenDangXiuGaiJiLu(modifications)
                .setInputBiaoShi("无1")
                .setInputXiTongGaiShu("无2")
                .setInputWenDangGaiShu("无3")
                .setInputJiXian("无4")
                .setInputYingJian("无5")
                .setInputRuanJian("无6")
                .setInputQiTa("无7")
                .setInputCanYuZuZhi("无8")
                .setInputRenYuan("无9")
                .setInputCeShiJiBie("无0")
                .setInputCeShiLeiBie("无11")
                .setInputYiBanCeShiTiaoJian("无12")
                .setInputJiHuaZhiXingDeCeShi("无13")
                .setInputCeShiYongLi("无14")
                .setInputYuJiGongZuoRi("10")
                .setZhiDingJiHua(zhiDingJiHua)
                .setSheJiCeShi(sheJiCeShi)
                .setZhiXingCeShi(zhiXingCeShi)
                .setPingGuCeShi(pingGuCeShi)
                .setInputXuQiuKeZhuiZongXing("无15");
        System.out.println(docServiceJS006.fill("1111",newJson));
    }
}
