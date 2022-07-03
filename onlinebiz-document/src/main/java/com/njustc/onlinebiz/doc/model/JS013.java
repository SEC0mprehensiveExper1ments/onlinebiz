package com.njustc.onlinebiz.doc.model;

import com.njustc.onlinebiz.common.model.test.review.SchemeReview;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JS013 {
    private String inputRuanJianMingChen = "";
    private String inputBanBenHao = "";
    private String inputXiangMuBianHao = "";
    private String inputCeShiLeiBie = "";
    private String inputTongGuo01 = "";
    private String inputTongGuo02 = "";
    private String inputTongGuo03 = "";
    private String inputTongGuo04 = "";
    private String inputTongGuo05 = "";
    private String inputTongGuo06 = "";
    private String inputTongGuo07 = "";
    private String inputTongGuo08 = "";
    private String inputBuTongGuo01 = "";
    private String inputBuTongGuo02 = "";
    private String inputBuTongGuo03 = "";
    private String inputBuTongGuo04 = "";
    private String inputBuTongGuo05 = "";
    private String inputBuTongGuo06 = "";
    private String inputBuTongGuo07 = "";
    private String inputBuTongGuo08 = "";

    public JS013(SchemeReview schemeReview) {
        if (schemeReview == null) {
            throw new IllegalArgumentException("schemeReview is null");
        }
        List<SchemeReview.ConclusionRow> conclusions = schemeReview.getConclusions();
        //检查各个字段的非空性
        if (schemeReview.getSoftwareName() != null) {
            this.inputRuanJianMingChen = schemeReview.getSoftwareName();
        }
        if (schemeReview.getVersion() != null) {
            this.inputBanBenHao = schemeReview.getVersion();
        }
        if (schemeReview.getProjectId() != null) {
            this.inputXiangMuBianHao = schemeReview.getSerialNumber();
        }
        if (schemeReview.getTestType() != null) {
            this.inputCeShiLeiBie = schemeReview.getTestType();
        }

        if (conclusions != null && conclusions.size() > 7) {
            this.inputTongGuo01 = conclusions.get(0).isPassed() ? "是" : "否";
            this.inputTongGuo02 = conclusions.get(1).isPassed() ? "是" : "否";
            this.inputTongGuo03 = conclusions.get(2).isPassed() ? "是" : "否";
            this.inputTongGuo04 = conclusions.get(3).isPassed() ? "是" : "否";
            this.inputTongGuo05 = conclusions.get(4).isPassed() ? "是" : "否";
            this.inputTongGuo06 = conclusions.get(5).isPassed() ? "是" : "否";
            this.inputTongGuo07 = conclusions.get(6).isPassed() ? "是" : "否";
            this.inputTongGuo08 = conclusions.get(7).isPassed() ? "是" : "否";
            if (!conclusions.get(0).isPassed()) {
                this.inputBuTongGuo01 = conclusions.get(0).getMessage();
            }
            if (!conclusions.get(1).isPassed()) {
                this.inputBuTongGuo02 = conclusions.get(1).getMessage();
            }
            if (!conclusions.get(2).isPassed()) {
                this.inputBuTongGuo03 = conclusions.get(2).getMessage();
            }
            if (!conclusions.get(3).isPassed()) {
                this.inputBuTongGuo04 = conclusions.get(3).getMessage();
            }
            if (!conclusions.get(4).isPassed()) {
                this.inputBuTongGuo05 = conclusions.get(4).getMessage();
            }
            if (!conclusions.get(5).isPassed()) {
                this.inputBuTongGuo06 = conclusions.get(5).getMessage();
            }
            if (!conclusions.get(6).isPassed()) {
                this.inputBuTongGuo07 = conclusions.get(6).getMessage();
            }
            if (!conclusions.get(7).isPassed()) {
                this.inputBuTongGuo08 = conclusions.get(7).getMessage();
            }
        }
    }
}
