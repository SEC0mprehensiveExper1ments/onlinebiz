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

    public String safe(String s) {
        return s == null ? "" : s;
    }

    public JS012(EntrustTestReview entrustTestReview) {
        if (entrustTestReview == null) {
            throw new IllegalArgumentException("entrustTestReview is null");
        }
        this.inputRuanJianMingChen = entrustTestReview.getSoftwareName();
        this.inputBanBenHao = entrustTestReview.getVersion();
        this.inputShenBaoDanWei = entrustTestReview.getPrincipal();
        if (entrustTestReview.getStartDate() != null) {
            this.inputQiShiShiJian0Nian = entrustTestReview.getStartDate().substring(0, 4);
            this.inputQiShiShiJian0Yue = entrustTestReview.getStartDate().substring(5, 7);
            this.inputQiShiShiJian0Ri = entrustTestReview.getStartDate().substring(8, 10);
        }
        if (entrustTestReview.getExpectFinishDate() != null) {
            this.inputYuJiShiJian0Nian = entrustTestReview.getExpectFinishDate().substring(0, 4);
            this.inputYuJiShiJian0Yue = entrustTestReview.getExpectFinishDate().substring(5, 7);
            this.inputYuJiShiJian0Ri = entrustTestReview.getExpectFinishDate().substring(8, 10);
        }
        this.inputZhuCeRen = entrustTestReview.getMainTester();
        if (entrustTestReview.getFinishDate() != null) {
            this.inputShiJiShiJian0Nian = entrustTestReview.getFinishDate().substring(0, 4);
            this.inputShiJiShiJian0Yue = entrustTestReview.getFinishDate().substring(5, 7);
            this.inputShiJiShiJian0Ri = entrustTestReview.getFinishDate().substring(8, 10);
        }
        List<String> conclusions = entrustTestReview.getConclusions();
        if (conclusions != null && conclusions.size() == 24) {
            this.inputQueRen011 = safe(conclusions.get(0));
            this.inputQueRen012 = safe(conclusions.get(1));
            this.inputQueRen013 = safe(conclusions.get(2));
            this.inputQueRen021 = safe(conclusions.get(3));
            this.inputQueRen022 = safe(conclusions.get(4));
            this.inputQueRen031 = safe(conclusions.get(5));
            this.inputQueRen032 = safe(conclusions.get(6));
            this.inputQueRen041 = safe(conclusions.get(7));
            this.inputQueRen051 = safe(conclusions.get(8));
            this.inputQueRen052 = safe(conclusions.get(9));
            this.inputQueRen053 = safe(conclusions.get(10));
            this.inputQueRen054 = safe(conclusions.get(11));
            this.inputQueRen055 = safe(conclusions.get(12));
            this.inputQueRen061 = safe(conclusions.get(13));
            this.inputQueRen071 = safe(conclusions.get(14));
            this.inputQueRen072 = safe(conclusions.get(15));
            this.inputQueRen073 = safe(conclusions.get(16));
            this.inputQueRen081 = safe(conclusions.get(17));
            this.inputQueRen082 = safe(conclusions.get(18));
            this.inputQueRen083 = safe(conclusions.get(19));
            this.inputQueRen084 = safe(conclusions.get(20));
            this.inputQueRen091 = safe(conclusions.get(21));
            this.inputQueRen092 = safe(conclusions.get(22));
            this.inputQueRen093 = safe(conclusions.get(23));
        }
    }
}

