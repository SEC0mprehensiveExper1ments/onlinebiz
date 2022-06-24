package com.njustc.onlinebiz.doc;

import com.njustc.onlinebiz.common.model.entrust.Entrust;
import com.njustc.onlinebiz.doc.dao.OSSProvider;
import com.njustc.onlinebiz.doc.model.JS002;
import com.njustc.onlinebiz.doc.service.DocServiceJS002;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;
@SpringBootTest
public class JS002Test {

    private final OSSProvider ossProvider = mock(OSSProvider.class);
    private final RestTemplate restTemplate = mock(RestTemplate.class);
    DocServiceJS002 docServiceJS002 = new DocServiceJS002(ossProvider);
//    @Autowired
//    DocServiceJS002 docServiceJS002;



    @Test
    void testFillJS002Success() {
        JS002 newJson = new JS002()
                .setMultiCeShiLeiXing01("1")
                .setMultiCeShiLeiXing02("1")
                .setInputCeShiLeiXing02("测试一下")
                .setMultiCeShiLeiXing03("1")
                .setMultiCeShiLeiXing04("1")
                .setInputRuanJianMingChen("某在线表格管理系统管理系统管理系统管理。")
                .setInputBanBenHao("XXX-2012")
                .setInputWeiTuoDanWeiZhongWen("某在线表格管理系统")
                .setInputWeiTuoDanWeiYingWen("某在线表格管理系统")
                .setInputKaiFaDanWei("某在线表格管理系统")
                .setSingleDanWeiXingZhi01("1")
                .setSingleDanWeiXingZhi02("1")
                .setSingleDanWeiXingZhi03("1")
                .setSingleDanWeiXingZhi04("1")
                .setSingleDanWeiXingZhi05("1")
                .setSingleDanWeiXingZhi06("1")
                .setInputRuanJianMiaoShu("\n\n")
                .setInputZhuYaoGongNeng("\n\n\n\n")
                .setMultiCeShiYiJu01("")
                .setMultiCeShiYiJu02("1")
                .setMultiCeShiYiJu03("")
                .setMultiCeShiYiJu04("")
                .setMultiCeShiYiJu05("1")
                .setInputCeShiYiJu05("1")
                .setMultiJiShuZhiBiao01("")
                .setMultiJiShuZhiBiao02("")
                .setMultiJiShuZhiBiao03("")
                .setMultiJiShuZhiBiao04("")
                .setMultiJiShuZhiBiao05("1")
                .setMultiJiShuZhiBiao06("1")
                .setMultiJiShuZhiBiao07("1")
                .setMultiJiShuZhiBiao08("1")
                .setMultiJiShuZhiBiao09("1")
                .setMultiJiShuZhiBiao010("1")
                .setMultiJiShuZhiBiao011("1")
                .setMultiJiShuZhiBiao012("1")
                .setMultiJiShuZhiBiao013("")
                .setInputJiShuZhiBiao013("测试一下")
                .setInputRuanJianGuiMo01("9")
                .setInputRuanJianGuiMo02("9")
                .setInputRuanJianGuiMo03("999")
                .setSingleRuanJianLeiXing0XiTong01("")
                .setSingleRuanJianLeiXing0XiTong02("")
                .setSingleRuanJianLeiXing0XiTong03("")
                .setSingleRuanJianLeiXing0XiTong04("1")
                .setSingleRuanJianLeiXing0XiTong05("1")
                .setSingleRuanJianLeiXing0ZhiChi01("1")
                .setSingleRuanJianLeiXing0ZhiChi02("")
                .setSingleRuanJianLeiXing0ZhiChi03("")
                .setSingleRuanJianLeiXing0ZhiChi04("")
                .setSingleRuanJianLeiXing0ZhiChi05("")
                .setSingleRuanJianLeiXing0ZhiChi06("")
                .setSingleRuanJianLeiXing0YingYong01("")
                .setSingleRuanJianLeiXing0YingYong02("")
                .setSingleRuanJianLeiXing0YingYong03("")
                .setSingleRuanJianLeiXing0YingYong04("")
                .setSingleRuanJianLeiXing0YingYong05("")
                .setSingleRuanJianLeiXing0YingYong06("")
                .setSingleRuanJianLeiXing0YingYong07("")
                .setSingleRuanJianLeiXing0YingYong08("")
                .setSingleRuanJianLeiXing0YingYong09("")
                .setSingleRuanJianLeiXing0YingYong010("")
                .setSingleRuanJianLeiXing0YingYong011("")
                .setSingleRuanJianLeiXing0YingYong012("")
                .setSingleRuanJianLeiXing0YingYong013("")
                .setSingleRuanJianLeiXing0QiTa01("")
                .setMultiKeHuDuan0Windows("")
                .setInputKeHuDuan0Windows("")
                .setMultiKeHuDuan0Linux("")
                .setInputKeHuDuan0Linux("")
                .setMultiKeHuDuan0QiTa("")
                .setInputKeHuDuan0QiTa("")
                .setInputKeHuDuan0NeiCunYaoQiu("100")
                .setInputKeHuDuan0QiTaYaoQiu("")
                .setMultiFuWuQiYingJian0PC("")
                .setMultiFuWuQiYingJian0Linux("")
                .setMultiFuWuQiYingJian0QiTa("")
                .setInputFuWuQiYingJian0QiTa("")
                .setInputFuWuQiYingJian0NeiCunYaoQiu("")
                .setInputFuWuQiYingJian0YingPanYaoQiu("")
                .setInputFuWuQiYingJian0QiTaYaoQiu("")
                .setInputFuWuQiRuanJian0CaoZuoXiTong("")
                .setInputFuWuQiRuanJian0BanBen("")
                .setInputFuWuQiRuanJian0BianChengYuYan("")
                .setMultiFuWuQiRuanJian0GouJia0CS("")
                .setMultiFuWuQiRuanJian0GouJia0BS("1")
                .setMultiFuWuQiRuanJian0GouJia0QiTa("1")
                .setInputFuWuQiRuanJian0ShuJuKu("")
                .setInputFuWuQiRuanJian0ZhongJianJian("")
                .setInputFuWuQiRuanJian0QiTaZhiCheng("")
                .setInputWangLuoHuanJing("")
                .setSingleRuanJianJieZhi0GuangPan("1")
                .setSingleRuanJianJieZhi0UPan("1")
                .setSingleRuanJianJieZhi0QiTa("1")
                .setInputRuanJianJieZhi0QiTa("")
                .setInputWenDangZiLiao("\n\n")
                .setSingleYangPingChuLi01("")
                .setSingleYangPingChuLi02("")
                .setInputWeiTuoDanWei0DianHua("86-25-89686596")
                .setInputWeiTuoDanWei0ChuanZhen("86-25-89686596")
                .setInputWeiTuoDanWei0DiZhi("南京市栖霞区仙林大道163号")
                .setInputWeiTuoDanWei0YouBian("210023")
                .setInputWeiTuoDanWei0LianXiRen(" ")
                .setInputWeiTuoDanWei0ShouJi(" ")
                .setInputWeiTuoDanWei0Email("keysoftlab@nju.edu.cn")
                .setInputWeiTuoDanWei0WangZhi("http://keysoftlab.nju.edu.cn")
                .setSingleMiJi01("")
                .setSingleMiJi02("1")
                .setSingleMiJi03("")
                .setSingleChaShaBingDu01("")
                .setSingleChaShaBingDu02("")
                .setInputChaShaBingDu02("金山毒霸")
                .setMultiCaiLiaoJianCha0CeshiYangPing01("1")
                .setMultiCaiLiaoJianCha0CeshiYangPing02("1")
                .setMultiCaiLiaoJianCha0XuQiuWenDang01("1")
                .setMultiCaiLiaoJianCha0XuQiuWenDang02("1")
                .setMultiCaiLiaoJianCha0XuQiuWenDang03("1")
                .setMultiCaiLiaoJianCha0YongHuWenDang01("1")
                .setMultiCaiLiaoJianCha0YongHuWenDang02("1")
                .setMultiCaiLiaoJianCha0CaoZuoWenDang01("1")
                .setMultiCaiLiaoJianCha0CaoZuoWenDang02("1")
                .setMultiCaiLiaoJianCha0CaoZuoWenDang03("1")
                .setMultiCaiLiaoJianCha0CaoZuoWenDang04("1")
                .setInputCaiLiaoJianCha0QiTa("")
                .setSingleQueRenYiJian01("")
                .setSingleQueRenYiJian02("")
                .setSingleQueRenYiJian03("")
                .setSingleQueRenYiJian04("")
                .setSingleShouLiYiJian01("")
                .setSingleShouLiYiJian02("")
                .setSingleShouLiYiJian03("")
                .setInputCeShiXiangMuBianHao("")
                .setInputBeiZhu("");

        ReflectionTestUtils.setField(docServiceJS002, "DOCUMENT_DIR", "/");
        System.out.println(docServiceJS002.fill("111", newJson));
    }
}
