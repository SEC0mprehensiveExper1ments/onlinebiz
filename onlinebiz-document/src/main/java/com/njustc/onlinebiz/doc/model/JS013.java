package com.njustc.onlinebiz.doc.model;

import com.njustc.onlinebiz.common.model.test.review.SchemeReview;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
        // TODO: 格式转换
    }
}
