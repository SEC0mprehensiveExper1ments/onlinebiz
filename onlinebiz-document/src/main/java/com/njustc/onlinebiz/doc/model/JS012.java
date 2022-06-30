package com.njustc.onlinebiz.doc.model;


import com.njustc.onlinebiz.common.model.test.review.EntrustTestReview;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JS012 {
    private String inputRuanJianMingChen = "";
    private String inputBanBenHao = "";
    private String inputShenBaoDanWei = "";
    private String inputQiShiShiJian0Nian = "";
    private String inputQiShiShiJian0Yue = "";
    private String inputQiShiShiJian0Ri = "";
    private String inputYuJiShiJian0Nian = "";
    private String inputYuJiShiJian0Yue = "";
    private String inputYuJiShiJian0Ri = "";
    private String inputZhuCeRen = "";
    private String inputShiJiShiJian0Nian = "";
    private String inputShiJiShiJian0Yue = "";
    private String inputShiJiShiJian0Ri = "";
    private String inputQueRen011 = "";
    private String inputQueRen012 = "";
    private String inputQueRen013 = "";
    private String inputQueRen021 = "";
    private String inputQueRen022 = "";
    private String inputQueRen031 = "";
    private String inputQueRen032 = "";
    private String inputQueRen041 = "";
    private String inputQueRen051 = "";
    private String inputQueRen052 = "";
    private String inputQueRen053 = "";
    private String inputQueRen054 = "";
    private String inputQueRen055 = "";
    private String inputQueRen061 = "";
    private String inputQueRen071 = "";
    private String inputQueRen072 = "";
    private String inputQueRen073 = "";
    private String inputQueRen081 = "";
    private String inputQueRen082 = "";
    private String inputQueRen083 = "";
    private String inputQueRen084 = "";
    private String inputQueRen091 = "";
    private String inputQueRen092 = "";
    private String inputQueRen093 = "";

    public JS012(EntrustTestReview entrustTestReview) {
        this.inputRuanJianMingChen = entrustTestReview.getSoftwareName();
        this.inputBanBenHao = entrustTestReview.getVersion();
        this.inputShenBaoDanWei = entrustTestReview.getPrincipal();
        this.inputQiShiShiJian0Nian = entrustTestReview.getStartDate().getYear() + "";
        this.inputQiShiShiJian0Yue = entrustTestReview.getStartDate().getMonth() + "";
        this.inputQiShiShiJian0Ri = entrustTestReview.getStartDate().getDay() + "";
        this.inputYuJiShiJian0Nian = entrustTestReview.getExpectFinishDate().getYear() + "";
        this.inputYuJiShiJian0Yue = entrustTestReview.getExpectFinishDate().getMonth() + "";
        this.inputYuJiShiJian0Ri = entrustTestReview.getExpectFinishDate().getDay() + "";
        this.inputZhuCeRen = entrustTestReview.getMainTester();
        this.inputShiJiShiJian0Nian = entrustTestReview.getFinishDate().getYear() + "";
        this.inputShiJiShiJian0Yue = entrustTestReview.getFinishDate().getMonth() + "";
        this.inputShiJiShiJian0Ri = entrustTestReview.getFinishDate().getDay() + "";
        List<String> conclusions = entrustTestReview.getConclusions();
        this.inputQueRen011 = conclusions.get(0);
        this.inputQueRen012 = conclusions.get(1);
        this.inputQueRen013 = conclusions.get(2);
        this.inputQueRen021 = conclusions.get(3);
        this.inputQueRen022 = conclusions.get(4);
        this.inputQueRen031 = conclusions.get(5);
        this.inputQueRen032 = conclusions.get(6);
        this.inputQueRen041 = conclusions.get(7);
        this.inputQueRen051 = conclusions.get(8);
        this.inputQueRen052 = conclusions.get(9);
        this.inputQueRen053 = conclusions.get(10);
        this.inputQueRen054 = conclusions.get(11);
        this.inputQueRen055 = conclusions.get(12);
        this.inputQueRen061 = conclusions.get(13);
        this.inputQueRen071 = conclusions.get(14);
        this.inputQueRen072 = conclusions.get(15);
        this.inputQueRen073 = conclusions.get(16);
        this.inputQueRen081 = conclusions.get(17);
        this.inputQueRen082 = conclusions.get(18);
        this.inputQueRen083 = conclusions.get(19);
        this.inputQueRen084 = conclusions.get(20);
        this.inputQueRen091 = conclusions.get(21);
        this.inputQueRen092 = conclusions.get(22);
        this.inputQueRen093 = conclusions.get(23);
    }
}

