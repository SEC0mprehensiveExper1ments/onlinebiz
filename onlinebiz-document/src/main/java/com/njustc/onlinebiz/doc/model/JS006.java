package com.njustc.onlinebiz.doc.model;

import com.njustc.onlinebiz.common.model.test.scheme.Modification;
import com.njustc.onlinebiz.common.model.test.scheme.Schedule;
import com.njustc.onlinebiz.common.model.test.scheme.Scheme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JS006 {
    private String inputBanBenHao = "";
    private List<Modification> wenDangXiuGaiJiLu = new ArrayList<>();
    private String inputBiaoShi = "";
    private String inputXiTongGaiShu = "";
    private String inputWenDangGaiShu = "";
    private String inputJiXian = "";
    private String inputYingJian = "";
    private String inputRuanJian = "";
    private String inputQiTa = "";
    private String inputCanYuZuZhi = "";
    private String inputRenYuan = "";
    private String inputCeShiJiBie = "";
    private String inputCeShiLeiBie = "";
    private String inputYiBanCeShiTiaoJian = "";
    private String inputJiHuaZhiXingDeCeShi = "";
    private String inputCeShiYongLi = "";
    private String inputYuJiGongZuoRi = "";
    private Schedule zhiDingJiHua = new Schedule();
    private Schedule sheJiCeShi = new Schedule();
    private Schedule zhiXingCeShi = new Schedule();
    private Schedule pingGuCeShi = new Schedule();
    private String inputXuQiuKeZhuiZongXing = "";

    public JS006(Scheme scheme) {
        this.inputBanBenHao = scheme.getContent().getVersion();
        this.wenDangXiuGaiJiLu = scheme.getContent().getModificationList();
        this.inputBiaoShi = scheme.getContent().getLogo();
        this.inputXiTongGaiShu = scheme.getContent().getSystemSummary();
        this.inputWenDangGaiShu = scheme.getContent().getDocumentSummary();
        this.inputJiXian = scheme.getContent().getBaseline();
        this.inputYingJian = scheme.getContent().getHardware();
        this.inputRuanJian = scheme.getContent().getSoftware();
        this.inputQiTa = scheme.getContent().getOtherEnvironment();
        this.inputCanYuZuZhi = scheme.getContent().getOrganization();
        this.inputRenYuan = scheme.getContent().getParticipant();
        this.inputCeShiJiBie = scheme.getContent().getTestLevel();
        this.inputCeShiLeiBie = scheme.getContent().getTestType();
        this.inputYiBanCeShiTiaoJian = scheme.getContent().getTestCondition();
        this.inputJiHuaZhiXingDeCeShi = scheme.getContent().getTestToBeExecuted();
        this.inputCeShiYongLi = scheme.getContent().getTestSample();
        this.inputYuJiGongZuoRi = scheme.getContent().getExpectedTime();
        this.inputXuQiuKeZhuiZongXing = scheme.getContent().getTraceability();
        this.zhiDingJiHua = scheme.getContent().getPlanSchedule();
        this.sheJiCeShi = scheme.getContent().getDesignSchedule();
        this.zhiXingCeShi = scheme.getContent().getExecuteSchedule();
        this.pingGuCeShi = scheme.getContent().getEvaluateSchedule();
    }
}
