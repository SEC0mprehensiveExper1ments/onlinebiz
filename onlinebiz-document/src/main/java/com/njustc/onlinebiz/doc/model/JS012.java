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
        this.inputQiShiShiJian0Nian = entrustTestReview.getStartDate().substring(0, 4);
        this.inputQiShiShiJian0Yue = entrustTestReview.getStartDate().substring(5, 7);
        this.inputQiShiShiJian0Ri = entrustTestReview.getStartDate().substring(8, 10);
        this.inputYuJiShiJian0Nian = entrustTestReview.getExpectFinishDate().substring(0, 4);
        this.inputYuJiShiJian0Yue = entrustTestReview.getExpectFinishDate().substring(5, 7);
        this.inputYuJiShiJian0Ri = entrustTestReview.getExpectFinishDate().substring(8, 10);
        this.inputZhuCeRen = entrustTestReview.getMainTester();
        this.inputShiJiShiJian0Nian = entrustTestReview.getFinishDate().substring(0, 4);
        this.inputShiJiShiJian0Yue = entrustTestReview.getFinishDate().substring(5, 7);
        this.inputShiJiShiJian0Ri = entrustTestReview.getFinishDate().substring(8, 10);
        List<String> conclusions = entrustTestReview.getConclusions();
        if (conclusions.size() == 24) {
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
        //增加字段的非空性检查
        if (this.inputRuanJianMingChen == null) this.inputRuanJianMingChen = "";
        if (this.inputBanBenHao == null) this.inputBanBenHao = "";
        if (this.inputShenBaoDanWei == null) this.inputShenBaoDanWei = "";
        if (this.inputZhuCeRen == null) this.inputZhuCeRen = "";
        if (this.inputQueRen011 == null) this.inputQueRen011 = "";
        if (this.inputQueRen012 == null) this.inputQueRen012 = "";
        if (this.inputQueRen013 == null) this.inputQueRen013 = "";
        if (this.inputQueRen021 == null) this.inputQueRen021 = "";
        if (this.inputQueRen022 == null) this.inputQueRen022 = "";
        if (this.inputQueRen031 == null) this.inputQueRen031 = "";
        if (this.inputQueRen032 == null) this.inputQueRen032 = "";
        if (this.inputQueRen041 == null) this.inputQueRen041 = "";
        if (this.inputQueRen051 == null) this.inputQueRen051 = "";
        if (this.inputQueRen052 == null) this.inputQueRen052 = "";
        if (this.inputQueRen053 == null) this.inputQueRen053 = "";
        if (this.inputQueRen054 == null) this.inputQueRen054 = "";
        if (this.inputQueRen055 == null) this.inputQueRen055 = "";
        if (this.inputQueRen061 == null) this.inputQueRen061 = "";
        if (this.inputQueRen071 == null) this.inputQueRen071 = "";
        if (this.inputQueRen072 == null) this.inputQueRen072 = "";
        if (this.inputQueRen073 == null) this.inputQueRen073 = "";
        if (this.inputQueRen081 == null) this.inputQueRen081 = "";
        if (this.inputQueRen082 == null) this.inputQueRen082 = "";
        if (this.inputQueRen083 == null) this.inputQueRen083 = "";
        if (this.inputQueRen084 == null) this.inputQueRen084 = "";
        if (this.inputQueRen091 == null) this.inputQueRen091 = "";
        if (this.inputQueRen092 == null) this.inputQueRen092 = "";
        if (this.inputQueRen093 == null) this.inputQueRen093 = "";
    }
}

