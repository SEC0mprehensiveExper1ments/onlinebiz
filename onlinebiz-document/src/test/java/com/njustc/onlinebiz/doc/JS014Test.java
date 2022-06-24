package com.njustc.onlinebiz.doc;

import com.njustc.onlinebiz.doc.model.JS014;
import com.njustc.onlinebiz.doc.service.DocServiceJS014;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JS014Test {

    @Autowired
    DocServiceJS014 docServiceJS014;

    @Test
    void testFillJS014Success() {
        JS014 newJson = new JS014()
                .setInputRuanJianMingChen("某在线表格管理平台")
                .setInputBanBenHao("XXXX-2012")
                .setInputWeiTuoDanWei("高校评审组")
                .setInputPingShenRen("高校评审组")
                .setInputPingShenShiJian0Nian("2022")
                .setInputPingShenShiJian0Yue("12")
                .setInputPingShenShiJian0Ri("30")
                .setInputPingShenJieGuo011("是可用的")
                .setInputPingShenJieGuo0121("是可用的")
                .setInputPingShenJieGuo0122("是可用的")
                .setInputPingShenJieGuo0123("是可用的")
                .setInputPingShenJieGuo0131("是可用的")
                .setInputPingShenJieGuo0132("是可用的")
                .setInputPingShenJieGuo0133("是可用的")
                .setInputPingShenJieGuo014("是可用的")
                .setInputPingShenJieGuo015("是可用的")
                .setInputPingShenJieGuo016("是可用的")
                .setInputPingShenJieGuo017("是可用的")
                .setInputPingShenJieGuo018("是可用的")
                .setInputPingShenJieGuo019("是可用的")
                .setInputPingShenJieGuo0110("是可用的")
                .setInputPingShenShuoMing011("确实是可用的")
                .setInputPingShenShuoMing0121("确实是可用的")
                .setInputPingShenShuoMing0122("确实是可用的")
                .setInputPingShenShuoMing0123("确实是可用的")
                .setInputPingShenShuoMing0131("确实是可用的")
                .setInputPingShenShuoMing0132("确实是可用的")
                .setInputPingShenShuoMing0133("确实是可用的")
                .setInputPingShenShuoMing014("确实是可用的")
                .setInputPingShenShuoMing015("确实是可用的")
                .setInputPingShenShuoMing016("确实是可用的")
                .setInputPingShenShuoMing017("确实是可用的")
                .setInputPingShenShuoMing018("确实是可用的")
                .setInputPingShenShuoMing019("确实是可用的")
                .setInputPingShenShuoMing0110("确实是可用的")
                .setInputPingShenJieGuo0211("")
                .setInputPingShenJieGuo0212("")
                .setInputPingShenJieGuo0213("")
                .setInputPingShenJieGuo0214("")
                .setInputPingShenJieGuo0215("")
                .setInputPingShenJieGuo0216("")
                .setInputPingShenJieGuo0217("")
                .setInputPingShenJieGuo0218("")
                .setInputPingShenJieGuo0219("")
                .setInputPingShenJieGuo02110("")
                .setInputPingShenJieGuo02111("")
                .setInputPingShenJieGuo0221("")
                .setInputPingShenJieGuo0222("")
                .setInputPingShenJieGuo0231("")
                .setInputPingShenJieGuo0241("")
                .setInputPingShenJieGuo0242("")
                .setInputPingShenJieGuo0251("")
                .setInputPingShenJieGuo0261("")
                .setInputPingShenJieGuo0262("")
                .setInputPingShenJieGuo0263("")
                .setInputPingShenShuoMing0211("")
                .setInputPingShenShuoMing0212("")
                .setInputPingShenShuoMing0213("")
                .setInputPingShenShuoMing0214("")
                .setInputPingShenShuoMing0215("")
                .setInputPingShenShuoMing0216("")
                .setInputPingShenShuoMing0217("")
                .setInputPingShenShuoMing0218("")
                .setInputPingShenShuoMing0219("")
                .setInputPingShenShuoMing02110("")
                .setInputPingShenShuoMing02111("")
                .setInputPingShenShuoMing0221("")
                .setInputPingShenShuoMing0222("")
                .setInputPingShenShuoMing0231("")
                .setInputPingShenShuoMing0241("")
                .setInputPingShenShuoMing0242("")
                .setInputPingShenShuoMing0251("")
                .setInputPingShenShuoMing0261("")
                .setInputPingShenShuoMing0262("")
                .setInputPingShenShuoMing0263("");

        System.out.println(docServiceJS014.fill("1111", newJson));

    }
}
