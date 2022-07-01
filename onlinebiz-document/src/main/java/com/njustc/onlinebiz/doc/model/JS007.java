package com.njustc.onlinebiz.doc.model;

import com.njustc.onlinebiz.common.model.test.report.Report;
import com.njustc.onlinebiz.common.model.test.report.SoftwareEnvironment;
import com.njustc.onlinebiz.common.model.test.report.TestContent;
import com.njustc.onlinebiz.common.model.test.testrecord.TestRecordList;
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
public class JS007 {
    private String inputBaoGaoBianHao="";
    private String inputRuanJianMingCheng="";
    private String inputBanBenHao="";
    private String inputWeiTuoDanWei="";
    private String inputCeShiLeiBie="";
    private String inputBaoGaoRiQiNian="";
    private String inputBaoGaoRiQiYue="";
    private String inputBaoGaoRiQiRi="";
    /* 子项先从左往右，从上至下，递增 */
    private String inputXiangMuBianHao="";
    private String inputYangPinMingCheng="";
    private String inputBanBenXingHao="";
    private String inputLaiYangRiQiNian="";
    private String inputLaiYangRiQiYue="";
    private String inputLaiYangRiQiRi="";
    private String inputCeShiKaiShiRiQiNian="";
    private String inputCeShiKaiShiRiQiYue="";
    private String inputCeShiKaiShiRiQiRi="";
    private String inputCeShiJieShuRiQiNian="";
    private String inputCeShiJieShuRiQiYue="";
    private String inputCeShiJieShuRiQiRi="";

    private String inputYangPinZhuangTai="";
    private String inputCeShiYiJu="";
    private String inputYangPinQingDan="";
    private String inputCeShiJieLun="";
    private String inputZhuCeRen="";
    private String inputZhuCeRiQiNian="";
    private String inputZhuCeRiQiYue="";
    private String inputZhuCeRiQiRi="";
    private String inputShenHeRen="";
    private String inputShenHeRiQiNian="";
    private String inputShenHeRiQiYue="";
    private String inputShenHeRiQiRi="";
    private String inputPiZhunRen="";
    private String inputPiZhunRiQiNian="";
    private String inputPiZhunRiQiYue="";
    private String inputPiZhunRiQiRi="";

    private String inputDianHua="";
    private String inputChuanZhen="";
    private String inputDiZhi="";
    private String inputYouBian="";
    private String inputLianXiRen="";
    private String inputEmail="";

    private String inputYingJianLeiBie="";
    private String inputYingJianMingCheng="";
    private String inputYingJianPeiZhi="";
    private String inputYingJianShuLiang="";
    private String inputCaoZuoXiTongMingCheng="";
    private String inputCaoZuoXiTongBanBen="";
    private List<SoftwareEnvironment> inputRuanJianHuanJing=new ArrayList<>();
    private String inputWangLuoHuanJing="";
    private List<String> inputTestBases=new ArrayList<>();
    private List<String> inputCanKaoZiLiao=new ArrayList<>();
    private List<TestContent> inputGongNengXingCeShi=new ArrayList<>();
    private List<TestContent> inputXiaoLvCeShi=new ArrayList<>();
    private List<TestContent> inputKeYiZhiXingCeShi=new ArrayList<>();
    private List<TestContent> inputYiYongXingCeShi=new ArrayList<>();
    private List<TestContent> inputKeKaoXingCeShi=new ArrayList<>();
    private List<TestContent> inputKeWeiHuXingCeShi=new ArrayList<>();
    private List<TestRecordList.TestRecord> inputCeShiZhiXingJiLu=new ArrayList<>();

    public JS007(Report report){
        this.inputBaoGaoBianHao=report.getContent().getId();
        this.inputRuanJianMingCheng=report.getContent().getSoftwareName();
        this.inputBanBenHao=report.getContent().getVersion();
        this.inputWeiTuoDanWei=report.getContent().getClientContact().getCompanyCH();
        this.inputCeShiLeiBie=report.getContent().getTestType();

        this.inputBaoGaoRiQiNian=report.getContent().getReportDate().substring(0,4);
        this.inputBaoGaoRiQiYue=report.getContent().getReportDate().substring(5,7);
        this.inputBaoGaoRiQiRi=report.getContent().getReportDate().substring(8,10);
        this.inputXiangMuBianHao=report.getContent().getProjectId();
        this.inputYangPinMingCheng=report.getContent().getSampleName();
        this.inputBanBenXingHao=report.getContent().getSampleVersion();

        this.inputLaiYangRiQiNian=report.getContent().getSampleDate().substring(0,4);
        this.inputLaiYangRiQiYue=report.getContent().getSampleDate().substring(5,7);
        this.inputLaiYangRiQiRi=report.getContent().getSampleDate().substring(8,10);
        this.inputCeShiKaiShiRiQiNian=report.getContent().getTestStartTime().substring(0,4);
        this.inputCeShiKaiShiRiQiYue=report.getContent().getTestStartTime().substring(5,7);
        this.inputCeShiKaiShiRiQiRi=report.getContent().getTestStartTime().substring(8,10);
        this.inputCeShiJieShuRiQiNian=report.getContent().getTestEndTime().substring(0,4);
        this.inputCeShiJieShuRiQiYue=report.getContent().getTestEndTime().substring(5,7);
        this.inputCeShiJieShuRiQiRi=report.getContent().getTestEndTime().substring(8,10);

        this.inputYangPinZhuangTai=report.getContent().getSampleStatus();
        this.inputCeShiYiJu=report.getContent().getTestBasis();
        this.inputYangPinQingDan=report.getContent().getSampleList();
        this.inputCeShiJieLun=report.getContent().getTestConclusion();
        this.inputZhuCeRen=report.getContent().getMainTester();
        //example:"2022-05-11T07:10:10.885Z"
        String zhuCeRiQi=report.getContent().getMainTesterDate();
        this.inputZhuCeRiQiNian=zhuCeRiQi.substring(0,4);
        this.inputZhuCeRiQiYue=zhuCeRiQi.substring(5,7);
        this.inputZhuCeRiQiRi=zhuCeRiQi.substring(8,10);

        this.inputShenHeRen=report.getContent().getAuditor();

        String shenHeRiQi=report.getContent().getAuditorDate();
        this.inputShenHeRiQiNian=shenHeRiQi.substring(0,4);
        this.inputShenHeRiQiYue=shenHeRiQi.substring(5,7);
        this.inputShenHeRiQiRi=shenHeRiQi.substring(8,10);

        this.inputPiZhunRen=report.getContent().getApprover();

        String piZhunRiQi=report.getContent().getApproverDate();
        this.inputPiZhunRiQiNian=piZhunRiQi.substring(0,4);
        this.inputPiZhunRiQiYue=piZhunRiQi.substring(5,7);
        this.inputPiZhunRiQiRi=piZhunRiQi.substring(8,10);

        this.inputDianHua=report.getContent().getClientContact().getContactPhone();
        this.inputChuanZhen=report.getContent().getClientContact().getFax();
        this.inputDiZhi=report.getContent().getClientContact().getCompanyAddress();
        this.inputYouBian=report.getContent().getClientContact().getZipCode();
        this.inputLianXiRen=report.getContent().getClientContact().getContact();
        this.inputEmail=report.getContent().getClientContact().getContactEmail();

        this.inputYingJianLeiBie=report.getContent().getHardwareType();
        this.inputYingJianMingCheng=report.getContent().getHardwareName();
        this.inputYingJianPeiZhi=report.getContent().getHardwareConfig();
        this.inputYingJianShuLiang=report.getContent().getHardwareNum();
        this.inputCaoZuoXiTongMingCheng=report.getContent().getOsSoftwareName();
        this.inputCaoZuoXiTongBanBen=report.getContent().getOsVersion();
        this.inputRuanJianHuanJing=report.getContent().getSoftwareEnvironments();
        this.inputWangLuoHuanJing=report.getContent().getNetworkEnvironment();
        this.inputTestBases=report.getContent().getTestBases();
        this.inputCanKaoZiLiao=report.getContent().getReferenceMaterials();
        this.inputGongNengXingCeShi=report.getContent().getFunctionalTests();
        this.inputXiaoLvCeShi=report.getContent().getEfficiencyTests();
        this.inputKeYiZhiXingCeShi=report.getContent().getPortableTests();
        this.inputYiYongXingCeShi=report.getContent().getUsabilityTests();
        this.inputKeKaoXingCeShi=report.getContent().getReliabilityTests();
        this.inputKeWeiHuXingCeShi=report.getContent().getMaintainabilityTests();
        this.inputCeShiZhiXingJiLu=report.getContent().getTestRecords();

        //添加字段非空性检查
        if (this.inputBaoGaoBianHao == null) this.inputBaoGaoBianHao = "";
        if (this.inputRuanJianMingCheng == null) this.inputRuanJianMingCheng = "";
        if (this.inputBanBenHao == null) this.inputBanBenHao = "";
        if (this.inputWeiTuoDanWei == null) this.inputWeiTuoDanWei = "";
        if (this.inputCeShiLeiBie == null) this.inputCeShiLeiBie = "";

    }
}
