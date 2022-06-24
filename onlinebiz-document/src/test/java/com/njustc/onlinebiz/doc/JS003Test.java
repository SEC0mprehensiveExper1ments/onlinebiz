package com.njustc.onlinebiz.doc;


import com.njustc.onlinebiz.doc.model.JS003.JS003;
import com.njustc.onlinebiz.doc.model.JS003.GongNeng;
import com.njustc.onlinebiz.doc.model.JS003.ZiGongNeng;
import com.njustc.onlinebiz.doc.service.DocServiceJS003;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class JS003Test {

    @Autowired
    DocServiceJS003 docServiceJS003;

    @Test
    void testFillJS003Success() {
        ZiGongNeng ziGongNeng1 = new ZiGongNeng("子功能1", "说明1");
        ZiGongNeng ziGongNeng2 = new ZiGongNeng("子功能2", "说明2");
        ZiGongNeng ziGongNeng3 = new ZiGongNeng("子功能3", "说明3");
        ZiGongNeng ziGongNeng4 = new ZiGongNeng("子功能4", "说明4");
        ZiGongNeng ziGongNeng5 = new ZiGongNeng("子功能5", "说明5");
        ZiGongNeng ziGongNeng6 = new ZiGongNeng("子功能6", "说明6");
        ZiGongNeng ziGongNeng7 = new ZiGongNeng("子功能7", "说明7");
        List<ZiGongNeng> ziGongNengs1 = new ArrayList<>();
        ziGongNengs1.add(ziGongNeng1);
        ziGongNengs1.add(ziGongNeng2);
        ziGongNengs1.add(ziGongNeng3);
        ziGongNengs1.add(ziGongNeng4);
        List<ZiGongNeng> ziGongNengs2 = new ArrayList<>();
        ziGongNengs2.add(ziGongNeng1);
        ziGongNengs2.add(ziGongNeng2);
        ziGongNengs2.add(ziGongNeng3);
        ziGongNengs2.add(ziGongNeng4);
        ziGongNengs2.add(ziGongNeng5);
        ziGongNengs2.add(ziGongNeng6);
        ziGongNengs2.add(ziGongNeng7);

        GongNeng gongNeng1 = new GongNeng("功能一", ziGongNengs1);
        GongNeng gongNeng2 = new GongNeng("功能二", ziGongNengs2);

        List<GongNeng> inputRuanJianGongNengXiangMu = new ArrayList<>();
        inputRuanJianGongNengXiangMu.add(gongNeng1);
        inputRuanJianGongNengXiangMu.add(gongNeng2);

        JS003 newJson = new JS003()
                .setInputRuanJianMingCheng("某在线表格管理")
                .setInputBanBenHao("2012-xxx")
                .setInputRuanJianGongNengXiangMu(inputRuanJianGongNengXiangMu);

        System.out.println(docServiceJS003.fill("1111", newJson));
    }
}
