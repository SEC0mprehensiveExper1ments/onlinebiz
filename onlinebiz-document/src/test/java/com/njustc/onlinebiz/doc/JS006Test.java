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
        modifications.add(new Modification("1.0", date1, Modification.Method.A, "1", "1"));
        modifications.add(new Modification("1.1", date2, Modification.Method.M, "2", "2"));
        modifications.add(new Modification("1.2", date3, Modification.Method.D, "3", "3"));
        Schedule zhiDingJiHua= new Schedule("1", date1, date2);
        Schedule sheJiCeShi= new Schedule("2", date1, date2);
        Schedule zhiXingCeShi= new Schedule("3", date1, date2);
        Schedule pingGuCeShi= new Schedule("4", date1, date2);
        JS006 newJson=new JS006()
                .setInputBanBenHao("1.2")
                .setWenDangXiuGaiJiLu(modifications)
                .setInputBiaoShi("无")
                .setInputXiTongGaiShu("无")
                .setInputWenDangGaiShu("无")
                .setInputJiXian("无")
                .setInputYingJian("无")
                .setInputRuanJian("无")
                .setInputQiTa("无")
                .setInputCanYuZuZhi("无")
                .setInputRenYuan("无")
                .setInputCeShiJiBie("无")
                .setInputCeShiLeiBie("无")
                .setInputYiBanCeShiTiaoJian("无")
                .setInputJiHuaZhiXingDeCeShi("无")
                .setInputCeShiYongLi("无")
                .setZhiDingJiHua(zhiDingJiHua)
                .setSheJiCeShi(sheJiCeShi)
                .setZhiXingCeShi(zhiXingCeShi)
                .setPingGuCeShi(pingGuCeShi)
                .setInputXuQiuKeZhuiZongXing("无");
        System.out.println(docServiceJS006.fill("1111",newJson));
    }
}
