package com.njustc.onlinebiz.doc.model;


import com.njustc.onlinebiz.common.model.test.review.ReportReview;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JS010 {
    private String inputRuanJianMingChen = "";
    private String inputWeiTuoDanWei = "";
    private String inputJianChaJieGuo01 = "";
    private String inputJianChaJieGuo02 = "";
    private String inputJianChaJieGuo03 = "";
    private String inputJianChaJieGuo04 = "";
    private String inputJianChaJieGuo05 = "";
    private String inputJianChaJieGuo06 = "";
    private String inputJianChaJieGuo07 = "";
    private String inputJianChaJieGuo08 = "";
    private String inputJianChaJieGuo09 = "";
    private String inputJianChaJieGuo010 = "";
    private String inputJianChaJieGuo0111 = "";
    private String inputJianChaJieGuo0112 = "";
    private String inputJianChaJieGuo0113 = "";
    private String inputJianChaJieGuo012 = "";

    public JS010(ReportReview reportReview) {
        if (reportReview == null) {
            throw new IllegalArgumentException("reportReview is null");
        }
        this.inputRuanJianMingChen = reportReview.getSoftwareName();
        this.inputWeiTuoDanWei = reportReview.getPrincipal();
        List<ReportReview.ConclusionRow> conclusions = reportReview.getConclusions();
        if (conclusions != null && conclusions.size() == 14) {
            this.inputJianChaJieGuo01 = conclusions.get(0).isPassed() ? "通过" : "不通过";
            this.inputJianChaJieGuo02 = conclusions.get(1).isPassed() ? "通过" : "不通过";
            this.inputJianChaJieGuo03 = conclusions.get(2).isPassed() ? "通过" : "不通过";
            this.inputJianChaJieGuo04 = conclusions.get(3).isPassed() ? "通过" : "不通过";
            this.inputJianChaJieGuo05 = conclusions.get(4).isPassed() ? "通过" : "不通过";
            this.inputJianChaJieGuo06 = conclusions.get(5).isPassed() ? "通过" : "不通过";
            this.inputJianChaJieGuo07 = conclusions.get(6).isPassed() ? "通过" : "不通过";
            this.inputJianChaJieGuo08 = conclusions.get(7).isPassed() ? "通过" : "不通过";
            this.inputJianChaJieGuo09 = conclusions.get(8).isPassed() ? "通过" : "不通过";
            this.inputJianChaJieGuo010 = conclusions.get(9).isPassed() ? "通过" : "不通过";
            this.inputJianChaJieGuo0111 = conclusions.get(10).isPassed() ? "通过" : "不通过";
            this.inputJianChaJieGuo0112 = conclusions.get(11).isPassed() ? "通过" : "不通过";
            this.inputJianChaJieGuo0113 = conclusions.get(12).isPassed() ? "通过" : "不通过";
            this.inputJianChaJieGuo012 = conclusions.get(13).isPassed() ? "通过" : "不通过";
        }
        //增加字段的非空性检查
        if (this.inputRuanJianMingChen == null) {
            this.inputRuanJianMingChen = "";
        }
        if (this.inputWeiTuoDanWei == null) {
            this.inputWeiTuoDanWei = "";
        }
    }
}
